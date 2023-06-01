package es.serversurvival.v2.pixelcoins.bolsa._shared.activos.infraestroctura;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.v2.pixelcoins.bolsa._shared.activos.dominio.ActivoBolsaInformationAPIService;

@Service
public final class MateriasPrimasBolsaInfomationAPIService implements ActivoBolsaInformationAPIService {
    @Override
    public double getUltimoPrecio(String nombreCorto) {
        return 0;
    }

    @Override
    public String getNombreLargo(String longName) {
        return null;
    }
}
