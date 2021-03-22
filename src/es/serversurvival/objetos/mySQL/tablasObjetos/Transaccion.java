package es.serversurvival.objetos.mySQL.tablasObjetos;

public class Transaccion {
    private int id;
    private String fecha;
    private String comprador;
    private String vendedor;
    private int cantidad;
    private String objeto;
    private String tipo;

    public Transaccion(int id, String fecha, String comprador, String vendedor, int cantidad, String objeto, String tipo) {
        this.id = id;
        this.fecha = fecha;
        this.comprador = comprador;
        this.vendedor = vendedor;
        this.cantidad = cantidad;
        this.objeto = objeto;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public String getFecha() {
        return fecha;
    }

    public String getComprador() {
        return comprador;
    }

    public String getVendedor() {
        return vendedor;
    }

    public int getCantidad() {
        return cantidad;
    }

    public String getObjeto() {
        return objeto;
    }

    public String getTipo() {
        return tipo;
    }
}