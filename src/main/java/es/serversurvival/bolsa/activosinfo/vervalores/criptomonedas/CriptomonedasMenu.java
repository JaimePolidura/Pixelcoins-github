package es.serversurvival.bolsa.activosinfo.vervalores.criptomonedas;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.Page;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitbettermenus.menustate.AfterShow;
import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.bukkitclassmapper._shared.utils.ItemUtils;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.TipoActivo;
import es.serversurvival.bolsa.activosinfo.vervalores.ComprarBolsaConfirmacionMenu;
import es.serversurvival.bolsa.posicionesabiertas.comprarlargo.ComprarLargoUseCase;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;

import static es.serversurvival._shared.utils.Funciones.*;
import static es.serversurvival.bolsa.activosinfo.vervalores.criptomonedas.TodasCriptomodeasVerValores.*;
import static org.bukkit.ChatColor.*;

@AllArgsConstructor
public final class CriptomonedasMenu extends Menu implements AfterShow {
    private final ComprarLargoUseCase comprarLargoUseCase;
    private final JugadoresService jugadoresService;
    private final ExecutorService executor;
    private final MenuService menuService;

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
        try{
            if(!hasLoaded(event.getCurrentItem())) return;

            ItemStack itemClicked = event.getCurrentItem();
            String precioString = ItemUtils.getLore(itemClicked, 1).split(" ")[1]
                    .replaceAll("\\.", "")
                    .replace(",", ".");

            double precio = Double.parseDouble(precioString);
            Jugador jugador = this.jugadoresService.getByNombre(player.getName());

            if(precio > jugador.getPixelcoins()){
                player.sendMessage(DARK_RED + "No tienes las suficientes pixelcoins");
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
                return;
            }

            String nombreActivo = ItemUtils.getLore(itemClicked, 0).split(" ")[1];

            this.menuService.open(player, new ComprarBolsaConfirmacionMenu(
                    nombreActivo, TipoActivo.CRIPTOMONEDAS, player.getName(), precio, comprarLargoUseCase, jugadoresService
            ));
        }catch (Exception e) {
            e.printStackTrace();
        }
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
    public void afterShow(Player player) {
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
            double precio = TipoActivo.CRIPTOMONEDAS.getTipoActivoService().getPrecio(nombreActivo);

            ItemUtils.setLore(item, 1, GOLD + "Precio: " + GREEN + FORMATEA.format(precio) + " PC");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<ItemStack> getItemsAccionesToEdit() {
        List<Page> pages = allPages();

        return pages.stream()
                .map(Page::getInventory)
                .map(Inventory::getContents)
                .flatMap(Arrays::stream)
                .filter(item -> item != null && item.getType().equals(Material.GOLD_BLOCK))
                .toList();
    }
}
