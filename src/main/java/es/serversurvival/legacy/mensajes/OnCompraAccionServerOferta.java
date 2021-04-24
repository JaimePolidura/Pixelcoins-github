package es.serversurvival.legacy.mensajes;

import es.jaime.EventListener;
import es.serversurvival.legacy.mySQL.enums.TipoOfertante;
import es.serversurvival.legacy.mySQL.eventos.empresas.EmpresaServerAccionCompradaEvento;
import es.serversurvival.legacy.util.Funciones;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

import static es.serversurvival.legacy.util.Funciones.*;
import static es.serversurvival.legacy.util.Funciones.diferenciaPorcntual;
import static org.bukkit.ChatColor.*;

public final class OnCompraAccionServerOferta {
    private final DecimalFormat formatea = Funciones.FORMATEA;

    @EventListener
    public void onCompraAccionServer (EmpresaServerAccionCompradaEvento e) {
        Player player = Bukkit.getPlayer(e.getJugador());

        enviarMensajeYSonido(player, GOLD + "Has comprado " + formatea.format(e.getCantidad())  + " acciones a " + GREEN + formatea.format(e.getOferta().getPrecio()) + " PC" + GOLD + " que es un total de " +
                GREEN + formatea.format(e.getPixelcoins()) + " PC " + GOLD + " comandos: " + AQUA + "/bolsa vender /bolsa cartera", Sound.ENTITY_PLAYER_LEVELUP);

        Bukkit.broadcastMessage(GOLD + player.getName() + " ha comprado " + e.getCantidad() + " acciones de la empresa del server: " + e.getEmpresa() + " a " + GREEN + e.getOferta().getPrecio() + "PC");

        if(e.getOferta().getTipo_ofertante() == TipoOfertante.EMPRESA){
            String mensajeOnline = GOLD + player.getName() + " ha comprado " + e.getCantidad() + " acciones de " + e.getEmpresa() + "."+GREEN+" +" + formatea.format(e.getPixelcoins()) + "PC";

            enviarMensaje(e.getEmpresa().getOwner(), mensajeOnline, mensajeOnline);
        }else{
            double beneficiosPerdidas = (e.getOferta().getPrecio() - e.getOferta().getPrecio_apertura()) * e.getCantidad();
            double rentabilidad = redondeoDecimales(diferenciaPorcntual(e.getOferta().getPrecio_apertura(), e.getOferta().getPrecio()), 3);

            String mensajeOnline = beneficiosPerdidas >= 0 ?
                    GOLD + player.getName() + " te ha comprado " + e.getCantidad() + " acciones de " + e.getEmpresa() + " con unos beneficios de " + GREEN + "+" + formatea.format(beneficiosPerdidas) + " PC +" + formatea.format(rentabilidad):
                    GOLD + player.getName() + " te ha comprado " + e.getCantidad() + " acciones de " + e.getEmpresa() + " con unos beneficios de " + RED + formatea.format(beneficiosPerdidas) + " PC " + formatea.format(rentabilidad) ;
            String mensajeOffline = beneficiosPerdidas >= 0 ?
                    player.getName() + " te ha comprado " + e.getCantidad() + " acciones de " + e.getEmpresa() + " con unos beneficios de " + "+" + formatea.format(beneficiosPerdidas) + " PC +" + formatea.format(rentabilidad) :
                    player.getName() + " te ha comprado " + e.getCantidad() + " acciones de " + e.getEmpresa() + " con unos beneficios de " + formatea.format(beneficiosPerdidas) + " PC" + formatea.format(rentabilidad);

            enviarMensaje(e.getOferta().getJugador(), mensajeOnline, mensajeOffline);

        }
    }
}
