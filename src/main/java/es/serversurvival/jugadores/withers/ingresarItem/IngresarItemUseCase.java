package es.serversurvival.jugadores.withers.ingresarItem;

import es.serversurvival.Pixelcoin;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;
import es.serversurvival.jugadores.withers.CambioPixelcoins;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class IngresarItemUseCase implements AllMySQLTablesInstances {
    private final JugadoresService jugadoresService;

    public IngresarItemUseCase(){
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
    }

    public void ingresarItem(ItemStack itemAIngresar, Player player) {
        Jugador jugador = jugadoresService.getByNombre(player.getName());

        int cantidad = itemAIngresar.getAmount();
        String nombreItem = itemAIngresar.getType().toString();
        double pixelcoinsAnadir = CambioPixelcoins.getCambioTotal(nombreItem, cantidad);

        this.jugadoresService.save(jugador.incrementPixelcoinsBy(pixelcoinsAnadir));

        player.getInventory().clear(player.getInventory().getHeldItemSlot());

        Pixelcoin.publish(new ItemIngresadoEvento(jugador, pixelcoinsAnadir, nombreItem));
    }
}
