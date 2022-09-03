package es.serversurvival.bolsa.activosinfo._shared.infrastructure.tipoactivos;

import es.dependencyinjector.annotations.Service;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.materiasprimas.MateriasPrimasApiService;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.materiasprimas.SupportedMateriasPrimas;

import static es.serversurvival._shared.utils.Funciones.*;
import static es.serversurvival._shared.utils.apiHttp.IEXCloud_API.*;

@Service
public final class MateriasPrimasApiServiceIEXCloud extends MateriasPrimasApiService {
    @Override
    public synchronized Double getPrecio(String nombreActivo) throws Exception {
        return Double.parseDouble(String.valueOf(peticionHttp("https://sandbox.iexapis.com/stable/data-points/market/" + nombreActivo + "?token=" + TOKEN)));
    }

    @Override
    public String getNombreActivoLargo(String nombreActivo) {
        return SupportedMateriasPrimas.getNombreActivoLargo(nombreActivo);
    }
}
