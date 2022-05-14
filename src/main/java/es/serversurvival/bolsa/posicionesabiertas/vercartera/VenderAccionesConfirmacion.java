package es.serversurvival.bolsa.posicionesabiertas.vercartera;

import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa._shared.application.OrderExecutorProxy;
import es.serversurvival.bolsa.ordenespremarket.abrirorden.AbrirOrdenUseCase;
import es.serversurvival.bolsa.ordenespremarket._shared.domain.TipoAccion;
import es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesAbiertasSerivce;
import es.serversurvival.bolsa.posicionesabiertas._shared.domain.PosicionAbierta;
import es.serversurvival.bolsa.posicionescerradas._shared.domain.TipoPosicion;
import es.serversurvival.bolsa.posicionesabiertas.comprarcorto.ComprarCortoUseCase;
import es.serversurvival.bolsa.posicionesabiertas.venderlargo.VenderLargoUseCase;
import es.serversurvival._shared.menus.Menu;
import es.serversurvival._shared.menus.confirmaciones.Confirmacion;
import es.serversurvival._shared.menus.inventory.InventoryCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.*;

import static es.serversurvival.bolsa.ordenespremarket.abrirorden.AbrirOrdenPremarketCommand.*;
import static org.bukkit.ChatColor.*;

public class VenderAccionesConfirmacion extends Menu implements Confirmacion {
    private final VenderLargoUseCase venderLargoUseCase;
    private final ComprarCortoUseCase comprarCortoUseCase;
    private final AbrirOrdenUseCase abrirOrdenUseCase;
    private final PosicionesAbiertasSerivce posicionesAbiertasSerivce;

    private final Inventory inventory;
    private final Player player;
    private final TipoPosicion tipoPosicion;
    private final UUID id;

    public VenderAccionesConfirmacion (Player player, UUID id, TipoPosicion tipoPosicion, TipoActivo tipoActivo, List<String> loreItemClicked) {
        this.player = player;
        this.posicionesAbiertasSerivce = DependecyContainer.get(PosicionesAbiertasSerivce.class);
        this.id = id;
        this.tipoPosicion = tipoPosicion;;
        this.abrirOrdenUseCase = new AbrirOrdenUseCase();
        this.venderLargoUseCase = new VenderLargoUseCase();
        this.comprarCortoUseCase = new ComprarCortoUseCase();

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
            String beneficios = loreItemClicked.get(8).split(" ")[2];
            String rentabilidad = loreItemClicked.get(9).split(" ")[1];
            loreVender = (beneficios.charAt(2) == '+') ?
                    Arrays.asList(GOLD + "Vender " + cantidadAcciones + " acciones con unos beneficios de " + GREEN + beneficios + " PC", GOLD + "y una rentabilidad del " + GREEN + rentabilidad) :
                    Arrays.asList(GOLD + "Vender " + cantidadAcciones + " acciones con unas perdidas de " + RED + beneficios + " PC", GOLD + " y una rentabilidad del " + RED + rentabilidad);
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
        closeMenu();

        PosicionAbierta posicion = posicionesAbiertasSerivce.getById(id);

        OrderExecutorProxy.execute(of(
                    player.getName(),
                    posicion.getNombreActivo(),
                    posicion.getCantidad(),
                    tipoPosicion == TipoPosicion.LARGO ? TipoAccion.LARGO_VENTA : TipoAccion.CORTO_COMPRA,
                    posicion.getPosicionAbiertaId()
        ), () -> {
            if(tipoPosicion == TipoPosicion.LARGO)
                venderLargoUseCase.venderPosicion(posicion, posicion.getCantidad(), player.getName());
            else
                comprarCortoUseCase.comprarPosicionCorto(posicion.getPosicionAbiertaId(), posicion.getCantidad(), posicion.getJugador());
        }
        );
    }

    @Override
    public void cancelar() {
        BolsaCarteraMenu menu = new BolsaCarteraMenu(player);
    }

    @Override
    public void onOtherClick(InventoryClickEvent event) { return; }
}
