package es.serversurvival.pixelcoins.deudas.pagartodo;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.pixelcoins._shared.Validador;
import es.serversurvival.pixelcoins.mercado._shared.OfertasService;
import es.serversurvival.pixelcoins.mercado._shared.TipoOferta;
import es.serversurvival.pixelcoins.transacciones.TipoTransaccion;
import es.serversurvival.pixelcoins.transacciones.Transaccion;
import es.serversurvival.pixelcoins.transacciones.TransaccionesService;
import es.serversurvival.pixelcoins.deudas._shared.Deuda;
import es.serversurvival.pixelcoins.deudas._shared.DeudasService;
import es.serversurvival.pixelcoins.deudas._shared.DeudasValidador;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class PagarTodaLaDeudaUseCase {
    private final TransaccionesService transaccionesService;
    private final DeudasValidador deudasValidador;
    private final OfertasService ofertasService;
    private final DeudasService deudasService;
    private final Validador validador;
    private final EventBus eventBus;

    public void pagarTodaLaDeuda(PagarTodaLaDeudaParametros parametros) {
        Deuda deuda = this.deudasService.getById(parametros.getDeudaId());
        double pixelcoinsRestantes = deuda.getPixelcoinsRestantesDePagar();

        deudasValidador.deudorDeDeuda(parametros.getDeudaId(), parametros.getJugadorId());
        deudasValidador.deudaPendiente(parametros.getDeudaId());
        validador.jugadorTienePixelcoins(parametros.getJugadorId(), deuda.getPixelcoinsRestantesDePagar());

        deudasService.save(deuda.anotarPagadoPorCompleto());
        ofertasService.deleteByObjetoYTipo(parametros.getDeudaId().toString(), TipoOferta.DEUDA_MERCADO_SECUNDARIO);
        transaccionesService.save(Transaccion.builder()
                    .pagadoId(deuda.getAcredorJugadorId())
                    .pagadorId(parametros.getJugadorId())
                    .pixelcoins(pixelcoinsRestantes)
                    .objeto(deuda.getDeudaId().toString())
                    .tipo(TipoTransaccion.DEUDAS_PAGO_COMPLETO)
                .build());

        eventBus.publish(new DeudaPagadoPorCompleto(parametros.getDeudaId(), deuda.getDeudorJugadorId()));
    }
}
