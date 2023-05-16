package es.serversurvival.v2.pixelcoins._shared;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.javaddd.domain.exceptions.IllegalQuantity;
import es.serversurvival.v1._shared.exceptions.NotEnoughPixelcoins;
import es.serversurvival.v2.pixelcoins.jugadores._shared.Jugador;
import es.serversurvival.v2.pixelcoins.transacciones.TransaccionesService;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Service
@AllArgsConstructor
public final class Validador {
    private final TransaccionesService transaccionesService;

    public void asegurarseNumeroMayorQueCero(String nombreNumro, double numero) {
        if(numero <= 0){
            throw new IllegalQuantity(String.format("%s tiene que ser un numero positivo mayor que 0", nombreNumro));
        }
    }

    public void asegurarseJugadorTienePixelcoins(Jugador jugador, double pixelcoinsMinimo) {
        if(transaccionesService.getBalancePixelcions(jugador.getJugadorId()) < pixelcoinsMinimo){
            throw new NotEnoughPixelcoins(String.format("El jugador %s no tiene las suficientes pixelcoins", jugador.getNombre()));
        }
    }

    public void asegurarseJugadorTienePixelcoins(UUID jugadorId, double pixelcoinsMinimo) {
        if(transaccionesService.getBalancePixelcions(jugadorId) < pixelcoinsMinimo){
            throw new NotEnoughPixelcoins("No tienes las suficientes pixelcoins");
        }
    }
}
