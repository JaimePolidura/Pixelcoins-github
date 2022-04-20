package es.serversurvival.mensajes.ver;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.jaimetruman.commands.CommandRunnerNonArgs;
import es.serversurvival.mensajes._shared.mysql.Mensaje;
import es.serversurvival._shared.comandos.PixelcoinCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@Command("mensajes")
public class MensajesComando extends PixelcoinCommand implements CommandRunnerNonArgs {
    private final VerMensajesUseCase useCase = VerMensajesUseCase.INSTANCE;

    @Override
    public void execute(CommandSender sender) {
        Player player = (Player) sender;

        List<Mensaje> mensajes = useCase.getMensajes(sender.getName());

        if (mensajes == null || mensajes.size() == 0) {
            player.sendMessage(ChatColor.DARK_RED + "No tienes ningun mensaje pendiente");
            return;
        }

        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "   MENSAJES:");
        for (int i = 0; i < mensajes.size(); i++) {
            player.sendMessage(ChatColor.GOLD + "" + (i + 1) + " " + mensajes.get(i).getMensaje());
        }
    }
}
