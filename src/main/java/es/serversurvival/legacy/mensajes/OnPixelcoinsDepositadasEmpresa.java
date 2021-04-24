package es.serversurvival.legacy.mensajes;

import es.jaime.EventListener;
import es.serversurvival.nfs.empresas.depositar.PixelcoinsDepositadasEvento;
import es.serversurvival.legacy.mySQL.tablasObjetos.Empresa;
import es.serversurvival.legacy.util.Funciones;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

import static es.serversurvival.legacy.util.Funciones.enviarMensajeYSonido;
import static org.bukkit.ChatColor.*;

public final class OnPixelcoinsDepositadasEmpresa {
    private final DecimalFormat formatea = Funciones.FORMATEA;

    @EventListener
    public void onPixelcoinsDepositadas (PixelcoinsDepositadasEvento evento) {
        Player player = Bukkit.getPlayer(evento.getJugador().getNombre());
        Empresa empresa = evento.getEmpresa();

        enviarMensajeYSonido(player, GOLD + "Has metido " + GREEN + formatea.format(evento.getPixelcoins()) + " PC" + GOLD + " en tu empresa: " + DARK_AQUA + evento.getEmpresa() +
                GOLD + " ahora tiene: " + GREEN + formatea.format(empresa.getPixelcoins() + evento.getPixelcoins()) + " PC", Sound.ENTITY_PLAYER_LEVELUP);
    }
}
