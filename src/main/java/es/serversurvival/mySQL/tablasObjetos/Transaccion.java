package es.serversurvival.mySQL.tablasObjetos;

public final class Transaccion implements TablaObjeto {
    private final int id;
    private final String fecha;
    private final String comprador;
    private final String vendedor;
    private final int cantidad;
    private final String objeto;
    private final String tipo;

    public Transaccion(int id, String fecha, String comprador, String vendedor,
                       int cantidad, String objeto, String tipo) {
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
