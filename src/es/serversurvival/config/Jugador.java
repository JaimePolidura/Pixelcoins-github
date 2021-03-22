package es.serversurvival.config;

public class Jugador {
    int cantidad;
    String nombre;

    Jugador(int cantidadx, String nombrex) {
        nombre = nombrex;
        cantidad = cantidadx;
    }

    public int getCantidad() {
        return cantidad;
    }

    public String getNombre() {
        return nombre;
    }

    public String mostar() {
        return nombre + ": " + cantidad;
    }
}
