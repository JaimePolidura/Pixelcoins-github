package es.serversurvival.mensajes;

import es.jaime.EventListener;
import es.serversurvival.mySQL.eventos.bolsa.PosicionVentaLargoEvento;
import es.serversurvival.util.Funciones;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

import java.text.DecimalFormat;

import static es.serversurvival.util.Funciones.enviarMensajeYSonido;
import static es.serversurvival.util.Funciones.redondeoDecimales;
import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.GOLD;

public final class OnVentaPosicionLargo {
    private final DecimalFormat formatea = Funciones.FORMATEA;

    @EventListener
    public void onVentaPosicionLargo (PosicionVentaLargoEvento e) {
        String mensajeAEnviarAlJugador;
        if (e.getRentabilidad() <= 0) {
            mensajeAEnviarAlJugador = GOLD + "Has vendido " + formatea.format(e.getCantidad()) + " de " + e.getTicker() + " a " +
                    GREEN + formatea.format(e.getPrecioCierre()) + " PC/Accion " + GOLD + " cuando la compraste a " + GREEN +
                    formatea.format(e.getPrecioApertura()) + " PC/Unidad " + GOLD + " -> " + RED + formatea.format(e.getRentabilidad())
                    + "% : " + formatea.format(redondeoDecimales(e.getResultado(), 3)) + " Perdidas PC " + GOLD + " de "
                    + GREEN + formatea.format(e.getValorTotal()) + " PC";

            Bukkit.broadcastMessage(GOLD + e.getVendedor() + " ha alacanzado una rentabilidad del " + RED +
                    formatea.format(redondeoDecimales(e.getRentabilidad(), 3)) + "% " + GOLD + "de las acciones de "
                    + e.getNombreValor() + " (" + e.getTicker() + ")");
        } else {
            mensajeAEnviarAlJugador = GOLD + "Has vendido " + formatea.format(e.getCantidad()) + " de " + e.getTicker() + " a " + GREEN
                    + formatea.format(e.getPrecioApertura()) + " PC/Accion " + GOLD + " cuando la compraste a " + GREEN +
                    formatea.format(e.getPrecioApertura()) + " PC/Unidad " + GOLD + " -> " + GREEN + formatea.format(e.getRentabilidad()) + "% : "
                    + formatea.format(redondeoDecimales(e.getResultado(), 3)) + " Beneficios PC " + GOLD + " de " + GREEN + formatea.format(e.getValorTotal()) + " PC";

            Bukkit.broadcastMessage(GOLD + e.getVendedor() + " ha alacanzado una rentabilidad del " + GREEN + "+" +
                    formatea.format(redondeoDecimales(e.getRentabilidad(), 3)) + "% " + GOLD +
                    "de las acciones de " + e.getNombreValor() + " (" + e.getTicker() + ")");
        }

        enviarMensajeYSonido(Bukkit.getPlayer(e.getVendedor()), mensajeAEnviarAlJugador, Sound.ENTITY_PLAYER_LEVELUP);

    }
}
