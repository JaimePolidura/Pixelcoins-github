package es.serversurvival.empresas.empresas.vertodas;

import es.serversurvival.empresas.empresas.solicitarservicio.SolicitarServicioUseCase;
import es.serversurvival.jugadores.perfil.PerfilMenu;
import es.serversurvival._shared.menus.Menu;
import es.serversurvival._shared.menus.inventory.InventoryCreator;
import es.serversurvival._shared.menus.CanGoBack;
import es.serversurvival._shared.menus.Clickable;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.empresas.empresas.miempresa.EmpresasVerMenu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import java.util.*;

import static es.serversurvival._shared.utils.Funciones.enviarMensajeYSonido;

public class EmpresasVerTodasMenu extends Menu implements CanGoBack, Clickable {
    public static final String titulo = ChatColor.DARK_RED + "" + ChatColor.BOLD + "        EMPRESAS";

    private final SolicitarServicioUseCase solicitarServico;
    private final Inventory inventory;
    private final Player player;

    public EmpresasVerTodasMenu(Player player) {
        this.solicitarServico = new SolicitarServicioUseCase();
        this.inventory = InventoryCreator.createInventoryMenu(new EmpresasVerTodasInventoryFactory(), player.getName());
        this.player = player;

        openMenu();
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public String getNameItemGoBack() {
        return Material.RED_WOOL.toString();
    }

    @Override
    public void goBack() {
        PerfilMenu perfilMenu = new PerfilMenu(player);
    }

    @Override
    public void onOherClick(InventoryClickEvent event) {
        ItemStack itemClickeado = event.getCurrentItem();
        if(itemClickeado == null || Funciones.esDeTipoItem(itemClickeado, "AIR")){
            return;
        }
        List<String> lore = itemClickeado.getItemMeta().getLore();
        if(lore == null || lore.get(1) == null){
            return;
        }

        String nombreEmpresa = lore.get(1).split(" ")[1].substring(4);
        String displayName = itemClickeado.getItemMeta().getDisplayName();

        if(displayName.equalsIgnoreCase(ChatColor.GOLD + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "CLICK PARA VER TU EMPRESA")){
            EmpresasVerMenu menu = new EmpresasVerMenu(player, nombreEmpresa);
        }else{
            solicitarServico.solicitar(event.getWhoClicked().getName(), nombreEmpresa);

            enviarMensajeYSonido(player, ChatColor.GOLD + "Has solicitado el servicio", Sound.ENTITY_PLAYER_LEVELUP);

            closeMenu();
        }

    }
}
