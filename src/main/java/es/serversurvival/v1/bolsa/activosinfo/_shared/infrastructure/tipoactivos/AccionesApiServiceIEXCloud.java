package es.serversurvival.v1.bolsa.activosinfo._shared.infrastructure.tipoactivos;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.v1.bolsa.activosinfo._shared.domain.tipoactivos.acciones.AccionesApiService;
import es.serversurvival.v1._shared.utils.Funciones;
import es.serversurvival.v1._shared.utils.apiHttp.IEXCloud_API;
import org.json.simple.JSONObject;

@Service
public class AccionesApiServiceIEXCloud extends AccionesApiService {
    @Override
    public synchronized Double getPrecio(String nombreActivo) throws Exception {
        Thread.sleep(200);

        return Double.parseDouble(String.valueOf(Funciones.peticionHttp("https://sandbox.iexapis.com/stable/stock/" + nombreActivo + "/price?token=" + IEXCloud_API.TOKEN)));
    }

    @Override
    public synchronized String getNombreActivoLargo(String nombreActivo) throws Exception{
        Object response = Funciones.peticionHttp("https://sandbox.iexapis.com/stable/stock/" + nombreActivo + "/company?token=" + IEXCloud_API.TOKEN);
        JSONObject json = (JSONObject) response;
        String nombreValor = String.valueOf(json.get("companyName"));

        nombreValor = Funciones.quitarCaracteres(nombreValor, '.', ',', '`');
        nombreValor = Funciones.quitarPalabrasEntreEspacios(nombreValor, "group", "inc", "co", "corp", "holdings", "ltd", "adr");

        return nombreValor;
    }

    @Override
    public synchronized Object getDividendosData(String nombreActivo) throws Exception {
        return Funciones.peticionHttp("https://sandbox.iexapis.com/stable/time-series/advanced_dividends/" + nombreActivo + "?range=next-week&token=" + IEXCloud_API.TOKEN);
    }

    @Override
    public synchronized Object getSplitData(String nombreActivo) throws Exception {
        return Funciones.peticionHttp("https://sandbox.iexapis.com/stable/stock/" + nombreActivo + "/splits/1m?token=" + IEXCloud_API.TOKEN);
    }
}
