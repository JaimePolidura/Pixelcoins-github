package es.serversurvival.v1.bolsa.activosinfo._shared.infrastructure.tipoactivos;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.v1.bolsa.activosinfo._shared.domain.tipoactivos.materiasprimas.MateriasPrimasApiService;
import es.serversurvival.v1.bolsa.activosinfo._shared.domain.tipoactivos.materiasprimas.SupportedMateriasPrimas;
import es.serversurvival.v1._shared.utils.Funciones;
import es.serversurvival.v1._shared.utils.apiHttp.IEXCloud_API;

@Service
public final class MateriasPrimasApiServiceIEXCloud extends MateriasPrimasApiService {
    @Override
    public synchronized Double getPrecio(String nombreActivo) throws Exception {
        return Double.parseDouble(String.valueOf(Funciones.peticionHttp("https://sandbox.iexapis.com/stable/data-points/market/" + nombreActivo + "?token=" + IEXCloud_API.TOKEN)));
    }

    @Override
    public String getNombreActivoLargo(String nombreActivo) {
        return SupportedMateriasPrimas.getNombreActivoLargo(nombreActivo);
    }
}
