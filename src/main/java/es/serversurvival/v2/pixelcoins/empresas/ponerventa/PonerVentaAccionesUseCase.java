package es.serversurvival.v2.pixelcoins.empresas.ponerventa;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.v2.pixelcoins.empresas._shared.EmpresasValidador;
import es.serversurvival.v2.pixelcoins.empresas._shared.accionistas.OfertaAccionMercadoJugador;
import es.serversurvival.v2.pixelcoins.mercado._shared.Oferta;
import es.serversurvival.v2.pixelcoins.mercado._shared.OfertasService;
import es.serversurvival.v2.pixelcoins.mercado._shared.TipoOferta;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class PonerVentaAccionesUseCase {
    private final EmpresasValidador empresasValidador;
    private final OfertasService ofertasService;
    private final EventBus eventBus;

    public void ponerVenta(PonerVentaAccionesUseCaseParametros parametros) {
        empresasValidador.empresaNoCerrada(parametros.getEmpresaId());
        empresasValidador.empresaCotizada(parametros.getEmpresaId());
        empresasValidador.accionistaDeEmpresa(parametros.getEmpresaId(), parametros.getJugadorId());
        empresasValidador.precioPorAccion(parametros.getPrecioPorAccion());
        empresasValidador.jugadorTieneAcciones(parametros.getEmpresaId(), parametros.getJugadorId(), parametros.getCantidadAcciones());
        empresasValidador.numerAccionesValido(parametros.getCantidadAcciones());

        Oferta oferta = OfertaAccionMercadoJugador.builder()
                .vendedorId(parametros.getJugadorId())
                .accionistaJugadorId(parametros.getJugadorId())
                .tipoOferta(TipoOferta.ACCIONES_EMPRESA_SERVER_MERCADO_JUGADOR)
                .cantidad(parametros.getCantidadAcciones())
                .precio(parametros.getPrecioPorAccion())
                .objeto(parametros.getEmpresaId())
                .build();

        ofertasService.save(oferta);

        eventBus.publish(new AccionPuestaVenta(oferta.getOfertaId()));
    }
}
