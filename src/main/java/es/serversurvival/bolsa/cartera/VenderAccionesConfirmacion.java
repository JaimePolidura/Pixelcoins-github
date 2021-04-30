package es.serversurvival.bolsa.cartera;

import es.serversurvival.bolsa.llamadasapi.mysql.TipoActivo;
import es.serversurvival.bolsa.ordenespremarket.abrirorden.AbrirOrdenUseCase;
import es.serversurvival.bolsa.ordenespremarket.mysql.AccionOrden;
import es.serversurvival.bolsa.posicionesabiertas.comprarcorto.ComprarCortoUseCase;
import es.serversurvival.bolsa.posicionesabiertas.comprarlargo.ComprarLargoUseCase;
import es.serversurvival.bolsa.posicionesabiertas.mysql.PosicionAbierta;
import es.serversurvival.bolsa.posicionesabiertas.vendercorto.VenderCortoUseCase;
import es.serversurvival.bolsa.posicionesabiertas.venderlargo.VenderLargoUseCase;
import es.serversurvival.bolsa.posicionescerradas.mysql.TipoPosicion;
import es.serversurvival.shared.menus.Menu;
import es.serversurvival.shared.menus.confirmaciones.Confirmacion;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.utils.Funciones;
import es.serversurvival.shared.menus.inventory.InventoryCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.*;

import static org.bukkit.ChatColor.*;

public class VenderAccionesConfirmacion extends Menu implements Confirmacion {
    private final VenderLargoUseCase venderLargoUseCase = VenderLargoUseCase.INSTANCE;
    private final ComprarCortoUseCase comprarCortoUseCase = ComprarCortoUseCase.INSTANCE;
    private final AbrirOrdenUseCase abrirOrdenUseCase = AbrirOrdenUseCase.INSTANCE;

    private final Inventory inventory;
    private final Player player;
    private final TipoPosicion tipoPosicion;
    private final TipoActivo tipoActivo;
    private final int id;

    public VenderAccionesConfirmacion (Player player, int id, TipoPosicion tipoPosicion, TipoActivo tipoActivo, List<String> loreItemClicked) {
        this.player = player;
        this.id = id;
        this.tipoPosicion = tipoPosicion;;
        this.tipoActivo = tipoActivo;

        String titulo = DARK_RED + "" + BOLD + "     ¿Quieres vender?";
        String tituloItemVender = GREEN + "" + BOLD + "VENDER";
        String tituloItemCancelar = RED + "" + BOLD + "CANCELAR";
        List<String> loreCancelar = Collections.singletonList("");
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
        PosicionAbierta posicion = AllMySQLTablesInstances.posicionesAbiertasMySQL.getPosicionAbierta(id);

        if(Funciones.mercadoEstaAbierto() && tipoPosicion == TipoPosicion.LARGO){
            venderLargoUseCase.venderPosicion(posicion, posicion.getCantidad(), player.getName());

        }else if (Funciones.mercadoEstaAbierto() && tipoPosicion == TipoPosicion.CORTO) {
            comprarCortoUseCase.comprarPosicionCorto(posicion, posicion.getCantidad(), player.getName());

        }else if (Funciones.mercadoNoEstaAbierto() && tipoPosicion == TipoPosicion.LARGO) {
            abrirOrdenUseCase.abrirOrden(player.getName(), posicion.getNombre_activo(), posicion.getCantidad(), AccionOrden.LARGO_VENTA);

        }else if (Funciones.mercadoNoEstaAbierto() && tipoPosicion == TipoPosicion.CORTO) {
            abrirOrdenUseCase.abrirOrden(player.getName(), posicion.getNombre_activo(), posicion.getCantidad(), AccionOrden.CORTO_COMPRA);
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
