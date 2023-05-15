package es.serversurvival.v2.pixelcoins.jugadores.cambiar.ingresarItem;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.v2.pixelcoins.jugadores._shared.Jugador;
import es.serversurvival.v2.pixelcoins.jugadores._shared.JugadoresService;
import es.serversurvival.v2.pixelcoins.jugadores.cambiar.TipoCambioPixelcoins;
import es.serversurvival.v2.pixelcoins.transacciones.TipoTransaccion;
import es.serversurvival.v2.pixelcoins.transacciones.Transaccion;
import es.serversurvival.v2.pixelcoins.transacciones.TransaccionesService;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class IngresarItemUseCase {
    private final TransaccionesService transaccionesService;
    private final JugadoresService jugadoresService;
    private final EventBus eventBus;

    public void ingresarItem(String nombreJugador, TipoCambioPixelcoins tipoCambio, int cantidad) {
        Jugador jugador = jugadoresService.getByNombre(nombreJugador);

        double pixelcoinsAnadir = tipoCambio.cambio * cantidad;

        this.transaccionesService.save(Transaccion.builder()
                        .tipo(TipoTransaccion.JUGADORES_CAMBIO_INGRESAR_ITEM)
                        .pagadoId(jugador.getJugadorId())
                        .pixelcoins(pixelcoinsAnadir)
                        .objeto(tipoCambio.name())
                .build());

        this.eventBus.publish(new ItemIngresadoEvento(jugador, pixelcoinsAnadir, tipoCambio.name()));
    }
}
