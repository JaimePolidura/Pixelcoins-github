package es.serversurvival.mySQL.tablasObjetos;

public final class Jugador implements TablaObjeto {
    private final String nombre;
    private final double pixelcoins;
    private final int nventas;
    private final double ingresos;
    private final double gastos;
    private final int ninpagos;
    private final int npagos;
    private final int numero_cuenta;
    private final String uuid;

    public Jugador(String nombre, double pixelcoin, int nventas, double ingresos, double gastos, int ninpagos, int npagos, int numero_cuenta, String uuid) {
        this.nombre = nombre;
        this.pixelcoins = pixelcoin;
        this.numero_cuenta = numero_cuenta;
        this.uuid = uuid;
        this.nventas = nventas;
        this.ingresos = ingresos;
        this.gastos = gastos;
        this.ninpagos = ninpagos;
        this.npagos = npagos;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPixelcoins() {
        return pixelcoins;
    }

    public int getNventas() {
        return nventas;
    }

    public double getIngresos() {
        return ingresos;
    }

    public double getGastos() {
        return gastos;
    }

    public int getNinpagos() {
        return ninpagos;
    }

    public int getNpagos() {
        return npagos;
    }

    public int getNumero_cuenta() {
        return numero_cuenta;
    }

    public String getUuid() {
        return uuid;
    }
}
