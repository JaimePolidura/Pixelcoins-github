package es.serversurvival.mySQL.tablasObjetos;

public final class Encantamiento implements TablaObjeto {
    private final int id;
    private final String encantamiento;
    private final int nivel;
    private final int id_oferta;

    public Encantamiento(int id_encantamiento, String encantamiento, int nivel, int id_oferta) {
        this.id = id_encantamiento;
        this.encantamiento = encantamiento;
        this.nivel = nivel;
        this.id_oferta = id_oferta;
    }

    public int getId() {
        return id;
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
