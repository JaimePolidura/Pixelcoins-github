package es.serversurvival.jugadores.venderjugador;

import es.jaime.javaddd.domain.exceptions.*;
import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerArgs;
import es.jaimetruman.menus.MenuService;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.utils.Funciones;
import main.ValidationResult;
import main.ValidatorService;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static es.serversurvival._shared.utils.validaciones.Validaciones.*;
import static org.bukkit.ChatColor.DARK_RED;
import static org.bukkit.ChatColor.GOLD;

@Command(
        value = "venderjugador",
        args = {"comprador", "pixelcoins"},
        explanation = "Vender a un nombreAccionista el item que tengas en la mano"
)
public class VenderJugadorComandoRunner implements CommandRunnerArgs<VenderJugadorComando> {
    private final MenuService menuService;

    public VenderJugadorComandoRunner() {
        this.menuService = DependecyContainer.get(MenuService.class);
    }

    @Override
    public void execute(VenderJugadorComando venderJugadorComando, CommandSender sender) {
        Player player = (Player) sender;
        Player comprador = venderJugadorComando.getComprador();
        double pixelcoins = venderJugadorComando.getPixelcoins();

        if(player == null || !player.isOnline())
            throw new ResourceNotFound("El nombreAccionista no esta online");
        if(Funciones.getEspaciosOcupados(comprador.getInventory()) == 36)
            throw new IllegalState("El nombreAccionista a vender, tiene el inventario lleno");
        if(player.getInventory().getItemInMainHand() == null || player.getInventory().getItemInMainHand().getType() == Material.AIR)
            throw new IllegalState("Tiens que tener un objeto en la mano");

        this.menuService.open(comprador, new VenderJugadorConfirmacionMenu(
                comprador,
                player,
                player.getInventory().getItemInMainHand(),
                ((Player) sender).getInventory().getHeldItemSlot(),
                pixelcoins
        ));

        sender.sendMessage(GOLD + "Has enviado la solicitud");
    }
}
