package es.serversurvival.pixelcoins.transacciones;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival._shared.utils.Funciones;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Service
@AllArgsConstructor
public class TransaccionesSaver {
    private final TransaccionesBalanceCache transaccionesBalanceCache;
    private final MovimientosService movimientosService;

    public void save(Transaccion transaccion) {
        if(!transaccion.getPagadorId().equals(Funciones.NULL_ID)){
            movimientosService.save(Movimiento.builder()
                    .movimientoId(UUID.randomUUID())
                    .transaccionId(transaccion.getTransaccionId())
                    .entidadId(transaccion.getPagadoId())
                    .otraEntidadId(transaccion.getPagadorId())
                    .pixelcoins(transaccion.getPixelcoins())
                    .tipo(transaccion.getTipo())
                    .objeto(transaccion.getObjeto())
                    .fecha(transaccion.getFecha())
                    .build());
        }

        if(!transaccion.getPagadoId().equals(Funciones.NULL_ID)){
            movimientosService.save(Movimiento.builder()
                    .movimientoId(UUID.randomUUID())
                    .transaccionId(transaccion.getTransaccionId())
                    .entidadId(transaccion.getPagadorId())
                    .otraEntidadId(transaccion.getPagadoId())
                    .pixelcoins(transaccion.getPixelcoins() * -1)
                    .tipo(transaccion.getTipo())
                    .objeto(transaccion.getObjeto())
                    .fecha(transaccion.getFecha())
                    .build());
        }

        transaccionesBalanceCache.update(transaccion);
    }
}
