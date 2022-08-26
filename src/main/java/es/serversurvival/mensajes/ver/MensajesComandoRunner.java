package es.serversurvival.mensajes.ver;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import es.serversurvival.mensajes._shared.domain.Mensaje;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@Command(value = "mensajes", explanation = "Ver todos los mensajes no leidos que tengas")
public class MensajesComandoRunner implements CommandRunnerNonArgs {
    private final VerMensajesUseCase verMensajesUseCase;

    public MensajesComandoRunner(){
        this.verMensajesUseCase = new VerMensajesUseCase();
    }

    @Override
    public void execute(CommandSender sender) {
        Player player = (Player) sender;

        List<Mensaje> mensajes = this.verMensajesUseCase.getMensajes(sender.getName());
        
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
