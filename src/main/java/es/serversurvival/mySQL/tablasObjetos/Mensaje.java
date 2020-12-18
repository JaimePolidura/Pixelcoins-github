package es.serversurvival.mySQL.tablasObjetos;

public final class Mensaje implements TablaObjeto {
    private final int id;
    private final String enviador;
    private final String destinatario;
    private final String mensaje;

    public Mensaje(int id, String enviador, String destinatario, String mensaje) {
        this.id = id;
        this.enviador = enviador;
        this.destinatario = destinatario;
        this.mensaje = mensaje;
    }

    public String getEnviador() {
        return enviador;
    }

    public int getId() {
        return id;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public String getMensaje() {
        return mensaje;
    }
}
