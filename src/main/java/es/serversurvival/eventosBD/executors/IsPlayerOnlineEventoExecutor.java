package es.serversurvival.eventosBD.executors;

import es.serversurvival.mySQL.enums.TipoEvento;
import es.serversurvival.mySQL.tablasObjetos.Evento;
import org.bukkit.Bukkit;

public class IsPlayerOnlineEventoExecutor extends EventoExecuter {
    @Override
    public TipoEvento getTipoEvento() {
        return TipoEvento.IS_PLAYER_ONLINE;
    }

    @Override
    public void execute(Evento evento) {
        String jugador = evento.getMensaje();

        eventosMySQL.nuevoEvento(jugador + "-" + Bukkit.getPlayer(jugador).isOnline(), TipoEvento.IS_PLAYER_ONLINE);
    }
}
