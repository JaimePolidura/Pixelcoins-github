package es.serversurvival.objetos.mySQL.tablasObjetos;

public class Jugador {
    private String nombre;
    private double pixelcoin;
    private int espacios;
    private int nventas;
    private double ingresos;
    private double gastos;
    private double beneficios;
    private int ninpagos;
    private int npagos;

    public Jugador(String nombre, double pixelcoin, int espacios, int nventas, double ingresos, double gastos, double beneficios, int ninpagos, int npagos) {
        this.nombre = nombre;
        this.pixelcoin = pixelcoin;
        this.espacios = espacios;
        this.nventas = nventas;
        this.ingresos = ingresos;
        this.gastos = gastos;
        this.beneficios = beneficios;
        this.ninpagos = ninpagos;
        this.npagos = npagos;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPixelcoin() {
        return pixelcoin;
    }

    public int getEspacios() {
        return espacios;
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

    public double getBeneficios() {
        return beneficios;
    }

    public int getNinpagos() {
        return ninpagos;
    }

    public int getNpagos() {
        return npagos;
    }
}