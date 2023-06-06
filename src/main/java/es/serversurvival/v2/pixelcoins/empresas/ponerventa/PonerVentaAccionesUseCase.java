package es.serversurvival.v2.pixelcoins.empresas.ponerventa;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.v2.pixelcoins.empresas._shared.EmpresasValidador;
import es.serversurvival.v2.pixelcoins.empresas._shared.accionistas.AccionistaEmpresa;
import es.serversurvival.v2.pixelcoins.empresas._shared.accionistas.AccionistasEmpresasService;
import es.serversurvival.v2.pixelcoins.empresas._shared.accionistas.OfertaAccionMercadoJugador;
import es.serversurvival.v2.pixelcoins.mercado._shared.OfertasService;
import es.serversurvival.v2.pixelcoins.mercado._shared.TipoOferta;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class PonerVentaAccionesUseCase {
    private final AccionistasEmpresasService accionistasEmpresasService;
    private final OfertasService ofertasService;
    private final EmpresasValidador validador;
    private final EventBus eventBus;

    public void ponerVenta(PonerVentaAccionesParametros parametros) {
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
