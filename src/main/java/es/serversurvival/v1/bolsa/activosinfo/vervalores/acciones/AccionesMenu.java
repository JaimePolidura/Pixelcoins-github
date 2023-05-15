package es.serversurvival.v1.bolsa.activosinfo.vervalores.acciones;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.Page;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitbettermenus.menustate.AfterShow;
import es.bukkitbettermenus.modules.pagination.PaginationConfiguration;
import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.bukkitclassmapper._shared.utils.ItemUtils;
import es.serversurvival.v1.bolsa.activosinfo._shared.application.ActivoInfoDataService;
import es.serversurvival.v1.bolsa.activosinfo._shared.domain.tipoactivos.TipoActivo;
import es.serversurvival.v1.bolsa.activosinfo.vervalores.ComprarBolsaConfirmacionMenu;
import es.serversurvival.v1.bolsa.activosinfo.vervalores.ComprarBolsaConfirmacionMenuState;
import es.serversurvival.v1.jugadores._shared.application.JugadoresService;
import es.serversurvival.v1.jugadores._shared.domain.Jugador;
import es.serversurvival.v1._shared.utils.Funciones;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutorService;

import static org.bukkit.ChatColor.*;

@RequiredArgsConstructor
public final class AccionesMenu extends Menu implements AfterShow {
    private final ActivoInfoDataService activoInfoDataService;
    private final JugadoresService jugadoresService;
    private final ExecutorService executor;
    private final MenuService menuService;

    @Override
    public int[][] items() {
        return new int[][] {
                {1, 2, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 7, 8, 9}
        };
    }

    @Override
    public MenuConfiguration configuration() {
        return MenuConfiguration.builder()
                .title(DARK_RED + "" + BOLD + "   Escoge para invertir")
                .fixedItems()
                .item(1, itemInfo())
                .items(2, itemsAcciones(), this::onAccionClicked)
                .breakpoint(7)
                .paginated(PaginationConfiguration.builder()
                        .backward(8, itemBackPage())
                        .forward(9, itemNextPage())
                        .build())
                .build();
    }

    private void onAccionClicked(Player player, InventoryClickEvent event) {
        if(!hasLoaded(event.getCurrentItem())) return;

        ItemStack itemClicked = event.getCurrentItem();
        String ticker = ItemUtils.getLore(itemClicked, 0).split(" ")[1];
        String priceString = ItemUtils.getLore(itemClicked, 1).split(" ")[1]
                .replaceAll("\\.", "")
                .replace(",", ".");

        double precio = Double.parseDouble(priceString);

        Jugador jugador = this.jugadoresService.getByNombre(player.getName());

        if(precio > jugador.getPixelcoins()){
            player.sendMessage(DARK_RED + "No tienes las suficientes pixelcoins");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
            return;
        }

        this.menuService.open(player, ComprarBolsaConfirmacionMenu.class, new ComprarBolsaConfirmacionMenuState(
                jugador, ticker, precio, TipoActivo.ACCIONES
        ));
    }

    private boolean hasLoaded(ItemStack item){
        return item.getItemMeta().getLore().stream()
                .noneMatch(lore -> lore.contains("Cargando..."));
    }

    private List<ItemStack> itemsAcciones() {
        Map<String, String> allAcciones = TodasAccionesVerValores.accciones;
        List<ItemStack> items = new ArrayList<>(allAcciones.size());

        for(Map.Entry<String, String> entry : allAcciones.entrySet()){
            String ticker = entry.getKey();
            String nombre = entry.getValue();

            items.add(buildItemAccion(ticker, nombre));
        }

        return items;
    }

    private ItemStack buildItemAccion(String ticker, String nombre) {
        return ItemBuilder.of(Material.BOOK)
                .title(GOLD + "" + BOLD + nombre)
                .lore(List.of(
                        GOLD + "Ticker: " + ticker,
                        RED + "Cargando..."))
                .build();
    }

    private ItemStack itemNextPage() {
        return ItemBuilder.of(Material.GREEN_WOOL)
                .title(GREEN + "" + BOLD + "->")
                .build();
    }

    private ItemStack itemBackPage () {
        return ItemBuilder.of(Material.RED_WOOL)
                .title(RED + "" + BOLD + "<-")
                .build();
    }

    private ItemStack itemInfo () {
        List<String> infolore = new ArrayList<>();
        infolore.add("Para invertir en estas cantidad: /bolsa invertir <ticker> <nº cantidad>");
        infolore.add("                  ");
        infolore.add("Estas cantidad son ejemplos con las que se puede comprar, ");
        infolore.add("si quieres comprar otra accion que no este en la lista adelante");
        infolore.add(" solo que necesitas encontrar el ticker en internet, ");
        infolore.add("se puede encontrar en cualquier pagina como por ejemplo");
        infolore.add("es.investing.com. Hay que aclarar que hay cantidad que se puede ");
        infolore.add("y otras no la mayoria que se pueden son americanas.");

        String displayname = AQUA + "" + BOLD + "INFO";

        return ItemBuilder.of(Material.PAPER).title(displayname).lore(infolore).build();
    }

    @Override
    public void afterShow(Player player) {
        List<ItemStack> itemsToEdit = getItemsAccionesToEdit();
        this.executor.execute(() -> {
            itemsToEdit.forEach(this::addPriceToAccionItem);
        });
    }

    private void addPriceToAccionItem(ItemStack itemToEdit) {
        try {
            String ticker = ItemUtils.getLore(itemToEdit, 0).split(" ")[1];
            double precio = activoInfoDataService.getPrecio(TipoActivo.ACCIONES, ticker);

            ItemUtils.setLore(itemToEdit, 1, GOLD + "Precio: " + GREEN + Funciones.FORMATEA.format(precio) + " PC");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<ItemStack> getItemsAccionesToEdit() {
        List<Page> allPages = getPages();

        return allPages.stream()
                .map(Page::getInventory)
                .map(Inventory::getContents)
                .flatMap(Arrays::stream)
                .filter(item -> item != null && item.getType().equals(Material.BOOK))
                .toList();
    }
}
