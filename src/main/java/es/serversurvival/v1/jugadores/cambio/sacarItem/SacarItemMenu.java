package es.serversurvival.v1.jugadores.cambio.sacarItem;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.serversurvival.v1._shared.utils.Funciones;
import es.serversurvival.v1.jugadores._shared.domain.Jugador;
import es.serversurvival.v1.jugadores.cambio.TipoCambioPixelcoins;
import es.serversurvival.v1.mensajes._shared.application.EnviadorMensajes;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static es.serversurvival.v1._shared.utils.Funciones.FORMATEA;
import static es.serversurvival.v1.jugadores.cambio.TipoCambioPixelcoins.*;
import static org.bukkit.ChatColor.*;

@AllArgsConstructor
public final class SacarItemMenu extends Menu<Jugador> {
    private final SacarItemUseCase sacarItemUseCase;
    private final EnviadorMensajes enviadorMensajes;

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
                .title(ChatColor.DARK_RED + "" + org.bukkit.ChatColor.BOLD + "   ELIGE ITEM PARA SACAR")
                .fixedItems()
                .item(1, buildItemInfo())
                .item(2, buildItem("DIAMANTE", DIAMANTE, Material.DIAMOND), this::onClick)
                .item(3, buildItem("BLOQUE DE DIAMANTE", DIAMANTE * 9, Material.DIAMOND_BLOCK), this::onClick)
                .item(4, buildItem("LAPISLAZULI", LAPISLAZULI, Material.LAPIS_LAZULI), this::onClick)
                .item(5, buildItem("BLOQUE DE LAPISLAZULI", LAPISLAZULI * 9, Material.LAPIS_BLOCK), this::onClick)
                .item(6, buildItem("CUARZO", CUARZO, Material.QUARTZ_BLOCK), this::onClick)
                .build();
    }

    private void onClick(Player player, InventoryClickEvent event) {
        Jugador jugador = getState();
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
        if(jugador.getPixelcoins() < cambioPixelcoins){
            enviadorMensajes.enviarMensajeYSonido(player, DARK_RED + "Necesitas tener minimo " + cambioPixelcoins + " pixelcoins para convertirlo", Sound.ENTITY_VILLAGER_NO);
            return;
        }

        sacarItemUseCase.sacarItem(jugador, tipoCambio, 1);

        player.getInventory().addItem(new ItemStack(Material.getMaterial(tipoItemClickeado), 1));

        setState(jugador.decrementPixelcoinsBy(cambioPixelcoins));

        enviadorMensajes.enviarMensajeYSonido(player, GOLD + "Has convertido las pixelcoins: " + RED + "-" + FORMATEA.format(cambioPixelcoins) + " PC " + GOLD +
                "Quedan " + GREEN + FORMATEA.format(jugador.getPixelcoins() - cambioPixelcoins) + " PC", Sound.ENTITY_PLAYER_LEVELUP);

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

    public ItemStack buildItem (String itemACambiar, double cambio, Material itemMaterial) {
        String displayName = ChatColor.GOLD + "" + ChatColor.BOLD + "CLICK PARA SACAR UN " + itemACambiar;
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.AQUA + "1 "+itemACambiar+" -> " + ChatColor.GREEN + cambio);
        lore.add("    ");
        lore.add(ChatColor.GOLD +"Tus pixelcoins disponibles: " + ChatColor.GREEN + FORMATEA.format(getState().getPixelcoins()));

        return ItemBuilder.of(itemMaterial).title(displayName).lore(lore).build();
    }
}
