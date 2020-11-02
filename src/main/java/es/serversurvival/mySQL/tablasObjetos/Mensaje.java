package es.serversurvival.mySQL.tablasObjetos;

public final class Mensaje implements TablaObjeto {
    private final int id_mensaje;
    private final String destinatario;
    private final String mensaje;

    public Mensaje(int id_mensaje, String destinatario, String mensaje) {
        this.id_mensaje = id_mensaje;
        this.destinatario = destinatario;
        this.mensaje = mensaje;
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