package es.serversurvival.pixelcoins.bolsa._shared.activos.infrastructure;

import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.ActivoBolsaInformationAPIService;

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
