package es.serversurvival.mySQL.tablasObjetos;

import es.serversurvival.mySQL.enums.TipoPosicion;
import es.serversurvival.mySQL.enums.TipoValor;


public final class PosicionAbierta implements TablaObjeto {
    private final int id;
    private final String jugador;
    private final String tipo_activo;
    private final String nombre_activo;
    private final int cantidad;
    private final double precio_apertura;
    private final String fecha_apertura;
    public final String tipo_posicion;

    public PosicionAbierta(int id, String jugador, String tipo_activo, String nombre,
                           int cantidad, double precio_apertura, String fecha_apertura, String tipo_posicion) {
        this.id = id;
        this.jugador = jugador;
        this.tipo_activo = tipo_activo;
        this.nombre_activo = nombre;
        this.cantidad = cantidad;
        this.precio_apertura = precio_apertura;
        this.fecha_apertura = fecha_apertura;
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

    public String getTipo_posicion() {
        return tipo_posicion;
    }

    public boolean esLargo () {
        return this.getTipo_posicion().equalsIgnoreCase(TipoPosicion.LARGO.toString());
    }

    public boolean esCorto () {
        return this.getTipo_posicion().equalsIgnoreCase(TipoPosicion.CORTO.toString());
    }

    public boolean esTipoAccion () {
        return this.getTipo_activo().equalsIgnoreCase(TipoValor.ACCIONES.toString());
    }

    public boolean esTipoCriptomoneda () {
        return this.getTipo_activo().equalsIgnoreCase(TipoValor.CRIPTOMONEDAS.toString());
    }

    public boolean esTipoMateriaPrima () {
        return this.getTipo_activo().equalsIgnoreCase(TipoValor.MATERIAS_PRIMAS.toString());
    }

    public boolean esTipoIndice () {
        return this.getTipo_activo().equalsIgnoreCase(TipoValor.INDICES.toString());
    }

}
