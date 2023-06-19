package es.serversurvival.pixelcoins.transacciones;

import es.serversurvival._shared.utils.Funciones;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
public final class Transaccion {
    @Getter private UUID transaccionId;
    @Getter private TipoTransaccion tipo;
    @Getter private UUID pagadorId;
    @Getter private UUID pagadoId;
    @Getter private double pixelcoins;
    @Getter private LocalDateTime fecha;
    @Getter private String objeto;

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
