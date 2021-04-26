package es.serversurvival.legacy.mensajes;

import es.jaime.EventListener;
import es.serversurvival.legacy.mySQL.eventos.bolsa.PosicionCompraLargoEvento;
import es.serversurvival.nfs.utils.Funciones;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

import java.text.DecimalFormat;

import static es.serversurvival.nfs.utils.Funciones.enviarMensajeYSonido;
import static org.bukkit.ChatColor.*;

public final class OnPosicionCompraLargo {
    private final DecimalFormat formatea = Funciones.FORMATEA;

    @EventListener
    public void onCompraBolsaLargo (PosicionCompraLargoEvento evento) {

    }
}
