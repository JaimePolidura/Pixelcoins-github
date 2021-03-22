package es.serversurvival.objetos.mySQL.tablasObjetos;

public class PosicionCerrada {
    private int id;
    private String jugador;
    private String tipo;
    private String nombre;
    private int cantidad;
    private double precioApertura;
    private String fechaApertura;
    private double precioCierre;
    private String fechaCierre;

    public PosicionCerrada(int id, String jugador, String tipo, String nombre, int cantidad,
                           double precioApertura, String fechaApertura, double precioCierre,
                           String fechaCierre, double rentabilidad) {
        this.id = id;
        this.jugador = jugador;
        this.tipo = tipo;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precioApertura = precioApertura;
        this.fechaApertura = fechaApertura;
        this.precioCierre = precioCierre;
        this.fechaCierre = fechaCierre;
        this.rentabilidad = rentabilidad;
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

    public double getPrecioCierre() {
        return precioCierre;
    }

    public String getFechaCierre() {
        return fechaCierre;
    }

    public double getRentabilidad() {
        return rentabilidad;
    }

    private double rentabilidad;
}
