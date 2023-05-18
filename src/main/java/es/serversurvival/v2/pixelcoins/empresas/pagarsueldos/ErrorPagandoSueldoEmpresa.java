package es.serversurvival.v2.pixelcoins.empresas.pagarsueldos;

import es.serversurvival.v1._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public final class ErrorPagandoSueldoEmpresa extends PixelcoinsEvento {
    private final UUID empleadoId;
    private final double sueldo;
}
