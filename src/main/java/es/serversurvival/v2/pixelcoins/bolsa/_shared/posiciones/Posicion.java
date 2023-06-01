package es.serversurvival.v2.pixelcoins.bolsa._shared.posiciones;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
public final class Posicion {
    @Getter private final UUID posicionId;
    @Getter private final UUID activoBolsaId;
    @Getter private final UUID jugadorId;
    @Getter private final int cantidad;
    @Getter private final TipoBolsaApuesta tipoApuesta;
    @Getter private final TipoPosicion tipoPosicion;
    @Getter private final double precioApertura;
    @Getter private final LocalDateTime fechaApertura;
    @Getter private final double precioCierre;
    @Getter private final LocalDateTime fechaCierre;
}
