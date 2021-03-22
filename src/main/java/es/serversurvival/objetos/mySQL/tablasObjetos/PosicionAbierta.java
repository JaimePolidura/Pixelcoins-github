package es.serversurvival.objetos.mySQL.tablasObjetos;

public class PosicionAbierta {
    private int id;
    private String jugador;
    private String tipo;
    private String nombre;
    private int cantidad;
    private double precioApertura;
    private String fechaApertura;

    public PosicionAbierta(int id, String jugador, String tipo, String nombre,
                           int cantidad, double precioApertura, String fechaApertura) {
        this.id = id;
        this.jugador = jugador;
        this.tipo = tipo;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precioApertura = precioApertura;
        this.fechaApertura = fechaApertura;
    }

    public int getId() {
        return id;
    }

    public String getJugador() {
        return jugador;
    }

    public String getTipo() {
        return tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecioApertura() {
        return precioApertura;
    }

    public String getFechaApertura() {
        return fechaApertura;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}