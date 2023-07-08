package es.serversurvival.minecraftserver.retos.verretos;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitbettermenus.modules.pagination.PaginationConfiguration;
import es.bukkitbettermenus.utils.ItemBuilder;
import es.serversurvival.minecraftserver._shared.MinecraftUtils;
import es.serversurvival.minecraftserver._shared.menus.MenuItems;
import es.serversurvival.pixelcoins.retos._shared.retos.application.RetosService;
import es.serversurvival.pixelcoins.retos._shared.retos.domain.ModuloReto;
import es.serversurvival.pixelcoins.retos._shared.retos.domain.Reto;
import es.serversurvival.pixelcoins.retos._shared.retos.domain.recompensas.TipoRecompensa;
import es.serversurvival.pixelcoins.retos._shared.retosadquiridos.application.RetosAdquiridosService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static es.serversurvival._shared.utils.Funciones.*;
import static org.bukkit.ChatColor.*;

@RequiredArgsConstructor
public final class VerRetosMenu extends Menu<VerRetosMenu.State> {
    private final RetosAdquiridosService retosAdquiridosService;
    private final RetosService retosService;
    private final MenuService menuService;

    @Override
    public int[][] items() {
        return new int[][] {
                {1, 2, 0, 0, 0, 0, 0, 0, 0 },
                {0, 0, 0, 0, 0, 0, 0, 0, 0 },
                {0, 0, 0, 0, 0, 0, 7, 8, 9 }
        };
    }

    @Override
    public MenuConfiguration configuration() {
        return MenuConfiguration.builder()
                .fixedItems()
                .title(MenuItems.TITULO_MENU + "Retos: " + getState().modulo.name().toLowerCase())
                .item(1, buildItemInfo())
                .items(2, buildItemsRetos(), this::itemRetoOnClick)
                .breakpoint(7, MenuItems.GO_MENU_BACK, (p, e) -> menuService.open(p, VerRetosModulosMenu.class))
                .paginated(PaginationConfiguration.builder()
                        .forward(9, MenuItems.GO_FORWARD_PAGE)
                        .backward(8, MenuItems.GO_BACKWARD_PAGE)
                        .build())
                .build();
    }

    private void itemRetoOnClick(Player player, InventoryClickEvent event) {
        UUID retoId = MinecraftUtils.getLastLineOfLore(event.getCurrentItem(), 0);
        Reto reto = retosService.getById(retoId);

        if(reto.isTieneHijos() && reto.esIndependiente()){
            menuService.open(player, VerRetosMenu.class, VerRetosMenu.stateOf(retoId, reto.getModulo()));
        }
    }

    private List<ItemStack> buildItemsRetos() {
        return retosService.findByModuloAndRetoPadreId(getState().getModulo(), getState().getRetoPadreId()).stream()
                .map(this::buildItemFromReto)
                .collect(Collectors.toList());
    }

    private ItemStack buildItemFromReto(Reto reto) {
        return ItemBuilder.of(Material.valueOf(reto.getLogotipo()))
                .title(getTituloItemReto(reto))
                .lore(buildLoreReto(reto))
                .build();
    }

    private List<String> buildLoreReto(Reto reto) {
        List<String> lore = new LinkedList<>();
        boolean tieneHijos = reto.isTieneHijos();

        if(tieneHijos && reto.esIndependiente()){
            lore.add(AQUA + "Tiene subretos");
            lore.add(" ");
        }

        lore.addAll(dividirDesc(GOLD + "Descripccion: " + reto.getDescripccion(), 30).stream()
                .map(str -> GOLD + str)
                .collect(Collectors.toList()));

        if(reto.esIndependiente()){
            boolean adquirdo = retosAdquiridosService.estaAdquirido(getPlayer().getUniqueId(), reto.getRetoId());

            lore.add(GOLD + "Estado: " + BOLD + (adquirdo ? GREEN + "✓ ADQUIRIDO ✓" : RED + "Pendiente de hacer"));
            lore.add(GOLD + getRecompensaDisplayString(reto));
        }else{
            List<Reto> retosProgresion = retosService.findByRetoPadreProgresionIdSortByPosicion(reto.getRetoId());

            lore.add(" ");
            for (Reto retoProgresion : retosProgresion) {
                addRetoProgresionToLore(lore, retoProgresion);
            }
        }

        lore.add("  ");
        lore.add(reto.getRetoId().toString());

        return lore;
    }

    private void addRetoProgresionToLore(List<String> lore, Reto retoProgresion) {
        boolean adquirido = retosAdquiridosService.estaAdquirido(getPlayer().getUniqueId(), retoProgresion.getRetoId());
        String retolore = "  - " + ChatColor.stripColor(
                retoProgresion.getFormatoCantidadRequerida().formatear(retoProgresion.getCantidadRequerida())
        );

        if(adquirido){
            lore.add(GREEN + "✓ " + retolore + GREEN + " ✓");
        }else{
            lore.add(RED + retolore + " " + getRecompensaDisplayString(retoProgresion));
        }
    }

    private String getRecompensaDisplayString(Reto reto) {
        if(reto.getTipoRecompensa() == TipoRecompensa.PIXELCOINS){
            return "Recompensa: " + formatPixelcoins(reto.getRecompensaPixelcoins());
        }else if(reto.getTipoRecompensa() == TipoRecompensa.LOOTBOX) {
            return "Recompensa: x" + formatNumero(reto.getNLootboxesRecompensa()) + " lootbox " + reto.getLootboxTierRecompensa().toString().toLowerCase();
        }

        return "";
    }

    private String getTituloItemReto(Reto reto) {
        boolean tieneHijosYnoProgresivo = reto.isTieneHijos() && !reto.esTipoProgresivo();

        return (tieneHijosYnoProgresivo ? MenuItems.CLICKEABLE : MenuItems.TITULO_ITEM) + reto.getNombre();
    }

    private ItemStack buildItemInfo() {
        return ItemBuilder.of(Material.PAPER)
                .title(MenuItems.INFO)
                .lore(List.of())
                .build();
    }

    public static VerRetosMenu.State stateOf(UUID retoPadreId, ModuloReto modulo) {
        return new State(retoPadreId, modulo);
    }

    public static VerRetosMenu.State stateOf(ModuloReto modulo) {
        return new State(NULL_ID, modulo);
    }

    @AllArgsConstructor
    public static class State {
        @Getter private final UUID retoPadreId;
        @Getter private final ModuloReto modulo;
    }
}
