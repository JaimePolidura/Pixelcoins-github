package es.serversurvival.pixelcoins.bolsa._shared.activos.infrastructure;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.TipoActivoBolsaService;
import lombok.SneakyThrows;
import org.json.simple.JSONObject;

import static es.jaime.javaddd.application.utils.ExceptionUtils.*;
import static es.serversurvival._shared.ConfigurationVariables.*;
import static es.serversurvival._shared.utils.Funciones.*;

@Service
public final class AccionesBolsaInformationAPIService implements TipoActivoBolsaService {
    @Override
    public double getUltimoPrecio(String nombreCorto) {
        try{
            Object response = peticionHttp(String.format("https://finnhub.io/api/v1/quote?symbol=%s&token=%s", nombreCorto, FINHUB_API_KEY));
            JSONObject json = (JSONObject) response;
            double precio = Double.parseDouble(String.valueOf(json.get("c")));

            if(precio <= 0){
                throw new RuntimeException(String.format("Activo con el ticker %s no encontrado", nombreCorto));
            }

            return precio;
        }catch (Exception  e) {
            throw new RuntimeException(String.format("Activo con el ticker %s no encontrado", nombreCorto));
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
