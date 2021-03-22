package es.serversurvival.objetos.mySQL.tablasObjetos;

public class LlamadaApi {
    private String simbolo;
    private double precio;
    private String tipo;

    public LlamadaApi(String simbolo, double precio, String tipo) {
        this.simbolo = simbolo;
        this.precio = precio;
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public double getPrecio() {
        return precio;
    }
}