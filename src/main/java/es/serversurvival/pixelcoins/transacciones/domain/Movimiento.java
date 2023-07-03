package es.serversurvival.pixelcoins.transacciones.domain;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public final class Movimiento {
    @Getter private UUID movimientoId;
    @Getter private UUID transaccionId;
    @Getter private UUID entidadId;
    @Getter private UUID otraEntidadId;
    @Getter private double pixelcoins;
    @Getter private TipoTransaccion tipo;
    @Getter private LocalDateTime fecha;
    @Getter private String objeto;
}
