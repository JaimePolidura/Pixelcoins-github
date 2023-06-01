package es.serversurvival.v2.pixelcoins.bolsa._shared.activos.dominio;

public interface ActivoBolsaInformationAPIService {
    double getUltimoPrecio(String nombreCorto);
    String getNombreLargo(String nombreCorto);
}
