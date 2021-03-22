package es.serversurvival.objetos.mySQL.tablasObjetos;

public class Deuda {
    private int id_deuda;
    private String deudor;
    private String acredor;
    private int pixelcoins;
    private int tiempo;
    private int interes;
    private int couta;
    private String fecha;

    public Deuda(int id_deuda, String deudor, String acredor, int pixelcoins, int tiempo, int interes, int couta, String fecha) {
        this.id_deuda = id_deuda;
        this.deudor = deudor;
        this.acredor = acredor;
        this.pixelcoins = pixelcoins;
        this.tiempo = tiempo;
        this.interes = interes;
        this.couta = couta;
        this.fecha = fecha;
    }

    public int getId_deuda() {
        return id_deuda;
    }

    public String getDeudor() {
        return deudor;
    }

    public String getAcredor() {
        return acredor;
    }

    public int getPixelcoins() {
        return pixelcoins;
    }

    public int getTiempo() {
        return tiempo;
    }

    public int getInteres() {
        return interes;
    }

    public int getCouta() {
        return couta;
    }

    public String getFecha() {
        return fecha;
    }
}