package es.serversurvival.v2.pixelcoins.bolsa.activos.dominio;

public interface ActivoBolsaInformationAPIService {
    double getUltimoPrecio(String nombreCorto);
    String getNombreLargo(String nombreCorto);
}
