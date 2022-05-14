package es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.tipoactivos.criptomonedas;

import java.util.Optional;
import java.util.stream.Stream;

import static es.serversurvival.bolsa.activosinfo._shared.domain.TipoActivo.CRIPTOMONEDAS;

public enum SupportedCriptomonedas {
    BTCUSD("Bitcoin", CRIPTOMONEDAS),
    LTCUSD("Litecoin", CRIPTOMONEDAS),
    ETHUSD("Etherium", CRIPTOMONEDAS);

    public final String nombreActivo;
    public final TipoActivo tipoActivo;

    SupportedCriptomonedas(String nombreActivo, TipoActivo tipoActivo) {
        this.nombreActivo = nombreActivo;
        this.tipoActivo = tipoActivo;
    }
    
    public static String getNombreActivoLargo(String simbolo) {
        Optional<SupportedCriptomonedas> valor = Stream.of(SupportedCriptomonedas.values())
                .filter(mat -> mat.nombreActivo.equalsIgnoreCase(simbolo))
                .findAny();

        return valor.isPresent() ?
                valor.get().nombreActivo :
                simbolo;
    }
}
