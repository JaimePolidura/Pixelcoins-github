package es.serversurvival.pixelcoins.empresas.despedir;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class EmpleadoDespedido extends PixelcoinsEvento {
    @Getter private final String causeDespido;
    @Getter private final UUID empleadoId;
    @Getter private final UUID empresaId;
}
