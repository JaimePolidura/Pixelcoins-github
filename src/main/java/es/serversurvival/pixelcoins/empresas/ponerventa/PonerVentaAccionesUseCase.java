package es.serversurvival.pixelcoins.empresas.ponerventa;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseHandler;
import es.serversurvival.pixelcoins.empresas._shared.EmpresasValidador;
import es.serversurvival.pixelcoins.mercado._shared.OfertasService;
import es.serversurvival.pixelcoins.mercado._shared.TipoOferta;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.AccionistaEmpresa;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.AccionistasEmpresasService;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.OfertaAccionMercadoJugador;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class PonerVentaAccionesUseCase implements UseCaseHandler<PonerVentaAccionesParametros> {
    private final AccionistasEmpresasService accionistasEmpresasService;
    private final OfertasService ofertasService;
    private final EmpresasValidador validador;
    private final EventBus eventBus;

    @Override
    public void handle(PonerVentaAccionesParametros parametros) {
        validador.numerAccionesValido(parametros.getCantidadAcciones());
        validador.empresaNoCerrada(parametros.getEmpresaId());
        validador.empresaCotizada(parametros.getEmpresaId());
        validador.accionistaDeEmpresa(parametros.getEmpresaId(), parametros.getJugadorId());
        validador.precioPorAccion(parametros.getPrecioPorAccion());
        validador.jugadorTieneAcciones(parametros.getEmpresaId(), parametros.getJugadorId(), parametros.getCantidadAcciones());
        AccionistaEmpresa acciones = accionistasEmpresasService.getByEmpresaIdAndJugadorId(parametros.getEmpresaId(), parametros.getJugadorId());
        validador.accionesNoEstanYaALaVenta(acciones.getAccionistaId(), parametros.getCantidadAcciones());

        ofertasService.save(OfertaAccionMercadoJugador.builder()
                .vendedorId(parametros.getJugadorId())
                .tipoOferta(TipoOferta.ACCIONES_SERVER_JUGADOR)
                .cantidad(parametros.getCantidadAcciones())
                .precio(parametros.getPrecioPorAccion())
                .empresaId(parametros.getEmpresaId())
                .objeto(acciones.getAccionistaId())
                .build());

        eventBus.publish(new AccionPuestaVenta(parametros));
    }
}
