package es.serversurvival.v2.pixelcoins.bolsa.activos.infraestroctura;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.v2.pixelcoins.bolsa.activos.dominio.ActivoBolsaInformationAPIService;

@Service
public final class AccionesBolsaInformationAPIService implements ActivoBolsaInformationAPIService {
    @Override
    public double getUltimoPrecio(String nombreCorto) {
        return 0;
    }

    @Override
    public String getNombreLargo(String longName) {
        return null;
    }
}
