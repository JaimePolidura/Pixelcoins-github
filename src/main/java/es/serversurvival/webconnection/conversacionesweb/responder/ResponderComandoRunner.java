package es.serversurvival.webconnection.conversacionesweb.responder;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival._shared.comandos.PixelcoinCommand;
import es.serversurvival.webconnection.conversacionesweb.mysql.ConversacionesWeb;
import main.ValidationResult;
import main.ValidatorService;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static es.serversurvival._shared.utils.validaciones.Validaciones.*;

@Command(
        value = "re",
        args = {"...mensaje"},
        explanation = "enviar un mensaje al usuario que este en la web"
)
public class ResponderComandoRunner extends PixelcoinCommand implements CommandRunnerArgs<ResponderComando> {

    @Override
    public void execute(ResponderComando comando, CommandSender sender) {
        String playerName = sender.getName();

        ValidationResult result = ValidatorService.startValidating(comando.mensaje, MaxLength.of(50), NotIncludeCharacters.of('&', '-'))
                .and(existeConversacion(sender.getName()), True.of("No hay ninguna conversacion pendiente"))
                .validateAll();

        if(result.isFailed()){
            sender.sendMessage(ChatColor.DARK_RED + result.getMessage());
            return;
        }

        conversacionesWebMySQL.nuevoMensaje(conversacionesWebMySQL.getConversacionServer(playerName), (Player) sender, comando.getMensaje());

    }

    private boolean existeConversacion (String jugador) {
        return ConversacionesWeb.INSTANCE.getConversacionServer(jugador) != null;
    }

}
