package es.serversurvival.pixelcoins.empresas.ponerventa;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseHandler;
import es.serversurvival.pixelcoins.empresas._shared.EmpresasValidador;
import es.serversurvival.pixelcoins.empresas._shared.empresas.application.EmpresasService;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import es.serversurvival.pixelcoins.mercado._shared.OfertasService;
import es.serversurvival.pixelcoins.mercado._shared.TipoOferta;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.domain.AccionistaEmpresa;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.applicaion.AccionistasEmpresasService;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.OfertaAccionMercadoJugador;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class PonerVentaAccionesUseCase implements UseCaseHandler<PonerVentaAccionesParametros> {
    private final AccionistasEmpresasService accionistasEmpresasService;
    private final EmpresasService empresasService;
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
        Empresa empresa = empresasService.getById(parametros.getEmpresaId());
        boolean esFundador = empresa.getFundadorJugadorId().equals(parametros.getJugadorId());
        validador.accionesNoEstanYaALaVenta(acciones.getAccionistaId(), parametros.getCantidadAcciones(), esFundador);

        ofertasService.save(OfertaAccionMercadoJugador.builder()
                .vendedorId(parametros.getJugadorId())
                .cantidad(parametros.getCantidadAcciones())
                .precio(parametros.getPrecioPorAccion())
                .empresaId(parametros.getEmpresaId())
                .vendedorAccionesId(acciones.getAccionistaId())
                .build());

        eventBus.publish(new AccionPuestaVenta(parametros));
    }
}
