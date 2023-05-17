package es.serversurvival.v2.pixelcoins.empresas.comprarservicio;

import es.serversurvival.v1._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class EmpresaServicioComprado extends PixelcoinsEvento {
    @Getter private final UUID empresaId;
    @Getter private final UUID compradorId;
    @Getter private final double pixelcoins;
}
