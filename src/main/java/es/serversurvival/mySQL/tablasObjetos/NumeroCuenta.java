package es.serversurvival.mySQL.tablasObjetos;

public final class NumeroCuenta implements TablaObjeto {
    public final int numero;
    public final String jugador;

    public NumeroCuenta(int numero, String jugador) {
        this.numero = numero;
        this.jugador = jugador;
    }

    public int getNumero() {
        return numero;
    }

    public String getJugador() {
        return jugador;
    }
}