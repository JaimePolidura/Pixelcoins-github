package es.serversurvival.v2.minecraftserver.jugadores.cambio.sacarmaxitem;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.serversurvival.v1._shared.utils.Funciones;
import es.serversurvival.v1.jugadores._shared.application.JugadoresService;
import es.serversurvival.v1.jugadores._shared.domain.Jugador;
import es.serversurvival.v1.jugadores.cambio.TipoCambioPixelcoins;
import es.serversurvival.v1.jugadores.cambio.sacarMaxItem.SacarMaxItemUseCase;
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

import static org.bukkit.ChatColor.DARK_RED;

@AllArgsConstructor
public final class SacarMaxItemMenu extends Menu {
    private final SacarMaxItemUseCase sacarMaxItemUseCase;
    private final EnviadorMensajes enviadorMensajes;
    private final JugadoresService jugadoresService;
    private final Jugador jugador;

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
                .item(2, buildItem(this.jugador.getPixelcoins(), "DIAMANTES", Material.DIAMOND_BLOCK, TipoCambioPixelcoins.DIAMANTE), this::onClick)
                .item(3, buildItem(this.jugador.getPixelcoins(), "LAPISLAZULI", Material.LAPIS_BLOCK, TipoCambioPixelcoins.LAPISLAZULI), this::onClick)
                .item(4, buildItem(this.jugador.getPixelcoins(), "CUARZO", Material.QUARTZ_BLOCK, TipoCambioPixelcoins.CUARZO), this::onClick)
                .build();
    }

    private void onClick(Player player, InventoryClickEvent event) {
        ItemStack itemClickeado = event.getCurrentItem();

        String tipoItem = itemClickeado.getType().toString();
        TipoCambioPixelcoins tipoCambio =  TipoCambioPixelcoins.valueOf(tipoItem);
        int espacios = Funciones.getEspaciosOcupados(player.getInventory());
        Jugador jugador = this.jugadoresService.getByNombre(player.getName());

        if(espacios == 36){
            player.sendMessage(ChatColor.DARK_RED + "Tienes el inventario libre");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
            return;
        }
        if (tipoCambio.cambio > jugador.getPixelcoins()) {
            enviadorMensajes.enviarMensajeYSonido(player, DARK_RED + "No tienes las suficientes pixelcoins", Sound.ENTITY_VILLAGER_NO);
            return;
        }

        sacarMaxItemUseCase.sacarMaxItem(jugador, tipoCambio);
    }

    private ItemStack buildItemInfo() {
        List<String> lore = new ArrayList<>() {{
            add("Puedes convertir todas tus pixelcoins");
            add("en el mayor numero posible de diamantes");
            add("cuerzo o lapislazuli");
        }};

        return ItemBuilder.of(Material.PAPER).lore(lore).build();
    }

    private ItemStack buildItem (double pixelcoins, String item, Material material, int cambioPixelcoins) {
        String displayName = ChatColor.GOLD + "" + ChatColor.BOLD + "CLICK PARA SACAR MAXIMO DE " + item;
        List<String> lore = new ArrayList<String>() {{
            add(ChatColor.BLUE + "1 DE "+item+" -> " + ChatColor.GREEN + cambioPixelcoins + " PC");
            add("    ");
            add("Tus pixelcoins disponibles: " + ChatColor.GREEN + Funciones.FORMATEA.format(pixelcoins));
        }};

        return ItemBuilder.of(material).title(displayName).lore(lore).build();
    }
}
