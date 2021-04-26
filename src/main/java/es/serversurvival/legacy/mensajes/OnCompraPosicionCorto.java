package es.serversurvival.legacy.mensajes;

import es.serversurvival.legacy.mySQL.eventos.bolsa.PosicionCompraCortoEvento;
import es.serversurvival.nfs.utils.Funciones;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

import static es.serversurvival.nfs.utils.Funciones.enviarMensajeYSonido;
import static es.serversurvival.nfs.utils.Funciones.redondeoDecimales;
import static org.bukkit.ChatColor.*;

public final class OnCompraPosicionCorto {
    private final DecimalFormat formatea = Funciones.FORMATEA;

    public void onCompraPosicionCorto (PosicionCompraCortoEvento e) {
        String mensaje;
        double revalorizacionTotal =  (e.getPrecioApertura() - e.getPrecioCierre()) * e.getCantidad();

        if (e.getRentabilidad() <= 0)
            mensaje = GOLD + "Has comprado en corto" + formatea.format(e.getCantidad()) + " de " + e.getTicker() + " a " + GREEN + formatea.format(e.getPrecioCierre())
                    + " PC/Accion " + GOLD + " cuando la vendiste a " + GREEN + formatea.format(e.getPrecioCierre()) + " PC/Unidad " + GOLD + " -> " +
                    RED + formatea.format(e.getRentabilidad()) + "% : " + formatea.format(redondeoDecimales(revalorizacionTotal, 3)) + " Perdidas PC ";
        else
            mensaje = GOLD + "Has comprado en corto" + formatea.format(e.getCantidad()) + " de " + e.getTicker() + " a " + GREEN + formatea.format(e.getPrecioCierre())
                    + " PC/Accion " + GOLD + " cuando la vendiste a " + GREEN + formatea.format(e.getPrecioApertura()) + " PC/Unidad " + GOLD + " -> " +
                    GREEN + formatea.format(e.getRentabilidad()) + "% : " + formatea.format(redondeoDecimales(revalorizacionTotal, 3)) + " Beneficios PC ";

        Player player = Bukkit.getPlayer(e.getVendedor());

        enviarMensajeYSonido(player, mensaje, Sound.ENTITY_PLAYER_LEVELUP);

        Bukkit.broadcastMessage(GOLD + player.getName() + " ha alacanzado una rentabilidad del " + GREEN + "+" + formatea.format(redondeoDecimales(e.getRentabilidad(), 3)) + "% "
                + GOLD + "de las acciones de " + e.getNombreValor() + " (" + e.getTicker() + "), poniendose en " + BOLD  + "CORTO");

    }
}
