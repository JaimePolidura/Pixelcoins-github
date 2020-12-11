package es.serversurvival.mySQL.tablasObjetos;

import es.serversurvival.mySQL.enums.TipoPosicion;
import es.serversurvival.mySQL.enums.TipoValor;


public final class PosicionAbierta implements TablaObjeto {
    private final int id;
    private final String jugador;
    private final String tipo;
    private final String nombre;
    private final int cantidad;
    private final double precioApertura;
    private final String fechaApertura;
    public final String tipoPosicion;

    public PosicionAbierta(int id, String jugador, String tipo, String nombre,
                           int cantidad, double precioApertura, String fechaApertura, String tipoPosicion) {
        this.id = id;
        this.jugador = jugador;
        this.tipo = tipo;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precioApertura = precioApertura;
        this.fechaApertura = fechaApertura;
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

    public String getTipoPosicion() {
        return tipoPosicion;
    }

    public boolean esLargo () {
        return this.getTipoPosicion().equalsIgnoreCase(TipoPosicion.LARGO.toString());
    }

    public boolean esCorto () {
        return this.getTipoPosicion().equalsIgnoreCase(TipoPosicion.CORTO.toString());
    }

    public boolean esTipoAccion () {
        return this.getTipo().equalsIgnoreCase(TipoValor.ACCIONES.toString());
    }

    public boolean esTipoCriptomoneda () {
        return this.getTipo().equalsIgnoreCase(TipoValor.CRIPTOMONEDAS.toString());
    }

    public boolean esTipoMateriaPrima () {
        return this.getTipo().equalsIgnoreCase(TipoValor.MATERIAS_PRIMAS.toString());
    }

    public boolean esTipoIndice () {
        return this.getTipo().equalsIgnoreCase(TipoValor.INDICES.toString());
    }

}
