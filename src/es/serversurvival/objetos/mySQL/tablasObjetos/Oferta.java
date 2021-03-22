package es.serversurvival.objetos.mySQL.tablasObjetos;

public class Oferta {
    private int id_oferta;
    private String nombre;
    private String objeto;
    private int cantidad;
    private double precio;
    private int durabilidad;

    public Oferta(int id_oferta, String nombre, String objeto,
                  int cantidad, double precio, int durabilidad) {
        this.id_oferta = id_oferta;
        this.nombre = nombre;
        this.objeto = objeto;
        this.cantidad = cantidad;
        this.precio = precio;
        this.durabilidad = durabilidad;
    }

    public int getId_oferta() {
        return id_oferta;
    }

    public String getNombre() {
        return nombre;
    }

    public String getObjeto() {
        return objeto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public int getDurabilidad() {
        return durabilidad;
    }
}