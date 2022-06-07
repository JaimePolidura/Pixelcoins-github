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
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.SupportedTipoActivo;
import es.serversurvival.bolsa.activosinfo.vervalores.ComprarBolsaConfirmacionMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.Executor;

import static es.serversurvival._shared.utils.Funciones.*;
import static org.bukkit.ChatColor.*;

public final class AccionesMenu extends Menu implements AfterShow {
    private final ActivosInfoService activosInfoService;
    private final MenuService menuService;
    private final Executor executor;
    private boolean hasLoaddedPrices;

    public AccionesMenu() {
        this.activosInfoService = DependecyContainer.get(ActivosInfoService.class);
        this.executor = DependecyContainer.get(Executor.class);
        this.menuService = DependecyContainer.get(MenuService.class);
    }

    @Override
    public int[][] items() {
        return new int[][] {
                {1, 2, 0, 0, 0, 0, 0, 0, 0 },
                {0, 0, 0, 0, 0, 0, 0, 0, 0 },
                {0, 0, 0, 0, 0, 0, 0, 0, 0 },
                {0, 0, 0, 0, 0, 0, 0, 0, 0 },
                {0, 0, 0, 0, 0, 0, 0, 0, 0 },
                {0, 0, 0, 0, 0, 0, 7, 8, 9 }
        };
    }

    @Override
    public MenuConfiguration configuration() {
        return MenuConfiguration.builder()
                .title(DARK_RED + "" + BOLD + "   Escoge para invertir")
                .fixedItems()
                .item(1, itemInfo())
                .items(2, itemsAcciones(), this::onAccoinClicked)
                .breakpoint(7)
                .paginated(PaginationConfiguration.builder()
                        .backward(8, itemBackPage())
                        .forward(9, itemNextPage())
                        .build())
                .build();
    }

    private void onAccoinClicked(Player player, InventoryClickEvent event) {
        if(!hasLoaded(event.getCurrentItem())) return;

        ItemStack itemClicked = event.getCurrentItem();
        String ticker = ItemUtils.getLore(itemClicked, 0);
        double precio = Double.parseDouble(ItemUtils.getLore(itemClicked, 1).split(" ")[1]);

        this.menuService.open(player, new ComprarBolsaConfirmacionMenu(
                ticker, SupportedTipoActivo.ACCIONES, player.getName(), precio
        ));
    }

    private boolean hasLoaded(ItemStack item){
        return item.getItemMeta().getLore().stream()
                .noneMatch(lore -> lore.contains("cargando..."));
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
                .title(BOLD + nombre)
                .lore(List.of(
                        RED + "Ticker: " + ticker,
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
        infolore.add("Para invertir en estas acciones: /bolsa invertir <ticker> <nº acciones>");
        infolore.add("                  ");
        infolore.add("Estas acciones son ejemplos con las que se puede comprar, ");
        infolore.add("si quieres comprar otra accion que no este en la lista adelante");
        infolore.add(" solo que necesitas encontrar el ticker en internet, ");
        infolore.add("se puede encontrar en cualquier pagina como por ejemplo");
        infolore.add("es.investing.com. Hay que aclarar que hay acciones que se puede ");
        infolore.add("y otras no la mayoria que se pueden son americanas.");

        String displayname = AQUA + "" + BOLD + "INFO";

        return ItemBuilder.of(Material.PAPER).title(displayname).lore(infolore).build();
    }

    @Override
    public void afterShow() {
        if(this.hasLoaddedPrices) return;

        List<ItemStack> itemsToEdit = getItemsAccionesToEdit();
        Map<String, ActivoInfo> allActivosInfo = this.activosInfoService.findAllToMap();

        for (ItemStack itemToEdit : itemsToEdit) {
            this.executor.execute(() -> {
                addPriceToAccionItem(allActivosInfo, itemToEdit);
            });
        }

        this.hasLoaddedPrices = true;
    }

    private void addPriceToAccionItem(Map<String, ActivoInfo> allActivosInfo, ItemStack itemToEdit) {
        try {
            String ticker = ItemUtils.getLore(itemToEdit, 0).split(" ")[1];
            double precio = allActivosInfo.get(ticker) == null ?
                    SupportedTipoActivo.ACCIONES.getTipoActivoService().getPrecio(ticker) :
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
