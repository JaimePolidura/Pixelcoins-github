package es.serversurvival.mySQL.tablasObjetos;

public final class VerificacionCuenta implements TablaObjeto{
    private final String jugador;
    private final int numero;

    public VerificacionCuenta(String jugador, int numero) {
        this.jugador = jugador;
        this.numero = numero;
    }

    public String getJugador() {
        return jugador;
    }

    public int getNumero() {
        return numero;
    }
}
