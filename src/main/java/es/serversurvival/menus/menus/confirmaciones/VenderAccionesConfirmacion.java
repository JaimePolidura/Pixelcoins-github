package es.serversurvival.menus.menus.confirmaciones;

import es.serversurvival.menus.Menu;
import es.serversurvival.menus.inventoryFactory.InventoryCreator;
import es.serversurvival.menus.menus.BolsaCarteraMenu;
import es.serversurvival.mySQL.MySQL;
import es.serversurvival.mySQL.enums.TipoActivo;
import es.serversurvival.mySQL.tablasObjetos.PosicionAbierta;
import es.serversurvival.mySQL.enums.TipoPosicion;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.*;

import static es.serversurvival.util.Funciones.*;
import static es.serversurvival.util.Funciones.mercadoEstaAbierto;
import static org.bukkit.ChatColor.*;

public class VenderAccionesConfirmacion extends Menu implements Confirmacion{
    private Inventory inventory;
    private Player player;
    private TipoPosicion tipoPosicion;
    private TipoActivo tipoActivo;
    private int id;

    public VenderAccionesConfirmacion (Player player, int id, TipoPosicion tipoPosicion, TipoActivo tipoActivo, List<String> loreItemClicked) {
        this.player = player;
        this.id = id;
        this.tipoPosicion = tipoPosicion;;
        this.tipoActivo = tipoActivo;

        String titulo = DARK_RED + "" + BOLD + "     ¿Quieres vender?";
        String tituloItemVender = GREEN + "" + BOLD + "VENDER";
        String tituloItemCancelar = RED + "" + BOLD + "CANCELAR";
        List<String> loreCancelar = Arrays.asList("");
        List<String> loreVender;

        if(tipoActivo == TipoActivo.ACCIONES_SERVER){
            String cantidadAcciones = loreItemClicked.get(2).split(" ")[1];
            loreVender = Arrays.asList(GOLD + "Se añadiran al mercado de acciones del server, ", GOLD + cantidadAcciones + " acciones. /empresas mercado");

        }else{
            String cantidadAcciones = loreItemClicked.get(5).split(" ")[1];
            String valorTotal = loreItemClicked.get(10).split(" ")[2];
            String beneficios = loreItemClicked.get(8).split(" ")[2];
            String rentabilidad = loreItemClicked.get(9).split(" ")[1];

            if(beneficios.charAt(2) == '+'){
                loreVender = Arrays.asList(GOLD +  "Vender " + cantidadAcciones + " acciones con unos beneficios de " + GREEN + beneficios + " PC", GOLD + "y una rentabilidad del " + GREEN + rentabilidad);
            }else{
                loreVender = Arrays.asList(GOLD +  "Vender " + cantidadAcciones + " acciones con unas perdidas de " + RED + beneficios + " PC", GOLD + " y una rentabilidad del " + RED + rentabilidad);
            }
        }

        this.inventory = InventoryCreator.createSolicitud(titulo, tituloItemVender, loreVender, tituloItemCancelar, loreCancelar);

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
    public void confirmar() {
        MySQL.conectar();

        PosicionAbierta posicionAVender = posicionesAbiertasMySQL.getPosicionAbierta(id);

        if(mercadoEstaAbierto() && tipoPosicion == TipoPosicion.LARGO){
            transaccionesMySQL.venderPosicion(posicionAVender, posicionAVender.getCantidad(), player.getName());

        }else if (mercadoEstaAbierto() && tipoPosicion == TipoPosicion.CORTO) {
            transaccionesMySQL.comprarPosicionCorto(posicionAVender, posicionAVender.getCantidad(), player.getName());

        }else if (mercadoNoEstaAbierto() && tipoPosicion == TipoPosicion.LARGO) {
            ordenesMySQL.abrirOrdenVentaLargo(player, String.valueOf(id), posicionAVender.getCantidad());

        }else if (mercadoNoEstaAbierto() && tipoPosicion == TipoPosicion.CORTO) {
            ordenesMySQL.abrirOrdenCompraCorto(player, String.valueOf(id), posicionAVender.getCantidad());
        }

        closeMenu();
        MySQL.desconectar();
    }

    @Override
    public void cancelar() {
        BolsaCarteraMenu menu = new BolsaCarteraMenu(player);
    }

    @Override
    public void onOtherClick(InventoryClickEvent event) { return; }
}
