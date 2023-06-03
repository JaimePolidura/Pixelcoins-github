package es.serversurvival.v2.pixelcoins.jugadores.cambiar.ingresarItem;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.v2.pixelcoins.transacciones.TipoTransaccion;
import es.serversurvival.v2.pixelcoins.transacciones.Transaccion;
import es.serversurvival.v2.pixelcoins.transacciones.TransaccionesService;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class IngresarItemUseCase {
    private final TransaccionesService transaccionesService;
    private final EventBus eventBus;

    public void ingresarItem(IngresarItemParametros parametros) {
        double pixelcoinsAnadir = parametros.getTipoCambio().cambio * parametros.getCantiadad();

        this.transaccionesService.save(Transaccion.builder()
                .tipo(TipoTransaccion.JUGADORES_CAMBIO_INGRESAR_ITEM)
                .pagadoId(parametros.getJugadorId())
                .pixelcoins(pixelcoinsAnadir)
                .objeto(parametros.getTipoCambio().name())
                .build());

        this.eventBus.publish(new ItemIngresadoEvento(parametros.getJugadorId(), pixelcoinsAnadir, parametros.getTipoCambio().name()));
    }
}
