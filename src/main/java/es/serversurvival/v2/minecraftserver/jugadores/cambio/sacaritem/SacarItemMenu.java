package es.serversurvival.v2.minecraftserver.jugadores.cambio.sacaritem;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.serversurvival.v1._shared.utils.Funciones;
import es.serversurvival.v2.minecraftserver._shared.MinecraftUtils;
import es.serversurvival.v2.pixelcoins.jugadores.cambiar.TipoCambioPixelcoins;
import es.serversurvival.v2.pixelcoins.jugadores.cambiar.sacarItem.SacarItemParametros;
import es.serversurvival.v2.pixelcoins.jugadores.cambiar.sacarItem.SacarItemUseCase;
import es.serversurvival.v2.pixelcoins.transacciones.TransaccionesService;
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

import static es.serversurvival.v1._shared.utils.Funciones.FORMATEA;
import static es.serversurvival.v2.pixelcoins.jugadores.cambiar.TipoCambioPixelcoins.*;
import static org.bukkit.ChatColor.*;

@AllArgsConstructor
public final class SacarItemMenu extends Menu {
    private final TransaccionesService transaccionesService;
    private final SacarItemUseCase sacarItemUseCase;

    @Override
    public int[][] items() {
        return new int[][] {
                {0, 0, 0, 0, 1, 0, 0, 0, 0},
                {2, 0, 3, 0, 4, 0, 5, 0, 6},
                {0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
    }

    @Override
    public MenuConfiguration configuration() {
        return MenuConfiguration.builder()
                .title(ChatColor.DARK_RED + "" + ChatColor.BOLD + "   ELIGE ITEM PARA SACAR")
                .fixedItems()
                .item(1, buildItemInfo())
                .item(2, player -> buildItem(player.getUniqueId(), "DIAMANTE", DIAMANTE, Material.DIAMOND), this::onClick)
                .item(3, player -> buildItem(player.getUniqueId(), "BLOQUE DE DIAMANTE", DIAMANTE * 9, Material.DIAMOND_BLOCK), this::onClick)
                .item(4, player -> buildItem(player.getUniqueId(), "LAPISLAZULI", LAPISLAZULI, Material.LAPIS_LAZULI), this::onClick)
                .item(5, player -> buildItem(player.getUniqueId(), "BLOQUE DE LAPISLAZULI", LAPISLAZULI * 9, Material.LAPIS_BLOCK), this::onClick)
                .item(6, player -> buildItem(player.getUniqueId(), "CUARZO", CUARZO, Material.QUARTZ_BLOCK), this::onClick)
                .build();
    }

    private void onClick(Player player, InventoryClickEvent event) {
        double pixelcoinsJugador = transaccionesService.getBalancePixelcions(player.getUniqueId());

        ItemStack itemClickeado = event.getCurrentItem();
        int espacios = Funciones.getEspaciosOcupados(player.getInventory());
        boolean inventarioLleno = espacios == 36 || (Funciones.esDeTipoItem(itemClickeado, "DIAMOND", "DIAMOND_BLOCK", "QUARTZ_BLOCK",
                "LAPIS_LAZULI", "LAPIS_BLOCK") && itemClickeado.getAmount() == 64);

        if(inventarioLleno){
            player.sendMessage(ChatColor.DARK_RED + "Necesitas tener el inventario con espacios libres");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
            return;
        }

        String tipoItemClickeado = itemClickeado.getType().toString();
        TipoCambioPixelcoins tipoCambio = TipoCambioPixelcoins.valueOf(tipoItemClickeado);
        double cambioPixelcoins = tipoCambio.cambio; //Pixelcoins a sacar
        if(pixelcoinsJugador < cambioPixelcoins){
            MinecraftUtils.enviarMensajeYSonido(player, DARK_RED + "Necesitas tener minimo " + cambioPixelcoins + " pixelcoins para convertirlo", Sound.ENTITY_VILLAGER_NO);
            return;
        }

        sacarItemUseCase.sacarItem(SacarItemParametros.of(player.getUniqueId(), tipoCambio, 1));

        player.getInventory().addItem(new ItemStack(Material.getMaterial(tipoItemClickeado), 1));

        MinecraftUtils.enviarMensajeYSonido(player, GOLD + "Has convertido las pixelcoins: " + RED + "-" + FORMATEA.format(cambioPixelcoins) + " PC " + GOLD +
                "Quedan " + GREEN + FORMATEA.format(pixelcoinsJugador - cambioPixelcoins) + " PC", Sound.ENTITY_PLAYER_LEVELUP);

    }

    private ItemStack buildItemInfo() {
        return ItemBuilder.of(Material.PAPER)
                .title(ChatColor.GOLD + "INFO")
                .lore(List.of(
                        "Una vez que tengas pixelcoins",
                        "puedes intercambiarlas por estos",
                        "bloques y viceversa"
                ))
                .build();
    }

    public ItemStack buildItem (UUID jugadorId, String itemACambiar, double cambio, Material itemMaterial) {
        double pilxelcoinsJugaodor = transaccionesService.getBalancePixelcions(jugadorId);

        String displayName = ChatColor.GOLD + "" + ChatColor.BOLD + "CLICK PARA SACAR UN " + itemACambiar;
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.AQUA + "1 "+itemACambiar+" -> " + ChatColor.GREEN + cambio);
        lore.add("   ");
        lore.add("Tus pixelcoins disponibles: " + ChatColor.GREEN + Funciones.FORMATEA.format(pilxelcoinsJugaodor));

        return ItemBuilder.of(itemMaterial).title(displayName).lore(lore).build();
    }
}
