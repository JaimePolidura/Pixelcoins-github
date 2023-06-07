package es.serversurvival.pixelcoins.empresas.contratar;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class JugadorContratado extends PixelcoinsEvento {
    @Getter private final UUID empleadoId;
}
