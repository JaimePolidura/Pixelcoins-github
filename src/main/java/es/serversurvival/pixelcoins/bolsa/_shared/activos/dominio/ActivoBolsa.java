package es.serversurvival.pixelcoins.bolsa._shared.activos.dominio;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
public final class ActivoBolsa {
    @Getter private UUID activoBolsaId;
    @Getter private TipoActivoBolsa tipoActivo;
    @Getter private String nombreCorto;
    @Getter private String nombreLargo;
    @Getter private int nReferencias;

    public ActivoBolsa decrementarNReferencias() {
        return new ActivoBolsa(activoBolsaId, tipoActivo, nombreCorto, nombreLargo, nReferencias - 1);
    }

    public ActivoBolsa incrementarNReferencias() {
        return new ActivoBolsa(activoBolsaId, tipoActivo, nombreCorto, nombreLargo, nReferencias + 1);
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
