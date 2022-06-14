package es.serversurvival.bolsa.activosinfo.vervalores.acciones;

import es.jaimetruman.ItemBuilder;
import es.jaimetruman.ItemUtils;
import es.jaimetruman.menus.Menu;
import es.jaimetruman.menus.MenuService;
import es.jaimetruman.menus.Page;
import es.jaimetruman.menus.configuration.MenuConfiguration;
import es.jaimetruman.menus.menustate.AfterShow;
import es.jaimetruman.menus.modules.pagination.PaginationConfiguration;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.activosinfo._shared.application.ActivosInfoService;
import es.serversurvival.bolsa.activosinfo._shared.domain.ActivoInfo;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.TipoActivo;
import es.serversurvival.bolsa.activosinfo.vervalores.ComprarBolsaConfirmacionMenu;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ExecutorService;

import static es.serversurvival._shared.utils.Funciones.*;
import static org.bukkit.ChatColor.*;

public final class AccionesMenu extends Menu implements AfterShow {
    private final ActivosInfoService activosInfoService;
    private final MenuService menuService;
    private final ExecutorService executor;
    private boolean hasLoaddedPrices;
    private final Jugador jugador;

    public AccionesMenu(String jugadorNombre) {
        this.activosInfoService = DependecyContainer.get(ActivosInfoService.class);
        this.executor = DependecyContainer.get(ExecutorService.class);
        this.menuService = DependecyContainer.get(MenuService.class);
        this.jugador = DependecyContainer.get(JugadoresService.class).getByNombre(jugadorNombre);
    }

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

        if(precio > this.jugador.getPixelcoins()){
            player.sendMessage(DARK_RED + "No tienes las suficientes pixelcoins");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
            return;
        }

        this.menuService.open(player, new ComprarBolsaConfirmacionMenu(
                ticker, TipoActivo.ACCIONES, player.getName(), precio
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
    public void afterShow() {
        if(this.hasLoaddedPrices) return;

        List<ItemStack> itemsToEdit = getItemsAccionesToEdit();
        Map<String, ActivoInfo> allActivosInfo = this.activosInfoService.findAllToMap();

        this.executor.execute(() -> {
            itemsToEdit.forEach(itemToEdit -> addPriceToAccionItem(allActivosInfo, itemToEdit));
        });

        this.hasLoaddedPrices = true;
    }

    private void addPriceToAccionItem(Map<String, ActivoInfo> allActivosInfo, ItemStack itemToEdit) {
        try {
            String ticker = ItemUtils.getLore(itemToEdit, 0).split(" ")[1];
            double precio = allActivosInfo.get(ticker) == null ?
                    TipoActivo.ACCIONES.getPrecio(ticker) :
                    allActivosInfo.get(ticker).getPrecio();

            ItemUtils.setLore(itemToEdit, 1, GOLD + "Precio: " + GREEN + FORMATEA.format(precio) + " PC");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<ItemStack> getItemsAccionesToEdit() {
        return allPages().stream()
                .map(Page::getInventory)
                .map(Inventory::getContents)
                .flatMap(Arrays::stream)
                .filter(item -> item != null && item.getType().equals(Material.BOOK))
                .toList();
    }
}
