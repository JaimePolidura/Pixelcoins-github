package es.serversurvival.v2.pixelcoins.empresas.comprarservicio;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class ComprarServicioParametros {
    @Getter private final UUID compradorJugadorId;
    @Getter private final UUID empresaId;
    @Getter private final double pixelcoins;
}
