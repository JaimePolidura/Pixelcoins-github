package es.serversurvival.pixelcoins.transacciones.domain;


import es.serversurvival._shared.utils.Funciones;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@ToString
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
    @Getter private UUID otro;

    public static Transaccion.TransaccionBuilder builder() {
        return new Transaccion.TransaccionBuilder();
    }

    public static class TransaccionBuilder {
        private UUID transaccionId;
        private TipoTransaccion tipo;
        private UUID pagadorId;
        private UUID pagadoId;
        private double pixelcoins;
        private LocalDateTime fecha;
        private String objeto;
        private UUID otro;

        public TransaccionBuilder() {
            this.transaccionId = UUID.randomUUID();
            this.fecha = LocalDateTime.now();
            this.pagadoId = Funciones.NULL_ID;
            this.pagadorId = Funciones.NULL_ID;
            this.otro = Funciones.NULL_ID;
            this.objeto = "";
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

        public TransaccionBuilder otro(UUID otro) {
            this.otro = otro;
            return this;
        }

        public Transaccion build() {
            return new Transaccion(transaccionId, tipo, pagadorId, pagadoId, pixelcoins, fecha, objeto, otro);
        }
    }
}
