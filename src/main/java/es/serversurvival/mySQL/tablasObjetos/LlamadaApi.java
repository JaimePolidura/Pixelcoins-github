package es.serversurvival.mySQL.tablasObjetos;

import es.serversurvival.mySQL.enums.TipoValor;

public final class LlamadaApi implements TablaObjeto {
    private final String simbolo;
    private final double precio;
    private final String tipo_activo;
    private final String nombre_activo;
    
    public LlamadaApi(String simbolo, double precio, String tipo_activo, String nombre_activo) {
        this.simbolo = simbolo;
        this.precio = precio;
        this.tipo_activo = tipo_activo;
        this.nombre_activo = nombre_activo;
    }

    public String getTipo_activo() {
        return tipo_activo;
    }

    public String getNombre_activo() {
        return nombre_activo;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public double getPrecio() {
        return precio;
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
