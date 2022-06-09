package es.serversurvival.bolsa.activosinfo.vervalores.criptomonedas;

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
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

import static es.serversurvival._shared.utils.Funciones.*;
import static es.serversurvival.bolsa.activosinfo.vervalores.criptomonedas.TodasCriptomodeasVerValores.*;
import static org.bukkit.ChatColor.*;

public final class CriptomonedasMenu extends Menu implements AfterShow {
    private final ExecutorService executor;
    private final MenuService menuService;
    private final Jugador jugador;

    public CriptomonedasMenu(String jugadorNombre) {
        this.executor = DependecyContainer.get(ExecutorService.class);
        this.menuService = DependecyContainer.get(MenuService.class);
        this.jugador = DependecyContainer.get(JugadoresService.class).getByNombre(jugadorNombre);
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
                .items(2, itemsCriptomonedas(), this::onCriptomonedaItemClick)
                .build();
    }

    private void onCriptomonedaItemClick(Player player, InventoryClickEvent event) {
        if(!hasLoaded(event.getCurrentItem())) return;

        ItemStack itemClicked = event.getCurrentItem();
        String precioString = ItemUtils.getLore(itemClicked, 1).split(" ")[1].replace(",", ".");
        double precio = Double.parseDouble(precioString);
        if(precio > this.jugador.getPixelcoins()){
            player.sendMessage(DARK_RED + "No tienes las suficientes pixelcoins");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
            return;
        }

        String nombreActivo = ItemUtils.getLore(itemClicked, 0).split(" ")[1];

        this.menuService.open(player, new ComprarBolsaConfirmacionMenu(
                nombreActivo, SupportedTipoActivo.CRIPTOMONEDAS, player.getName(), precio
        ));
    }

    private boolean hasLoaded(ItemStack itemStack){
        return itemStack.getItemMeta().getLore().stream()
                .noneMatch(lore -> lore.contains("Cargando..."));
    }

    private List<ItemStack> itemsCriptomonedas() {
        List<ItemStack> toReturn = new ArrayList<>(CRIPTOMONEDAS.size());

        for(var entry: CRIPTOMONEDAS.entrySet()){
            String nombreActivoLargo = entry.getValue();

            List<String> lore = List.of(
                    GOLD + "Simbolo: " + entry.getKey(),
                    RED + "Cargando..."
            );

            toReturn.add(ItemBuilder.of(Material.GOLD_BLOCK)
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
                        GOLD + "Para invertir en estas criptomonedas clickea en ",
                        GOLD + "cualquiera de ellas y elige la cantidad a comprar"
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
            double precio = SupportedTipoActivo.CRIPTOMONEDAS.getTipoActivoService().getPrecio(nombreActivo);

            ItemUtils.setLore(item, 1, GOLD + "Precio: " + GREEN + FORMATEA.format(precio) + " PC");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<ItemStack> getItemsAccionesToEdit() {
        return allPages().stream()
                .map(Page::getInventory)
                .map(Inventory::getContents)
                .flatMap(Arrays::stream)
                .filter(item -> item != null && item.getType().equals(Material.GOLD_BLOCK))
                .toList();
    }
}
