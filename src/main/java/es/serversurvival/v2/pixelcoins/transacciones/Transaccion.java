package es.serversurvival.v2.pixelcoins.transacciones;

import es.serversurvival.v1._shared.utils.Funciones;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
public final class Transaccion {
    @Getter private final UUID transaccionId;
    @Getter private final TipoTransaccion tipo;
    @Getter private final UUID pagadorId;
    @Getter private final UUID pagadoId;
    @Getter private final double pixelcoins;
    @Getter private final LocalDateTime fecha;
    @Getter private final String objeto;

    public static TransaccionBuilder builder() {
        return new TransaccionBuilder();
    }

    public static class TransaccionBuilder {
        private UUID transaccionId;
        private TipoTransaccion tipo;
        private UUID pagadorId;
        private UUID pagadoId;
        private double pixelcoins;
        private LocalDateTime fecha;
        private String objeto;

        public TransaccionBuilder() {
            this.transaccionId = UUID.randomUUID();
            this.fecha = LocalDateTime.now();
            this.pagadoId = Funciones.NULL_ID;
            this.pagadorId = Funciones.NULL_ID;
        }

        public TransaccionBuilder tipo(TipoTransaccion tipo) {
            this.tipo = tipo;
            return this;
        }

        public TransaccionBuilder pagadorId(UUID pagadorId) {
            this.pagadorId = pagadorId;
            return this;
        }

        public TransaccionBuilder pagadoId(UUID pagadoId) {
            this.pagadoId = pagadoId;
            return this;
        }

        public TransaccionBuilder pixelcoins(double pixelcoins) {
            this.pixelcoins = pixelcoins;
            return this;
        }

        public TransaccionBuilder objeto(UUID objeto) {
            this.objeto = objeto.toString();
            return this;
        }

        public TransaccionBuilder objeto(String objeto) {
            this.objeto = objeto;
            return this;
        }

        public Transaccion build() {
            return new Transaccion(transaccionId, tipo, pagadorId, pagadoId, pixelcoins, fecha, objeto);
        }
    }
}
