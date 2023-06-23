package es.serversurvival.pixelcoins.bolsa._shared.activos.infrastructure;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.ActivoBolsaInformationAPIService;
import lombok.SneakyThrows;
import org.json.simple.JSONObject;

@Service
public final class CriptoMonedasBolsaInformationAPIService implements ActivoBolsaInformationAPIService {
    @Override
    @SneakyThrows
    public double getUltimoPrecio(String nombreCorto) {
        JSONObject json = (JSONObject) Funciones.peticionHttp(String.format("https://api.coinbase.com/v2/exchange-rates?currency=%s", nombreCorto));
        JSONObject data = (JSONObject) json.get("data");
        JSONObject rates = (JSONObject) data.get("rates");

        return Double.parseDouble(String.valueOf(rates.get("USD")));
    }

    @Override
    public String getNombreLargo(String nombreCorto) {
        return switch (nombreCorto.toUpperCase()) {
            case "BTC" -> "Bitcoin";
            case "ETH" -> "Etherium";
            case "LTC" -> "Litecoin";
            case "ADA" -> "Cardano";
            case "SOL" -> "Solana";
            case "DOT" -> "Polkadot";
            case "DOGE" -> "Dogecoin";
            case "XMR" -> "Monero";
            case "USDT" -> "Teher";
            case "BNB" -> "BNB";
            case "XRP" -> "XRP";
            case "MATIC" -> "Polygon";
            case "SHIB" -> "Shiba";
            default -> throw new IllegalStateException("Criptomoneda " + nombreCorto + " no soportada");
        };
    }
}
