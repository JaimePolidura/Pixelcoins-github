package es.serversurvival.bolsa.activosinfo.vervalores.materiasprimas;

import es.jaimetruman.ItemBuilder;
import es.jaimetruman.ItemUtils;
import es.jaimetruman.menus.Menu;
import es.jaimetruman.menus.MenuService;
import es.jaimetruman.menus.Page;
import es.jaimetruman.menus.configuration.MenuConfiguration;
import es.jaimetruman.menus.menustate.AfterShow;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.SupportedTipoActivo;
import es.serversurvival.bolsa.activosinfo.vervalores.ComprarBolsaConfirmacionMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;

import static es.serversurvival._shared.utils.Funciones.FORMATEA;
import static es.serversurvival.bolsa.activosinfo.vervalores.materiasprimas.TodasMateriasPrimasVerValores.*;
import static org.bukkit.ChatColor.*;

public final class MateriasPrimasMenu extends Menu implements AfterShow {
    private final Executor executor;
    private final MenuService menuService;

    public MateriasPrimasMenu() {
        this.executor = DependecyContainer.get(Executor.class);
        this.menuService = DependecyContainer.get(MenuService.class);
    }

    @Override
    public int[][] items() {
        return new int[][]{{1, 2, 0, 0, 0}};
    }

    @Override
    public MenuConfiguration configuration() {
        return MenuConfiguration.builder()
                .title(DARK_RED + "" + BOLD + "   Escoge para invertir")
                .fixedItems()
                .item(1, itemInfo())
                .items(2, itemsCriptomonedas(), this::onMateriaPrimaItemClick)
                .build();
    }

    private void onMateriaPrimaItemClick(Player player, InventoryClickEvent event) {
        if(hasLoaded(event.getCurrentItem())) return;

        ItemStack itemClicked = event.getCurrentItem();
        double precio = Double.parseDouble(ItemUtils.getLore(itemClicked, 1).split(" ")[1]);
        String nombreActivo = ItemUtils.getLore(itemClicked, 0).split(" ")[1];

        this.menuService.open(player, new ComprarBolsaConfirmacionMenu(
                nombreActivo, SupportedTipoActivo.MATERIAS_PRIMAS, player.getName(), precio
        ));
    }

    private boolean hasLoaded(ItemStack itemStack){
        return itemStack.getItemMeta().getLore().stream()
                .noneMatch(lore -> lore.contains("Cargando"));
    }

    private List<ItemStack> itemsCriptomonedas() {
        List<ItemStack> toReturn = new ArrayList<>(MATERIAS_PRIMAS.size());

        for(var entry: MATERIAS_PRIMAS.entrySet()){
            String nombreActivoLargo = entry.getValue();

            List<String> lore = List.of(
                    GOLD + "Simbolo: " + entry.getKey(),
                    RED + "Cargando..."
            );

            toReturn.add(ItemBuilder.of(Material.COAL)
                    .title(GOLD + "" + BOLD + nombreActivoLargo)
                    .lore(lore)
                    .build());
        }

        return toReturn;
    }

    private ItemStack itemInfo() {
        return ItemBuilder.of(Material.PAPER)
                .title(AQUA + "" + BOLD + "INFO")
                .lore(List.of(
                        "Para invertir en estas mateiras primass clickea en cualquiera de ellas y elige la cantidad a comprar"
                ))
                .build();
    }

    @Override
    public void afterShow() {
        List<ItemStack> items = this.getItemsAccionesToEdit();

        for (ItemStack item : items) {
            this.executor.execute(() -> {
                addPriceToItem(item);
            });
        }
    }

    private void addPriceToItem(ItemStack item) {
        try {
            String nombreActivo = ItemUtils.getLore(item, 0).split(" ")[1];
            double precio = SupportedTipoActivo.MATERIAS_PRIMAS.getTipoActivoService().getPrecio(nombreActivo);

            ItemUtils.setLore(item, 1, GOLD + "Precio: " + GREEN + FORMATEA.format(precio) + " PC");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<ItemStack> getItemsAccionesToEdit() {
        return getPages().stream()
                .map(Page::getInventory)
                .map(Inventory::getContents)
                .flatMap(Arrays::stream)
                .filter(item -> item != null && item.getType().equals(Material.COAL))
                .toList();
    }
}
