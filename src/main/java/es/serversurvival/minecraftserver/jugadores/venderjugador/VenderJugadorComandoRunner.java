package es.serversurvival.minecraftserver.jugadores.venderjugador;

import es.bukkitbettermenus.MenuService;
import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.jaime.javaddd.domain.exceptions.IllegalState;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival._shared.utils.Funciones;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.GOLD;

@Command(
        value = "venderjugador",
        args = {"comprador", "pixelcoins"},
        explanation = "Vender a un nombreAccionista el item que tengas en la mano"
)
@AllArgsConstructor
public class VenderJugadorComandoRunner implements CommandRunnerArgs<VenderJugadorComando> {
    private final MenuService menuService;

    @Override
    public void execute(VenderJugadorComando venderJugadorComando, Player player) {
        Player comprador = venderJugadorComando.getComprador();
        double pixelcoins = venderJugadorComando.getPixelcoins();

        if(player == null || !player.isOnline())
            throw new ResourceNotFound("El nombreAccionista no esta online");
        if(Funciones.getEspaciosOcupados(comprador.getInventory()) == 36)
            throw new IllegalState("El nombreAccionista a vender, tiene el inventario lleno");
        if(player.getInventory().getItemInMainHand() == null || player.getInventory().getItemInMainHand().getType() == Material.AIR)
            throw new IllegalState("Tiens que tener un objeto en la mano");

        this.menuService.open(comprador, VenderJugadorConfirmacionMenu.class,  new VenderJugadorConfirmacionMenuState(
                comprador, player, player.getInventory().getItemInMainHand(), player.getInventory().getHeldItemSlot(), pixelcoins
        ));

        player.sendMessage(GOLD + "Has enviado la solicitud");
    }
}
