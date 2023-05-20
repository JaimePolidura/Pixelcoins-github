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
    private final EmpresasValidador empresasValidador;
    private final OfertasService ofertasService;
    private final EventBus eventBus;

    public void ponerVenta(PonerVentaAccionesParametros parametros) {
        empresasValidador.numerAccionesValido(parametros.getCantidadAcciones());
        empresasValidador.empresaNoCerrada(parametros.getEmpresaId());
        empresasValidador.empresaCotizada(parametros.getEmpresaId());
        empresasValidador.accionistaDeEmpresa(parametros.getEmpresaId(), parametros.getJugadorId());
        empresasValidador.precioPorAccion(parametros.getPrecioPorAccion());
        empresasValidador.jugadorTieneAcciones(parametros.getEmpresaId(), parametros.getJugadorId(), parametros.getCantidadAcciones());

        AccionistaEmpresa acciones = accionistasEmpresasService.getByEmpresaIdAndJugadorId(parametros.getEmpresaId(), parametros.getJugadorId());

        ofertasService.save(OfertaAccionMercadoJugador.builder()
                .vendedorId(parametros.getJugadorId())
                .accionistaJugadorId(parametros.getJugadorId())
                .tipoOferta(TipoOferta.ACCIONES_SERVER_JUGADOR)
                .cantidad(parametros.getCantidadAcciones())
                .precio(parametros.getPrecioPorAccion())
                .empresaId(parametros.getEmpresaId())
                .objeto(acciones.getAccionistaId())
                .build());

        eventBus.publish(new AccionPuestaVenta(parametros));
    }
}
