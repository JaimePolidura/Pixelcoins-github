package es.serversurvival.jugadores.cambio.ingresarItem;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.serversurvival.Pixelcoin;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;
import es.serversurvival.jugadores.cambio.TipoCambioPixelcoins;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@UseCase
@AllArgsConstructor
public final class IngresarItemUseCase {
    private final JugadoresService jugadoresService;

    public void ingresarItem(String nombrJugador, TipoCambioPixelcoins tipoCambio, int cantidad) {
        Jugador jugador = jugadoresService.getByNombre(nombrJugador);

        double pixelcoinsAnadir = tipoCambio.cambio * cantidad;

        this.jugadoresService.save(jugador.incrementPixelcoinsBy(pixelcoinsAnadir));

        Pixelcoin.publish(new ItemIngresadoEvento(jugador, pixelcoinsAnadir, tipoCambio.name()));
    }
}
