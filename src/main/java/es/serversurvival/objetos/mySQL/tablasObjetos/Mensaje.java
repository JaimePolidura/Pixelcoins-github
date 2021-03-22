package es.serversurvival.objetos.mySQL.tablasObjetos;

public class Mensaje {
    private int id_mensaje;
    private String destinatario;
    private String mensaje;

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