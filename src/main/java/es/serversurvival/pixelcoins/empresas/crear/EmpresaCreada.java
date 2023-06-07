package es.serversurvival.pixelcoins.empresas.crear;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class EmpresaCreada extends PixelcoinsEvento {
    @Getter private final UUID empresaId;
}
