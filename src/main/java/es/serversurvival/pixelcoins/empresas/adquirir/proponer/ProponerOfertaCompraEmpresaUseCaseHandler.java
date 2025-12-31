package es.serversurvival.pixelcoins.empresas.adquirir.proponer;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.pixelcoins._shared.Validador;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseHandler;
import es.serversurvival.pixelcoins.empresas._shared.EmpresasValidador;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.applicaion.AccionistasEmpresasService;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.domain.TipoVotacion;
import es.serversurvival.pixelcoins.empresas._shared.votaciones.iniciar.IniciarVotacionUseCase;
import es.serversurvival.pixelcoins.empresas._shared.votaciones.votar.VotarVotacionParametros;
import es.serversurvival.pixelcoins.empresas._shared.votaciones.votar.VotarVotacionUseCase;
import lombok.AllArgsConstructor;

import java.util.UUID;

@UseCase
@AllArgsConstructor
public class ProponerOfertaCompraEmpresaUseCaseHandler implements UseCaseHandler<ProponerOfertaCompraEmpresaParametros> {
    private final AccionistasEmpresasService accionistasEmpresasService;
    private final IniciarVotacionUseCase iniciarVotacionUseCase;
    private final VotarVotacionUseCase votarVotacionUseCase;
    private final EmpresasValidador empresasValidador;
    private final Validador validador;
    private final EventBus eventBus;

    @Override
    public void handle(ProponerOfertaCompraEmpresaParametros parametros) {
        validar(parametros);

        UUID votacionId = iniciarVotacionUseCase.iniciar(AceptarOfertaCompraEmpresaVotacion.builder()
                .tipo(TipoVotacion.ACEPTAR_OFERTA_COMPRA_EMPRESA)
                .empresaId(parametros.getEmpresaAComprarId())
                .descripccion("")
                .iniciadoPorJugadorId(parametros.getJugadorIdIniciador())
                .precioTotal(parametros.getPrecioTotal())
                .elCompradorEsUnJugador(parametros.getTipoComprador() == ProponerOfertaCompraEmpresaParametros.TipoComprador.JUGADOR)
                .compradorId(parametros.getCompradorId())
                .build());

        if (accionistasEmpresasService.esAccionista(parametros.getJugadorIdIniciador(), parametros.getEmpresaAComprarId())) {
            votarVotacionUseCase.handle(VotarVotacionParametros.builder()
                    .jugadorId(parametros.getJugadorIdIniciador())
                    .empresaId(parametros.getEmpresaAComprarId())
                    .votacionId(votacionId)
                    .autoVoto(true)
                    .aFavor(true)
                    .build());
        }

        eventBus.publish(OfertaCompraEmpresaPropuesta.builder()
                .compradorId(parametros.getCompradorId())
                .empresaAComprarId(parametros.getEmpresaAComprarId())
                .tipoComprador(parametros.getTipoComprador())
                .precioTotal(parametros.getPrecioTotal())
                .build());
    }

    private void validar(ProponerOfertaCompraEmpresaParametros parametros) {
        empresasValidador.empresaNoCerrada(parametros.getEmpresaAComprarId());
        empresasValidador.empresaCotizada(parametros.getEmpresaAComprarId());
        validador.numeroMayorQueCero(parametros.getPrecioTotal(), "El precio de compra");

        switch (parametros.getTipoComprador()) {
            case EMPRESA -> {
                validador.notEqual(parametros.getEmpresaAComprarId(), parametros.getCompradorId(), "La empresa");
                empresasValidador.tienePixelcoinsSuficientes(parametros.getCompradorId(), parametros.getPrecioTotal());
            }
            case JUGADOR -> {
                validador.jugadorTienePixelcoins(parametros.getCompradorId(), parametros.getPrecioTotal());
            }
        }
    }

}
