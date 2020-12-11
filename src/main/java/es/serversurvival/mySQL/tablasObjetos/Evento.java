package es.serversurvival.mySQL.tablasObjetos;

import es.serversurvival.mySQL.enums.TipoEvento;

public class Evento implements TablaObjeto{
    private final int id;
    private final String mensaje;
    private final TipoEvento tipo_evento;

    public Evento(int id, String mensaje, String evento) {
        this.id = id;
        this.mensaje = mensaje;
        this.tipo_evento = TipoEvento.valueOf(evento);
    }

    public int getId() {
        return id;
    }

    public String getMensaje() {
        return mensaje;
    }

    public TipoEvento getTipoEvento() {
        return tipo_evento;
    }
}
