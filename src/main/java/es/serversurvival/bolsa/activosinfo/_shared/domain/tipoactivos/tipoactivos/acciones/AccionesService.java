package es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.tipoactivos.acciones;

import es.serversurvival._shared.utils.apiHttp.IEXCloud_API;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.Dividendos;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.Split;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.TipoActivoService;
import io.vavr.control.Try;
import org.json.simple.JSONObject;

import static es.serversurvival._shared.utils.Funciones.*;

public final class AccionesService extends TipoActivoService implements Split, Dividendos {
    @Override
    public double getPrecio(String nombreActivo) {
        Try<Object> priceResponseAttempt = Try.of(() -> {
            return peticionHttp("https://sandbox.iexapis.com/stable/stock/" + nombreActivo + "/price?token=" + IEXCloud_API.token);
        });

        return priceResponseAttempt.isSuccess() ? Double.parseDouble(String.valueOf(priceResponseAttempt.get())) : -1;
    }

    @Override
    public String getNombreActivoLargo(String nombreActivo) throws Exception {
        Object response = peticionHttp("https://sandbox.iexapis.com/stable/stock/" + nombreActivo + "/company?token=" + IEXCloud_API.token);
        JSONObject json = (JSONObject) response;
        String nombreValor = String.valueOf(json.get("companyName"));

        nombreValor = quitarCaracteres(nombreValor, '.', ',', '`');
        nombreValor = quitarPalabrasEntreEspacios(nombreValor, "group", "inc", "co", "corp", "holdings", "ltd", "adr");

        return nombreValor;
    }

    @Override
    public Object getDividendosData(String nombreActivo) {
        Try<Object> dividendReponse = Try.of(() -> {
            return peticionHttp("https://sandbox.iexapis.com/stable/time-series/advanced_dividends/" + nombreActivo + "?range=next-week&token=" + IEXCloud_API.token);
        });

        return dividendReponse.isSuccess() ? dividendReponse.get() : null;
    }

    @Override
    public Object getSplitData(String nombreActivo) {
        Try<Object> dividendoAttempt = Try.of(() -> {
            return peticionHttp("https://sandbox.iexapis.com/stable/stock/" + nombreActivo + "/splits/1m?token=" + IEXCloud_API.token);
        });

        return dividendoAttempt.isSuccess() ? dividendoAttempt.get() : null;
    }
}
