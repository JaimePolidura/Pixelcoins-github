package es.serversurvival.pixelcoins.jugadores.cambiar.ingresarItem;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseHandler;
import es.serversurvival.pixelcoins.transacciones.domain.TipoTransaccion;
import es.serversurvival.pixelcoins.transacciones.domain.Transaccion;
import es.serversurvival.pixelcoins.transacciones.application.TransaccionesSaver;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class IngresarItemUseCase implements UseCaseHandler<IngresarItemParametros> {
    private final TransaccionesSaver transaccionesSaver;
    private final EventBus eventBus;

    @Override
    public void handle(IngresarItemParametros parametros) {
        double pixelcoinsAnadir = parametros.getTipoCambio().cambio * parametros.getCantiadad();

        transaccionesSaver.save(Transaccion.builder()
                .tipo(TipoTransaccion.JUGADORES_CAMBIO_INGRESAR_ITEM)
                .pagadoId(parametros.getJugadorId())
                .pixelcoins(pixelcoinsAnadir)
                .objeto(parametros.getTipoCambio().name())
                .build());

        eventBus.publish(new ItemIngresadoEvento(parametros.getJugadorId(), pixelcoinsAnadir, parametros.getTipoCambio()));
    }
}
