package es.serversurvival.objetos.mySQL.tablasObjetos;

public class Encantamiento {
    private int id_encantamiento;
    private String encantamiento;
    private int nivel;
    private int id_oferta;

    public Encantamiento(int id_encantamiento, String encantamiento, int nivel, int id_oferta) {
        this.id_encantamiento = id_encantamiento;
        this.encantamiento = encantamiento;
        this.nivel = nivel;
        this.id_oferta = id_oferta;
    }

    public int getId_encantamiento() {
        return id_encantamiento;
    }

    public String getEncantamiento() {
        return encantamiento;
    }

    public int getNivel() {
        return nivel;
    }

    public int getId_oferta() {
        return id_oferta;
    }
}
