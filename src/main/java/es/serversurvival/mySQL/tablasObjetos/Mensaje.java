package es.serversurvival.mySQL.tablasObjetos;

public final class Mensaje implements TablaObjeto {
    private final int id_mensaje;
    private final String enviador;
    private final String destinatario;
    private final String mensaje;

    public Mensaje(int id_mensaje, String enviador, String destinatario, String mensaje) {
        this.id_mensaje = id_mensaje;
        this.enviador = enviador;
        this.destinatario = destinatario;
        this.mensaje = mensaje;
    }

    public String getEnviador() {
        return enviador;
    }

    public int getId_mensaje() {
        return id_mensaje;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public String getMensaje() {
        return mensaje;
    }
}
