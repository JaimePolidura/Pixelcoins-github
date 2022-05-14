package es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.tipoactivos.materiasprimas;

import es.serversurvival._shared.utils.apiHttp.IEXCloud_API;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.TipoActivoService;
import io.vavr.control.Try;

import static es.serversurvival._shared.utils.Funciones.*;

public final class MateriasPrimasService extends TipoActivoService {
    @Override
    public double getPrecio(String nombreActivo) {
        Try<Object> response = Try.of(() -> {
            return peticionHttp("https://sandbox.iexapis.com/stable/data-points/market/" + nombreActivo + "?token=" + IEXCloud_API.token);
        });

        return response.isSuccess() ? Double.parseDouble(String.valueOf(response)) : -1;
    }

    @Override
    public String getNombreActivoLargo(String nombreActivo) {
        return SupportedMateriasPrimas.getNombreActivoLargo(nombreActivo);
    }
}
