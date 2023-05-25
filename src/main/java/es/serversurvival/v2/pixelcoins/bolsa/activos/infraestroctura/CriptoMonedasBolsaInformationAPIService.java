package es.serversurvival.v2.pixelcoins.bolsa.activos.infraestroctura;

import es.serversurvival.v2.pixelcoins.bolsa.activos.dominio.ActivoBolsaInformationAPIService;

public final class CriptoMonedasBolsaInformationAPIService implements ActivoBolsaInformationAPIService {
    @Override
    public double getUltimoPrecio(String nombreCorto) {
        return 0;
    }

    @Override
    public String getNombreLargo(String longName) {
        return null;
    }
}
