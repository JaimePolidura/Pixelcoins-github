package es.serversurvival.pixelcoins.bolsa._shared.activos.infrastructure;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.javaddd.application.utils.ExceptionUtils;
import es.serversurvival._shared.ConcurrencyUtils;
import es.serversurvival._shared.ConfigurationVariables;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.ActivoBolsaInformationAPIService;
import lombok.SneakyThrows;
import org.json.simple.JSONObject;

import java.net.URI;
import java.net.http.HttpRequest;

import static es.jaime.javaddd.application.utils.ExceptionUtils.*;
import static es.serversurvival._shared.ConfigurationVariables.*;
import static es.serversurvival._shared.utils.Funciones.*;

@Service
public final class AccionesBolsaInformationAPIService implements ActivoBolsaInformationAPIService {
    @Override
    public double getUltimoPrecio(String nombreCorto) {
        try{
            Object response = peticionHttp(String.format("https://finnhub.io/api/v1/quote?symbol=%s&token=%s", nombreCorto, FINHUB_API_KEY));
            JSONObject json = (JSONObject) response;

            return Double.parseDouble(String.valueOf(json.get("c")));
        }catch (Exception  e) {
            return rethrowChecked(() -> {
                JSONObject json = (JSONObject) Funciones.peticionHttp("https://api.nasdaq.com/api/quote/%s/info?assetclass=stocks");
                JSONObject data = (JSONObject) json.get("data");
                JSONObject primary = (JSONObject) data.get("primary");

                return Double.parseDouble(primary.get("lastSalePrice").toString().split("\\$")[0]);
            });
        }
    }

    @Override
    @SneakyThrows
    public String getNombreLargo(String nombreCorto) {
        JSONObject json = (JSONObject) peticionHttp(String.format("https://finnhub.io/api/v1/stock/profile2?symbol=%s&token=%s", nombreCorto, FINHUB_API_KEY));
        String nombreLargo = (String) json.get("name");

        nombreLargo = quitarCaracteres(nombreLargo, '.', ',');
        nombreLargo = quitarPalabrasEntreEspacios(nombreLargo, "group", "inc", "co", "corp", "holdings", "ltd", "adr");

        return nombreLargo;
    }
}
