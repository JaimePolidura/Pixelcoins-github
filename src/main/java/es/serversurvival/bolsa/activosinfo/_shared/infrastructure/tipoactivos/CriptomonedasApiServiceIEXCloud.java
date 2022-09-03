package es.serversurvival.bolsa.activosinfo._shared.infrastructure.tipoactivos;

import es.dependencyinjector.annotations.Service;
import es.serversurvival._shared.utils.apiHttp.IEXCloud_API;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.criptomonedas.CriptomonedasApiService;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.criptomonedas.SupportedCriptomonedas;
import org.json.simple.JSONObject;

import static es.serversurvival._shared.utils.Funciones.*;

@Service
public final class CriptomonedasApiServiceIEXCloud extends CriptomonedasApiService {
    @Override
    public synchronized Double getPrecio(String nombreActivo) throws Exception {
        JSONObject json = (JSONObject) peticionHttp("https://sandbox.iexapis.com/stable/crypto/" + nombreActivo + "/price?token=" + IEXCloud_API.TOKEN);

        return Double.parseDouble(String.valueOf(json.get("price")));
    }

    @Override
    public String getNombreActivoLargo(String nombreActivo) {
        return SupportedCriptomonedas.getNombreActivoLargo(nombreActivo);
    }
}
