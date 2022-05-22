package es.serversurvival.bolsa.activosinfo._shared.infrastructure.tipoactivos;

import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.acciones.AccionesApiService;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.acciones.Dividendos;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.acciones.Split;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.TipoActivoService;
import org.json.simple.JSONObject;

import static es.serversurvival._shared.utils.Funciones.*;
import static es.serversurvival._shared.utils.apiHttp.IEXCloud_API.*;

public class AccionesApiServiceIEXCloud extends AccionesApiService {
    @Override
    public Double getPrecio(String nombreActivo) throws Exception {
        return Double.parseDouble(String.valueOf(peticionHttp("https://sandbox.iexapis.com/stable/stock/" + nombreActivo + "/price?token=" + TOKEN)));
    }

    @Override
    public String getNombreActivoLargo(String nombreActivo) throws Exception{
        Object response = peticionHttp("https://sandbox.iexapis.com/stable/stock/" + nombreActivo + "/company?token=" + TOKEN);
        JSONObject json = (JSONObject) response;
        String nombreValor = String.valueOf(json.get("companyName"));

        nombreValor = quitarCaracteres(nombreValor, '.', ',', '`');
        nombreValor = quitarPalabrasEntreEspacios(nombreValor, "group", "inc", "co", "corp", "holdings", "ltd", "adr");

        return nombreValor;
    }

    @Override
    public Object getDividendosData(String nombreActivo) throws Exception {
        return peticionHttp("https://sandbox.iexapis.com/stable/time-series/advanced_dividends/" + nombreActivo + "?range=next-week&token=" + TOKEN);
    }

    @Override
    public Object getSplitData(String nombreActivo) throws Exception {
        return peticionHttp("https://sandbox.iexapis.com/stable/stock/" + nombreActivo + "/splits/1m?token=" + TOKEN);
    }
}
