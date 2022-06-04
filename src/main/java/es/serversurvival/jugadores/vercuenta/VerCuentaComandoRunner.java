package es.serversurvival.jugadores.vercuenta;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerNonArgs;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(
        value = "cuentaweb",
        explanation = "Ver tu cuenta de la web"
)
public class VerCuentaComandoRunner implements CommandRunnerNonArgs {
    private final JugadoresService jugadoresService;

    public VerCuentaComandoRunner(){
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
    }

    @Override
    public void execute(CommandSender sender) {
        Player player = (Player) sender;
        Jugador jugador = jugadoresService.getByNombre(player.getName());

        player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
        player.sendMessage("   ");
        player.sendMessage(ChatColor.AQUA + "Tu numero de cuenta: " + ChatColor.BOLD + jugador.getNumeroVerificacionCuenta());
        player.sendMessage(ChatColor.AQUA + "Para registrarse: " + ChatColor.BOLD + "http://serversurvival.ddns.net/registrarse");
        player.sendMessage(ChatColor.AQUA + "Para iniciarSesion: " + ChatColor.BOLD + "http://serversurvival.ddns.net/login");
        player.sendMessage("   ");
        player.sendMessage(ChatColor.AQUA + "Para ver tu perfil: " + ChatColor.BOLD + "http://serversurvival.ddns.net/perfil");
        player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
    }
}
