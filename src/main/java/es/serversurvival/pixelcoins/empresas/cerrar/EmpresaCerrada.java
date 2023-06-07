package es.serversurvival.pixelcoins.empresas.cerrar;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class EmpresaCerrada extends PixelcoinsEvento {
    @Getter private final UUID empresaId;
}
