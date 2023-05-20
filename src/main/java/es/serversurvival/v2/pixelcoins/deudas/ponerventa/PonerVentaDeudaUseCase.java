package es.serversurvival.v2.pixelcoins.deudas.ponerventa;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.v2.pixelcoins._shared.Validador;
import es.serversurvival.v2.pixelcoins.deudas._shared.Deuda;
import es.serversurvival.v2.pixelcoins.deudas._shared.DeudasService;
import es.serversurvival.v2.pixelcoins.deudas._shared.DeudasValidador;
import es.serversurvival.v2.pixelcoins.deudas._shared.OfertaDeudaMercadoSecundario;
import es.serversurvival.v2.pixelcoins.mercado._shared.Oferta;
import es.serversurvival.v2.pixelcoins.mercado._shared.TipoOferta;
import es.serversurvival.v2.pixelcoins.mercado.ofrecer.OfrecerOfertaUseCase;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class PonerVentaDeudaUseCase {
    private final OfrecerOfertaUseCase ofrecerOfertaUseCase;
    private final DeudasValidador deudasValidador;
    private final DeudasService deudasService;
    private final Validador validador;
    private final EventBus eventBus;

    public void vender(PonerVentaDeudaUseCaseParametros parametros) {
        deudasValidador.deudaPendiente(parametros.getDeudaId());
        deudasValidador.acredorDeDeuda(parametros.getDeudaId(), parametros.getJugadorId());
        validador.numeroMayorQueCero(parametros.getPrecio(), "El precio de la deuda");

        Deuda deudaAVender = deudasService.getById(parametros.getDeudaId());

        Oferta oferta = OfertaDeudaMercadoSecundario.builder()
                .vendedorId(parametros.getJugadorId())
                .precio(parametros.getPrecio())
                .tipoOferta(TipoOferta.DEUDA_MERCADO_SECUNDARIO)
                .objeto(deudaAVender.getDeudaId())
                .build();

        ofrecerOfertaUseCase.ofrecer(oferta);

        eventBus.publish(new DeudaPuestaVenta(deudaAVender.getDeudaId(), oferta.getOfertaId(), oferta.getPrecio()));
    }
}
