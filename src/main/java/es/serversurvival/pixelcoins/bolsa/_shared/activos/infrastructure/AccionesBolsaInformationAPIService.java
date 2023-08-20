package es.serversurvival.pixelcoins.bolsa._shared.activos.infrastructure;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.TipoActivoBolsaService;
import es.serversurvival.pixelcoins.config._shared.application.Configuration;
import es.serversurvival.pixelcoins.config._shared.domain.ConfigurationKey;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.json.simple.JSONObject;

import static es.jaime.javaddd.application.utils.ExceptionUtils.*;
import static es.serversurvival._shared.ConfigurationVariables.*;
import static es.serversurvival._shared.utils.Funciones.*;

@Service
@AllArgsConstructor
public final class AccionesBolsaInformationAPIService implements TipoActivoBolsaService {
    private final Configuration configuration;

    @Override
    public double getUltimoPrecio(String nombreCorto) {
        try{
            String key = configuration.get(ConfigurationKey.FINHUB_API_KEY);

            Object response = peticionHttp(String.format("https://finnhub.io/api/v1/quote?symbol=%s&token=%s", nombreCorto, key));
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
        String key = configuration.get(ConfigurationKey.FINHUB_API_KEY);
        JSONObject json = (JSONObject) peticionHttp(String.format("https://finnhub.io/api/v1/stock/profile2?symbol=%s&token=%s", nombreCorto, key));
        String nombreLargo = (String) json.get("name");

        nombreLargo = quitarCaracteres(nombreLargo, '.', ',');
        nombreLargo = quitarPalabrasEntreEspacios(nombreLargo, "group", "inc", "co", "corp", "holdings", "ltd", "adr");

        return nombreLargo;
    }
}
