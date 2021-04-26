package es.serversurvival.legacy.menus.menus.confirmaciones;

import es.serversurvival.legacy.menus.Menu;
import es.serversurvival.legacy.mySQL.AllMySQLTablesInstances;
import es.serversurvival.nfs.bolsa.llamadasapi.TipoActivo;
import es.serversurvival.legacy.mySQL.enums.TipoPosicion;
import es.serversurvival.nfs.bolsa.posicionesabiertas.mysql.PosicionAbierta;
import es.serversurvival.nfs.utils.Funciones;
import es.serversurvival.legacy.menus.inventoryFactory.InventoryCreator;
import es.serversurvival.nfs.bolsa.cartera.BolsaCarteraMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.*;

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

        PosicionAbierta posicionAVender = AllMySQLTablesInstances.posicionesAbiertasMySQL.getPosicionAbierta(id);

        if(Funciones.mercadoEstaAbierto() && tipoPosicion == TipoPosicion.LARGO){
            AllMySQLTablesInstances.transaccionesMySQL.venderPosicion(posicionAVender, posicionAVender.getCantidad(), player.getName());

        }else if (Funciones.mercadoEstaAbierto() && tipoPosicion == TipoPosicion.CORTO) {
            AllMySQLTablesInstances.transaccionesMySQL.comprarPosicionCorto(posicionAVender, posicionAVender.getCantidad(), player.getName());

        }else if (Funciones.mercadoNoEstaAbierto() && tipoPosicion == TipoPosicion.LARGO) {
            AllMySQLTablesInstances.ordenesMySQL.abrirOrdenVentaLargo(player, String.valueOf(id), posicionAVender.getCantidad());

        }else if (Funciones.mercadoNoEstaAbierto() && tipoPosicion == TipoPosicion.CORTO) {
            AllMySQLTablesInstances.ordenesMySQL.abrirOrdenCompraCorto(player, String.valueOf(id), posicionAVender.getCantidad());
        }

        closeMenu();
    }

    @Override
    public void cancelar() {
        BolsaCarteraMenu menu = new BolsaCarteraMenu(player);
    }

    @Override
    public void onOtherClick(InventoryClickEvent event) { return; }
}
