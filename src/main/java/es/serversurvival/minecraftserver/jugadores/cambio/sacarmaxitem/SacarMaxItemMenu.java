package es.serversurvival.minecraftserver.jugadores.cambio.sacarmaxitem;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseBus;
import es.serversurvival.pixelcoins.jugadores.cambiar.TipoCambioPixelcoins;
import es.serversurvival.pixelcoins.jugadores.cambiar.sacarMaxItem.SacarMaxItemParametros;
import es.serversurvival.pixelcoins.transacciones.TransaccionesService;
import es.serversurvival._shared.utils.Funciones;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public final class SacarMaxItemMenu extends Menu {
    private final TransaccionesService transaccionesService;
    private final UseCaseBus useCaseBus;

    @Override
    public int[][] items() {
        return new int[][] {
                {0, 0, 0, 0, 1, 0, 0, 0, 0},
                {0, 2, 0, 0, 3, 0, 0, 4, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
    }

    @Override
    public MenuConfiguration configuration() {
        return MenuConfiguration.builder()
                .fixedItems()
                .title(ChatColor.DARK_RED + "" + ChatColor.BOLD + "ELLIGE ITEM PARA SACR MAX")
                .item(4, buildItemInfo())
                .item(2, player -> buildItem(player.getUniqueId(), "DIAMANTES", Material.DIAMOND_BLOCK, TipoCambioPixelcoins.DIAMANTE), this::onClick)
                .item(3, player -> buildItem(player.getUniqueId(), "LAPISLAZULI", Material.LAPIS_BLOCK, TipoCambioPixelcoins.LAPISLAZULI), this::onClick)
                .item(4, player -> buildItem(player.getUniqueId(), "CUARZO", Material.QUARTZ_BLOCK, TipoCambioPixelcoins.CUARZO), this::onClick)
                .build();
    }

    private void onClick(Player player, InventoryClickEvent event) {
        ItemStack itemClickeado = event.getCurrentItem();
        String tipoItem = itemClickeado.getType().toString();
        TipoCambioPixelcoins tipoCambio =  TipoCambioPixelcoins.valueOf(tipoItem);
        int espacios = Funciones.getEspaciosOcupados(player.getInventory());

        if(espacios == 36){
            player.sendMessage(ChatColor.DARK_RED + "Tienes el inventario libre");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
            return;
        }

        useCaseBus.handle(SacarMaxItemParametros.of(player.getUniqueId(), tipoCambio));
        player.closeInventory();
    }

    private ItemStack buildItemInfo() {
        List<String> lore = new ArrayList<>() {{
            add("Puedes convertir todas tus pixelcoins");
            add("en el mayor numero posible de diamantes");
            add("cuerzo o lapislazuli");
        }};

        return ItemBuilder.of(Material.PAPER).lore(lore).build();
    }

    private ItemStack buildItem (UUID jugaodrId, String item, Material material, int cambioPixelcoins) {
        double pixelcoinsJugador = transaccionesService.getBalancePixelcoins(jugaodrId);

        String displayName = ChatColor.GOLD + "" + ChatColor.BOLD + "SACAR MAXIMO DE " + item;
        List<String> lore = new ArrayList<>() {{
            add(ChatColor.BLUE + "1 DE " + item + " -> " + ChatColor.GREEN + cambioPixelcoins + " PC");
            add("    ");
            add("Tus pixelcoins disponibles: " + Funciones.formatPixelcoins(pixelcoinsJugador));
        }};

        return ItemBuilder.of(material).title(displayName).lore(lore).build();
    }
}
