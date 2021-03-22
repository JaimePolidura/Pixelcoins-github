package es.serversurvival.mySQL.tablasObjetos;

public final class PosicionCerrada implements TablaObjeto {
    private final int id;
    private final String jugador;
    private final String tipo;
    private final String nombre;
    private final int cantidad;
    private final double precioApertura;
    private final String fechaApertura;
    private final double precioCierre;
    private final String fechaCierre;
    private final Double rentabilidad;
    private final String valorNombre;
    private final String tipoPosicion;

    public PosicionCerrada(int id, String jugador, String tipo, String nombre, int cantidad,
                           double precioApertura, String fechaApertura, double precioCierre,
                           String fechaCierre, Double rentabilidad, String valorNombre, String tipoPosicion) {
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
        this.valorNombre = valorNombre;
        this.tipoPosicion = tipoPosicion;
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

    public Double getRentabilidad() {
        return rentabilidad;
    }

    public String getValorNombre() {
        return valorNombre;
    }

    public String getTipoPosicion() {
        return tipoPosicion;
    }

    public String getRentabilidadString(){
        return String.valueOf((double) rentabilidad);
    }

    public boolean esSimilar (PosicionCerrada posicionAComparar) {
        return posicionAComparar.rentabilidad.equals(rentabilidad) && posicionAComparar.getNombre().equalsIgnoreCase(nombre) && jugador.equalsIgnoreCase(posicionAComparar.getJugador());
    }
}
