package es.serversurvival.v2.pixelcoins.bolsa.abrir;

import es.serversurvival.v2.pixelcoins.bolsa._shared.posiciones.TipoBolsaApuesta;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class AbrirPosicoinBolsaParametros {
    @Getter private final UUID jugadorId;
    @Getter private final TipoBolsaApuesta tipoApuesta;
    @Getter private final UUID actiboBolsaId;
    @Getter private final int cantidad;
}
