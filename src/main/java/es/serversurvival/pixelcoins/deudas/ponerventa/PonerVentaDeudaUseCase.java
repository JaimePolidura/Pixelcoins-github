package es.serversurvival.pixelcoins.deudas.ponerventa;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.pixelcoins._shared.Validador;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseHandler;
import es.serversurvival.pixelcoins.mercado._shared.TipoOferta;
import es.serversurvival.pixelcoins.mercado.ofrecer.OfrecerOfertaParametros;
import es.serversurvival.pixelcoins.mercado.ofrecer.OfrecerOfertaUseCase;
import es.serversurvival.pixelcoins.deudas._shared.application.DeudasValidador;
import es.serversurvival.pixelcoins.deudas._shared.OfertaDeudaMercadoSecundario;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class PonerVentaDeudaUseCase implements UseCaseHandler<PonerVentaDeudaParametros> {
    private final OfrecerOfertaUseCase ofrecerOfertaUseCase;
    private final DeudasValidador deudasValidador;
    private final Validador validador;
    private final EventBus eventBus;

    @Override
    public void handle(PonerVentaDeudaParametros parametros) {
        deudasValidador.deudaPendiente(parametros.getDeudaId());
        deudasValidador.acredorDeDeuda(parametros.getDeudaId(), parametros.getJugadorId());
        validador.numeroMayorQueCero(parametros.getPrecio(), "El precio de la deuda");

        ofrecerOfertaUseCase.handle(OfrecerOfertaParametros.of(OfertaDeudaMercadoSecundario.builder()
                .vendedorId(parametros.getJugadorId())
                .precio(parametros.getPrecio())
                .tipoOferta(TipoOferta.DEUDA_MERCADO_SECUNDARIO)
                .objeto(parametros.getDeudaId())
                .build()));

        eventBus.publish(new DeudaPuestaVenta(parametros));
    }
}
