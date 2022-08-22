package es.serversurvival.jugadores.cambio.sacarMaxItem;

import es.jaimetruman.ItemBuilder;
import es.jaimetruman.menus.Menu;
import es.jaimetruman.menus.configuration.MenuConfiguration;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;
import es.serversurvival.jugadores.cambio.CambioPixelcoins;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static es.serversurvival._shared.utils.Funciones.FORMATEA;
import static es.serversurvival.jugadores.cambio.CambioPixelcoins.*;
import static org.bukkit.ChatColor.DARK_RED;

public final class SacarMaxItemMenu extends Menu {
    public static final String TITULO = ChatColor.DARK_RED + "" + ChatColor.BOLD + "ELLIGE ITEM PARA SACR MAX";

    private final Jugador jugador;
    private final JugadoresService jugadoresService;

    public SacarMaxItemMenu(String jugadorNombre) {
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
        this.jugador = this.jugadoresService.getByNombre(jugadorNombre);
    }

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
                .title(TITULO)
                .item(4, buildItemInfo())
                .item(2, buildItem(jugador.getPixelcoins(), "DIAMANTES", Material.DIAMOND_BLOCK, DIAMANTE), this::onClick)
                .item(3, buildItem(jugador.getPixelcoins(), "LAPISLAZULI", Material.LAPIS_BLOCK, LAPISLAZULI), this::onClick)
                .item(4, buildItem(jugador.getPixelcoins(), "CUARZO", Material.QUARTZ_BLOCK, CUARZO), this::onClick)
                .build();
    }

    private void onClick(Player player, InventoryClickEvent event) {
        ItemStack itemClickeado = event.getCurrentItem();

        String tipoItem = itemClickeado.getType().toString();
        int espacios = Funciones.getEspaciosOcupados(player.getInventory());
        Jugador jugador = this.jugadoresService.getByNombre(player.getName());

        if(espacios == 36){
            player.sendMessage(ChatColor.DARK_RED + "Tienes el inventario libre");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
            return;
        }
        if (!CambioPixelcoins.suficientesPixelcoins(tipoItem, 1, jugador.getPixelcoins())) {
            Funciones.enviarMensajeYSonido(player, DARK_RED + "No tienes las suficientes pixelcoins", Sound.ENTITY_VILLAGER_NO);
            return;
        }

        String tipoItemClickeado = itemClickeado.getType().toString();

        SacarMaxItemUseCase.INSTANCE.sacarMaxItem(tipoItemClickeado, jugador);
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
            add("Tus pixelcoins disponibles: " + ChatColor.GREEN + FORMATEA.format(pixelcoins));
        }};

        return ItemBuilder.of(material).title(displayName).lore(lore).build();
    }
}
