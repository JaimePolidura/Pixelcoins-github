package es.serversurvival.eventosBD.executors;

import es.serversurvival.mySQL.Eventos;
import es.serversurvival.mySQL.enums.TipoEvento;
import es.serversurvival.mySQL.tablasObjetos.Evento;

public abstract class EventoExecuter {
    protected final Eventos eventosMySQL = Eventos.INSTANCE;

    public abstract TipoEvento getTipoEvento();
    public abstract void execute (Evento evento);
}
