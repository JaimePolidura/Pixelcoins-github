package es.serversurvival.pixelcoins.empresas.cambiardirector.proponer;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class NuevoDirectorPropuesto extends PixelcoinsEvento {
    @Getter private final UUID jugadorId;
    @Getter private final UUID empresaId;
    @Getter private final UUID nuevoDirectorId;
    @Getter private final String razonCambio;
}
