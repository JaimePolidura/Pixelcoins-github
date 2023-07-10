package es.serversurvival.pixelcoins.deudas.ponerventa;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.pixelcoins._shared.Validador;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseHandler;
import es.serversurvival.pixelcoins.mercado.ponerventa.PonerVentaOfertaParametros;
import es.serversurvival.pixelcoins.mercado.ponerventa.PonerVentaOfertaUseCase;
import es.serversurvival.pixelcoins.deudas._shared.application.DeudasValidador;
import es.serversurvival.pixelcoins.deudas._shared.OfertaDeudaMercadoSecundario;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class PonerVentaDeudaUseCase implements UseCaseHandler<PonerVentaDeudaParametros> {
    private final PonerVentaOfertaUseCase ofrecerOfertaUseCase;
    private final DeudasValidador deudasValidador;
    private final Validador validador;
    private final EventBus eventBus;

    @Override
    public void handle(PonerVentaDeudaParametros parametros) {
        deudasValidador.deudaPendiente(parametros.getDeudaId());
        deudasValidador.acredorDeDeuda(parametros.getDeudaId(), parametros.getJugadorId());
        validador.numeroMayorQueCero(parametros.getPrecio(), "El precio de la deuda");

        ofrecerOfertaUseCase.handle(PonerVentaOfertaParametros.of(OfertaDeudaMercadoSecundario.builder()
                .vendedorId(parametros.getJugadorId())
                .precio(parametros.getPrecio())
                .deudaId(parametros.getDeudaId())
                .build()));

        eventBus.publish(new DeudaPuestaVenta(parametros));
    }
}
