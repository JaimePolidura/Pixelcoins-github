package es.serversurvival.mySQL.tablasObjetos;

public final class Oferta implements TablaObjeto {
    private final int id;
    private final String jugador;
    private final String objeto;
    private final int cantidad;
    private final double precio;
    private final int durabilidad;

    public Oferta(int id_oferta, String nombre, String objeto,
                  int cantidad, double precio, int durabilidad) {
        this.id = id_oferta;
        this.jugador = nombre;
        this.objeto = objeto;
        this.cantidad = cantidad;
        this.precio = precio;
        this.durabilidad = durabilidad;
    }

    public int getId() {
        return id;
    }

    public String getJugador() {
        return jugador;
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
