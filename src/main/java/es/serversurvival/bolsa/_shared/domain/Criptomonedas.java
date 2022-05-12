package es.serversurvival.bolsa._shared.domain;

import es.serversurvival.bolsa.other._shared.llamadasapi.mysql.TipoActivo;

import java.util.Optional;
import java.util.stream.Stream;

import static es.serversurvival.bolsa.other._shared.llamadasapi.mysql.TipoActivo.CRIPTOMONEDAS;

public enum Criptomonedas {
    BTCUSD("Bitcoin", CRIPTOMONEDAS),
    LTCUSD("Litecoin", CRIPTOMONEDAS),
    ETHUSD("Etherium", CRIPTOMONEDAS);

    public final String nombreActivo;
    public final TipoActivo tipoActivo;

    Criptomonedas(String nombreActivo, TipoActivo tipoActivo) {
        this.nombreActivo = nombreActivo;
        this.tipoActivo = tipoActivo;
    }
    
    public static String getNombreActivo(String simbolo) {
        Optional<Criptomonedas> valor = Stream.of(Criptomonedas.values())
                .filter(mat -> mat.nombreActivo.equalsIgnoreCase(simbolo))
                .findAny();

        return valor.isPresent() ?
                valor.get().nombreActivo :
                simbolo;
    }
}
