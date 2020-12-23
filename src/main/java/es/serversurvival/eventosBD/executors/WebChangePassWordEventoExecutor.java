package es.serversurvival.eventosBD.executors;

import es.serversurvival.mySQL.VerificacionCuentas;
import es.serversurvival.mySQL.enums.TipoEvento;
import es.serversurvival.mySQL.tablasObjetos.Evento;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class WebChangePassWordEventoExecutor extends EventoExecuter {
    @Override
    public TipoEvento getTipoEvento() {
        return TipoEvento.CHANGE_PASSWORD;
    }

    /**
     *  Codigo para el mensaje del evento: nombreJugador-numero
     */
    @Override
    public void execute(Evento evento) {
        String[] splittedEvento = evento.getMensaje().split("-");
        String nombreJugador = splittedEvento[0];
        int numero = Integer.parseInt(splittedEvento[1]);

        Player player = Bukkit.getPlayer(nombreJugador);
        if(player != null){
            player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "El numero es: " + ChatColor.AQUA + "" + ChatColor.BOLD + "" + numero);
            VerificacionCuentas.INSTANCE.nuevaVerificacionCuenta(nombreJugador, numero);
        }
    }
}
