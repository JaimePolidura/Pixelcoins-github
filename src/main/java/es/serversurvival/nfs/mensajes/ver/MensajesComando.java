package es.serversurvival.nfs.mensajes.ver;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.nfs.shared.comandos.PixelcoinCommand;
import es.serversurvival.nfs.mensajes.mysql.Mensaje;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@Command("mensajes")
public class MensajesComando extends PixelcoinCommand implements CommandRunner {
    private final VerMensajesUseCase useCase = VerMensajesUseCase.INSTANCE;

    @Override
    public void execute(CommandSender sender, String[] strings) {
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
