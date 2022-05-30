package es.serversurvival.bolsa.activosinfo.vervalores.criptomonedas;

import java.util.Map;

public final class TodasCriptomodeasVerValores {
    public static final Map<String, String> CRIPTOMONEDAS;

    static {
        CRIPTOMONEDAS = Map.of("BTCUSD", "Bitcoin",
                "ETHUSD", "Ethereum",
                "LTCUSD", "Litecoin");
    }
}
