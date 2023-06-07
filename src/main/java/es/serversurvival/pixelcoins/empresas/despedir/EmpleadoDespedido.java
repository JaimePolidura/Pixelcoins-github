package es.serversurvival.pixelcoins.empresas.despedir;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class EmpleadoDespedido extends PixelcoinsEvento {
    @Getter private final UUID empleadoId;
}
