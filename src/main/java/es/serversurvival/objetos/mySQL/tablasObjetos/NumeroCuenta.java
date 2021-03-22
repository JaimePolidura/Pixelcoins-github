package es.serversurvival.objetos.mySQL.tablasObjetos;

public class NumeroCuenta {
    public int numero;
    public String jugador;

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