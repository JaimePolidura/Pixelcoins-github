package es.serversurvival.mySQL.tablasObjetos;

public final class JugadorInfo implements TablaObjeto {
    private final String nombre_jugador;
    private final String UUID;

    public JugadorInfo(String nombre_jugador, String UUID) {
        this.nombre_jugador = nombre_jugador;
        this.UUID = UUID;
    }

    public String getNombre_jugador() {
        return nombre_jugador;
    }

    public String getUUID() {
        return UUID;
    }
}