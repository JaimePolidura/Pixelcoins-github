package es.serversurvival.webconnection.conversacionesweb.responder;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival._shared.comandos.PixelcoinCommand;
import es.serversurvival.webconnection.conversacionesweb.mysql.ConversacionesWeb;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival._shared.utils.validaciones.Validaciones;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command("re")
public class ResponderComando extends PixelcoinCommand implements CommandRunner {
    @Override
    public void execute(CommandSender sender, String[] args) {
        String playerName = sender.getName();

        ValidationResult result = ValidationsService.startValidating(args.length, Validaciones.Different.of(0,
                "Tiene que tener como minomo 2 palabras"))
                .and(Funciones.buildStringFromArray(args), Validaciones.MaxLength.of(50), Validaciones.NotIncludeCharacters.of('&', '-'))
                .and(existeConversacion(sender.getName()), Validaciones.True.of("No hay ninguna conversacion pendiente"))
                .validateAll();

        if(result.isFailed()){
            sender.sendMessage(ChatColor.DARK_RED + result.getMessage());
            return;
        }

        conversacionesWebMySQL.nuevoMensaje(conversacionesWebMySQL.getConversacionServer(playerName), (Player) sender,
                Funciones.buildStringFromArray(args));
    }

    private boolean existeConversacion (String jugador) {
        return ConversacionesWeb.INSTANCE.getConversacionServer(jugador) != null;
    }
}
