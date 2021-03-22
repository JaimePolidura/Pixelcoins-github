package es.serversurvival.objetos.mySQL.tablasObjetos;

public class Cuenta {
    private int id;
    private String jugador;
    private String contra;
    private String email;

    public Cuenta(int id, String jugador, String contra, String email) {
        this.id = id;
        this.jugador = jugador;
        this.contra = contra;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJugador() {
        return jugador;
    }

    public void setJugador(String jugador) {
        this.jugador = jugador;
    }

    public String getContra() {
        return contra;
    }

    public void setContra(String contra) {
        this.contra = contra;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
