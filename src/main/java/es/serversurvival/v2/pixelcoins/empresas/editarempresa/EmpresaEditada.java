package es.serversurvival.v2.pixelcoins.empresas.editarempresa;

import es.serversurvival.v1._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class EmpresaEditada extends PixelcoinsEvento {
    @Getter private final UUID empresaId;
}
