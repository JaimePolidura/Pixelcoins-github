package es.serversurvival.mySQL.tablasObjetos;

public final class PosicionCerrada implements TablaObjeto {
    private final int id;
    private final String jugador;
    private final String tipo_activo;
    private final String nombre_activo;
    private final int cantidad;
    private final double precio_apertura;
    private final String fecha_apertura;
    private final double precio_cierre;
    private final String fecha_cierre;
    private final Double rentabilidad;
    private final String simbolo;
    private final String tipo_posicion;

    public PosicionCerrada(int id, String jugador, String tipo_activo, String nombre_activo, int cantidad,
                           double precio_apertura, String fecha_apertura, double precio_cierre,
                           String fecha_cierre, Double rentabilidad, String simbolo, String tipo_posicion) {
        this.id = id;
        this.jugador = jugador;
        this.tipo_activo = tipo_activo;
        this.nombre_activo = nombre_activo;
        this.cantidad = cantidad;
        this.precio_apertura = precio_apertura;
        this.fecha_apertura = fecha_apertura;
        this.precio_cierre = precio_cierre;
        this.fecha_cierre = fecha_cierre;
        this.rentabilidad = rentabilidad;
        this.simbolo = simbolo;
        this.tipo_posicion = tipo_posicion;
    }

    public int getId() {
        return id;
    }

    public String getJugador() {
        return jugador;
    }

    public String getTipo_activo() {
        return tipo_activo;
    }

    public String getNombre_activo() {
        return nombre_activo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecio_apertura() {
        return precio_apertura;
    }

    public String getFecha_apertura() {
        return fecha_apertura;
    }

    public double getPrecio_cierre() {
        return precio_cierre;
    }

    public String getFecha_cierre() {
        return fecha_cierre;
    }

    public Double getRentabilidad() {
        return rentabilidad;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public String getTipo_posicion() {
        return tipo_posicion;
    }

    public String getRentabilidadString(){
        return String.valueOf((double) rentabilidad);
    }

    public boolean esSimilar (PosicionCerrada posicionAComparar) {
        return posicionAComparar.rentabilidad.equals(rentabilidad) && posicionAComparar.getNombre_activo().equalsIgnoreCase(nombre_activo) && jugador.equalsIgnoreCase(posicionAComparar.getJugador());
    }
}
