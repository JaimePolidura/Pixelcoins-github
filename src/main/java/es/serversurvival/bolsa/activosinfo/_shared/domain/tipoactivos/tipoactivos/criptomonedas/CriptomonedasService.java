package es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.tipoactivos.criptomonedas;

import es.serversurvival._shared.utils.apiHttp.IEXCloud_API;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.TipoActivoService;
import io.vavr.control.Try;
import org.json.simple.JSONObject;

import static es.serversurvival._shared.utils.Funciones.*;

public final class CriptomonedasService extends TipoActivoService {
    @Override
    public double getPrecio(String nombreActivo) {
        Try<JSONObject> getPrecioAttmept = Try.of(() -> {
            return (JSONObject) peticionHttp("https://sandbox.iexapis.com/stable/crypto/" + nombreActivo + "/price?token=" + IEXCloud_API.token);
        });

        return getPrecioAttmept.isSuccess() ?
                Double.parseDouble((String) getPrecioAttmept.get().get("price")) :
                -1;
    }

    @Override
    public String getNombreActivoLargo(String nombreActivo) {
        return SupportedCriptomonedas.getNombreActivoLargo(nombreActivo);
    }
}
