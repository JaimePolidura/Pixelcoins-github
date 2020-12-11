package es.serversurvival.mySQL.tablasObjetos;

import es.serversurvival.mySQL.enums.TipoValor;

public final class LlamadaApi implements TablaObjeto {
    private final String simbolo;
    private final double precio;
    private final String tipo;
    private final String nombreValor;

    public LlamadaApi(String simbolo, double precio, String tipo, String nombreActivo) {
        this.simbolo = simbolo;
        this.precio = precio;
        this.tipo = tipo;
        this.nombreValor = nombreActivo;
    }

    public String getTipo() {
        return tipo;
    }

    public String getNombreValor() {
        return nombreValor;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public double getPrecio() {
        return precio;
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
