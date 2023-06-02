package es.serversurvival.v2.pixelcoins.bolsa._shared.activos.dominio;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class ActivoBolsa {
    @Getter private final UUID activoBolsaId;
    @Getter private final TipoActivoBolsa tipoActivoBolsa;
    @Getter private final String nombreCorto;
    @Getter private final String nombreLargo;
    @Getter private final int nReferencias;

    public ActivoBolsa decrementarNReferencias() {
        return new ActivoBolsa(activoBolsaId, tipoActivoBolsa, nombreCorto, nombreLargo, nReferencias - 1);
    }

    public ActivoBolsa incrementarNReferencias() {
        return new ActivoBolsa(activoBolsaId, tipoActivoBolsa, nombreCorto, nombreLargo, nReferencias + 1);
    }

    public static ActivoBolsaBuilder builder() {
        return new ActivoBolsaBuilder();
    }

    public static class ActivoBolsaBuilder {
        private TipoActivoBolsa tipoActivoBolsa;
        private String nomnbreCorto;
        private String nombreLargo;

        public ActivoBolsaBuilder tipoActivoBolsa(TipoActivoBolsa tipoActivoBolsa) {
            this.tipoActivoBolsa = tipoActivoBolsa;
            return this;
        }

        public ActivoBolsaBuilder nomnbreCorto(String nomnbreCorto) {
            this.nomnbreCorto = nomnbreCorto;
            return this;
        }

        public ActivoBolsaBuilder nombreLargo(String nombreLargo) {
            this.nombreLargo = nombreLargo;
            return this;
        }

        public ActivoBolsa build() {
            return new ActivoBolsa(UUID.randomUUID(), tipoActivoBolsa, nomnbreCorto, nombreLargo, 0);
        }
    }
}
