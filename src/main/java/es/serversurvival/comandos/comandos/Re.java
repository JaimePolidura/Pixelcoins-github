package es.serversurvival.comandos.comandos;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.comandos.ComandoUtilidades;
import es.serversurvival.mySQL.ConversacionesWeb;
import es.serversurvival.util.Funciones;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static es.serversurvival.validaciones.Validaciones.*;

@Command(name = "re")
public class Re extends ComandoUtilidades implements CommandRunner {
    @Override
    public void execute(CommandSender sender, String[] args) {
        conversacionesWebMySQL.conectar();

        String playerName = sender.getName();

        ValidationResult result = ValidationsService.startValidating(args.length, Different.of(0,
                "Tiene que tener como minomo 2 palabras"))
                .and(Funciones.buildStringFromArray(args), MaxLength.of(50), NotIncludeCharacters.of('&', '-'))
                .and(existeConversacion(sender.getName()), True.of("No hay ninguna conversacion pendiente"))
                .validateAll();

        if(result.isFailed()){
            sender.sendMessage(ChatColor.DARK_RED + result.getMessage());
        }else{
            conversacionesWebMySQL.nuevoMensaje(conversacionesWebMySQL.getConversacionServer(playerName), (Player) sender, Funciones.buildStringFromArray(args));
        }

        conversacionesWebMySQL.desconectar();
    }

    private boolean existeConversacion (String jugador) {
        return ConversacionesWeb.INSTANCE.getConversacionServer(jugador) != null;
    }
}
