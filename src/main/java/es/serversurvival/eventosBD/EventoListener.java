package es.serversurvival.eventosBD;

import es.serversurvival.eventosBD.executors.EventoExecuter;
import es.serversurvival.eventosBD.executors.WebChangePassWordEventoExecutor;
import es.serversurvival.mySQL.Eventos;
import es.serversurvival.mySQL.MySQL;
import es.serversurvival.mySQL.enums.TipoEvento;
import es.serversurvival.mySQL.tablasObjetos.Evento;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventoListener {
    private Map<TipoEvento, EventoExecuter> tipoEventosDisponibles;
    private Eventos eventosMySQL = Eventos.INSTANCE;

    public EventoListener () {
        this.tipoEventosDisponibles = new HashMap<>();
        rellenarEventosDisponibles();
    }

    public synchronized void searchForEventsAndExecute () {
        MySQL.conectar();

        List<Evento> nuevosEventos = eventosMySQL.getAllEventos();

        nuevosEventos.forEach(evento -> {
            this.ejecutarEvento(evento);
            eventosMySQL.borrarEvento(evento.getId());
        });
    }

    private void ejecutarEvento(Evento evento) {
        this.tipoEventosDisponibles.get(evento.getTipoEvento()).execute(evento);
    }

    private void rellenarEventosDisponibles() {
        this.tipoEventosDisponibles.put(TipoEvento.CHANGE_PASSWORD, new WebChangePassWordEventoExecutor());
    }
}
