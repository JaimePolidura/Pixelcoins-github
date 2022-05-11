package es.serversurvival.bolsa.other._shared.posicionesabiertas.mysql;

import es.serversurvival.bolsa.other._shared.llamadasapi.mysql.TipoActivo;

import java.util.Optional;
import java.util.stream.Stream;

import static es.serversurvival.bolsa.other._shared.llamadasapi.mysql.TipoActivo.CRIPTOMONEDAS;

public enum Criptomonedas {
    BTCUSD("Bitcoin", CRIPTOMONEDAS),
    LTCUSD("Litecoin", CRIPTOMONEDAS),
    ETHUSD("Etherium", CRIPTOMONEDAS);

    public final String nombreValor;
    public final TipoActivo tipoActivo;

    Criptomonedas(String nombreValor, TipoActivo tipoActivo) {
        this.nombreValor = nombreValor;
        this.tipoActivo = tipoActivo;
    }

    public static String getNombreValor (String simbolo) {
        Optional<Criptomonedas> valor = Stream.of(Criptomonedas.values())
                .filter(mat -> mat.nombreValor.equalsIgnoreCase(simbolo))
                .findAny();

        return valor.isPresent() ?
                valor.get().nombreValor :
                simbolo;
    }
}
