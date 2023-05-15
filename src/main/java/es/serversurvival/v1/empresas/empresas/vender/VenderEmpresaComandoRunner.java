package es.serversurvival.v1.empresas.empresas.vender;

import es.bukkitbettermenus.MenuService;
import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.jaime.javaddd.domain.exceptions.IllegalQuantity;
import es.serversurvival.v1._shared.exceptions.NotEnoughPixelcoins;
import es.serversurvival.v1.jugadores._shared.application.JugadoresService;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import static org.bukkit.ChatColor.*;

@Command(
        value = "empresas vender",
        args = {"empresa", "nombreAccionista", "precio"},
        explanation = "Vender tu empresa a otro nombreAccionista por pixelcoins"
)
@RequiredArgsConstructor
public class VenderEmpresaComandoRunner implements CommandRunnerArgs<VenderEmpresaComando> {
    private final MenuService menuService;
    private final JugadoresService jugadoresService;

    @Override
    public void execute(VenderEmpresaComando comando, CommandSender player) {
        String empresa = comando.getEmpresa();
        String jugadorAVender = comando.getJugador();
        double precio = comando.getPrecio();

        if(precio <= 0)
            throw new IllegalQuantity("El precio tiene que ser un numero positivo");
        if(this.jugadoresService.getByNombre(jugadorAVender).getPixelcoins() < precio)
            throw new NotEnoughPixelcoins("No tiene tantas pixelcoins como crees xdd");

        this.menuService.open(Bukkit.getPlayer(jugadorAVender), VenderEmpresaConfirmacionMenu.class, VenderEmpresaConfirmacionMenuState.fromCommand(player.getName(), comando));

        player.sendMessage(GOLD + "Has enviado la soilcitud");
    }
}
