package es.serversurvival.pixelcoins.transacciones.application;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.EventBus;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.pixelcoins.transacciones.application.MovimientosService;
import es.serversurvival.pixelcoins.transacciones.application.TransaccionesBalanceCache;
import es.serversurvival.pixelcoins.transacciones.domain.Movimiento;
import es.serversurvival.pixelcoins.transacciones.domain.Transaccion;
import es.serversurvival.pixelcoins.transacciones.domain.TransaccionCreada;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Service
@AllArgsConstructor
public class TransaccionesSaver {
    private final TransaccionesBalanceCache transaccionesBalanceCache;
    private final MovimientosService movimientosService;
    private final EventBus eventBus;

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

        eventBus.publish(new TransaccionCreada(transaccion));
    }
}
