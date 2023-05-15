package es.serversurvival.v1.bolsa.activosinfo._shared.infrastructure.tipoactivos;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.v1._shared.utils.apiHttp.IEXCloud_API;
import es.serversurvival.v1.bolsa.activosinfo._shared.domain.tipoactivos.criptomonedas.CriptomonedasApiService;
import es.serversurvival.v1.bolsa.activosinfo._shared.domain.tipoactivos.criptomonedas.SupportedCriptomonedas;
import es.serversurvival.v1._shared.utils.Funciones;
import org.json.simple.JSONObject;

@Service
public final class CriptomonedasApiServiceIEXCloud extends CriptomonedasApiService {
    @Override
    public synchronized Double getPrecio(String nombreActivo) throws Exception {
        JSONObject json = (JSONObject) Funciones.peticionHttp("https://sandbox.iexapis.com/stable/crypto/" + nombreActivo + "/price?token=" + IEXCloud_API.TOKEN);

        return Double.parseDouble(String.valueOf(json.get("price")));
    }

    @Override
    public String getNombreActivoLargo(String nombreActivo) {
        return SupportedCriptomonedas.getNombreActivoLargo(nombreActivo);
    }
}
