package es.serversurvival.mySQL.tablasObjetos;


public final class Deuda  implements TablaObjeto{
    private final int id;
    private final String deudor;
    private final String acredor;
    private final int pixelcoins_restantes;
    private final int tiempo_restante;
    private final int interes;
    private final int couta;
    private final String fecha_ultimapaga;

    public Deuda(int id, String deudor, String acredor, int pixelcoins_restantes, int tiempo_restante, int interes, int couta, String fecha_ultimapaga) {
        this.id = id;
        this.deudor = deudor;
        this.acredor = acredor;
        this.pixelcoins_restantes = pixelcoins_restantes;
        this.tiempo_restante = tiempo_restante;
        this.interes = interes;
        this.couta = couta;
        this.fecha_ultimapaga = fecha_ultimapaga;
    }

    public int getId() {
        return id;
    }

    public String getDeudor() {
        return deudor;
    }

    public String getAcredor() {
        return acredor;
    }

    public int getPixelcoins_restantes() {
        return pixelcoins_restantes;
    }

    public int getTiempo_restante() {
        return tiempo_restante;
    }

    public int getInteres() {
        return interes;
    }

    public int getCouta() {
        return couta;
    }

    public String getFecha_ultimapaga() {
        return fecha_ultimapaga;
    }

    public String getPixelcoinsFormateada(){
        return formatea.format(pixelcoins_restantes);
    }

    public String getCuotaFormateada(){
        return formatea.format(couta);
    }
}
