package es.serversurvival.pixelcoins.jugadores.cambiar.ingresarItem;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseHandler;
import es.serversurvival.pixelcoins.transacciones.TipoTransaccion;
import es.serversurvival.pixelcoins.transacciones.Transaccion;
import es.serversurvival.pixelcoins.transacciones.TransaccionesService;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class IngresarItemUseCase implements UseCaseHandler<IngresarItemParametros> {
    private final TransaccionesService transaccionesService;
    private final EventBus eventBus;

    @Override
    public void handle(IngresarItemParametros parametros) throws Exception {
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
