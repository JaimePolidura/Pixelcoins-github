package es.serversurvival.pixelcoins.bolsa._shared.activos;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival._shared.eventospixelcoins.PluginIniciado;
import es.serversurvival.pixelcoins.bolsa._shared.activos.aplicacion.ActivosBolsaService;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.ActivoBolsa;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.TipoActivoBolsa;
import lombok.AllArgsConstructor;

import java.util.List;

@EventHandler
@AllArgsConstructor
public final class OnPluginIniciado {
    private final ActivosBolsaService activosBolsaService;

    @EventListener
    public void on(PluginIniciado pluginIniciado) {
        if(activosBolsaService.findByTipo(TipoActivoBolsa.ACCION).size() > 0){
            return;
        }

        List<ActivoBolsa> activos = List.of(
                accion("ADBE", "Adobe"),
                accion("GOOG", "Google"),
                accion("AMZN", "Amazon"),
                accion("META", "Facebook"),
                accion("AMD", "AMD"),
                accion("INTC", "Intel"),
                accion("AAPL", "Apple"),
                accion("AXP", "American express"),
                accion("BAC", "Bank of Amerca"),
                accion("BRK.B", "Berkshire Hathaway"),
                accion("BA", "Boeing"),
                accion("KO", "Cocacola"),
                accion("PEP", "Pepsi"),
                accion("DBX", "Dropbox"),
                accion("FDX", "FedEx"),
                accion("FL", "FootLocker"),
                accion("F", "Ford"),
                accion("IBM", "IBM"),
                accion("MCD", "MacDonalds"),
                accion("MSFT", "Microsoft"),
                accion("NFLX", "Netflix"),
                accion("ORCL", "Oracle"),
                accion("PYPL", "Paypal"),
                accion("TSLA", "Tesla"),
                accion("YUM", "YUM (Tacobell, KFC, PizzaHut"),
                accion("SPOT", "Spotify"),
                accion("UBER", "Uber"),
                accion("V", "Visa"),
                accion("SAN", "Santander"),
                accion("NVDA", "NVdia"),
                accion("SHOP", "Shopify"),
                accion("NKE", "Nike"),
                accion("QCOM", "Qualcomm"),
                accion("NDAQ", "Nasdaq"),
                accion("MA", "Mastercard"),
                accion("JPM", "JPMorgan"),
                accion("RACE", "Ferrari"),
                accion("TEF", "Telefonica"),
                accion("CSCO", "Cisco"),
                accion("TSM", "Taiwan semiconductors manufacturing"),
                accion("LOGI", "Logitech"),
                accion("CRSR", "Corsair"),
                accion("MCO", "Moodys"),
                accion("WMT", "Waltmart"),
                criptomoneda("BTC", "Bitcoin"),
                criptomoneda("ETH", "Etherium"),
                criptomoneda("LTC", "Litecoin"),
                criptomoneda("ADA", "Cardano"),
                criptomoneda("SOL", "Solana"),
                criptomoneda("DOT", "Polkadot"),
                criptomoneda("DOGE", "Dogecoin"),
                criptomoneda("XMR", "Monero"),
                criptomoneda("USDT", "Tether"),
                criptomoneda("BNB", "BNB"),
                criptomoneda("XRP", "XRP"),
                criptomoneda("MATIC", "Polygon"),
                criptomoneda("SHIB", "Shiba")
        );

        activos.forEach(activosBolsaService::save);
    }

    private ActivoBolsa criptomoneda(String nombreCorto, String nombreLargo) {
        return ActivoBolsa.builder()
                .tipoActivoBolsa(TipoActivoBolsa.CRIPTOMONEDAS)
                .nombreLargo(nombreLargo)
                .nombreCorto(nombreCorto)
                .build();
    }

    private ActivoBolsa accion(String nombreCorto, String nombreLargo) {
        return ActivoBolsa.builder()
                .tipoActivoBolsa(TipoActivoBolsa.ACCION)
                .nombreLargo(nombreLargo)
                .nombreCorto(nombreCorto)
                .build();
    }
}
