package es.serversurvival.v2.pixelcoins.jugadores.pagar;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.CannotBeYourself;
import es.serversurvival.v1._shared.exceptions.NotEnoughPixelcoins;
import es.serversurvival.v2.pixelcoins.jugadores._shared.Jugador;
import es.serversurvival.v2.pixelcoins.transacciones.TipoTransaccion;
import es.serversurvival.v2.pixelcoins.transacciones.Transaccion;
import es.serversurvival.v2.pixelcoins.transacciones.TransaccionesService;
import lombok.AllArgsConstructor;

import java.util.UUID;

@UseCase
@AllArgsConstructor
public final class PagarUseCase {
    private final TransaccionesService transaccionesService;
    private final EventBus eventBus;

    public void hacerPago(Jugador pagador, Jugador pagado, double pixelcoinsPago) {
        asegurarseNoEsElMismoJugador(pagador.getJugadorId(), pagado.getJugadorId());
        asegurarsePixecoinsNoNegativo(pixelcoinsPago);
        asegurarseTienePixelcoins(pagador, pixelcoinsPago);

        transaccionesService.save(Transaccion.builder()
                        .pagadorId(pagador.getJugadorId())
                        .pagadoId(pagado.getJugadorId())
                        .pixelcoins(pixelcoinsPago)
                        .tipo(TipoTransaccion.JUGADORES_PAGO)
                .build());

        eventBus.publish(new JugadorPagadoManualmente(pixelcoinsPago, pagador.getJugadorId(), pagado.getJugadorId()));
    }

    private void asegurarsePixecoinsNoNegativo(double pixelcoinsPagador) {
        if(pixelcoinsPagador <= 0){
            throw new NotEnoughPixelcoins("Tu eres tonto o q te pasa?!");
        }
    }

    private void asegurarseTienePixelcoins(Jugador pagador, double pixelcoinsPago) {
        var pixelcoinsPagador = this.transaccionesService.getBalancePixelcions(pagador.getJugadorId());

        if(pixelcoinsPago > pixelcoinsPagador){
            throw new NotEnoughPixelcoins("No tienes las suficientes pixelcoins para realizar el pago");
        }
    }

    private void asegurarseNoEsElMismoJugador(UUID pagdorId, UUID pagadoId) {
        if(pagdorId != pagadoId){
            throw new CannotBeYourself("No te puedes pagarte a ti mismo");
        }
    }
}
