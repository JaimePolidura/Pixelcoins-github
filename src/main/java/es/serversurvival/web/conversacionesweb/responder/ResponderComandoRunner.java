package es.serversurvival.web.conversacionesweb.responder;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.comandos.PixelcoinCommand;
import es.serversurvival.web.conversacionesweb._shared.application.ConversacionesWebService;
import es.serversurvival.web.conversacionesweb._shared.domain.ConversacionWeb;
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
    private final ResponderUseCase responderUseCase;

    public ResponderComandoRunner(){
        this.responderUseCase = new ResponderUseCase();
    }

    @Override
    public void execute(ResponderComando comando, CommandSender sender) {
        String playerName = sender.getName();

        ValidationResult result = ValidatorService.startValidating(comando.mensaje, MaxLength.of(50), NotIncludeCharacters.of('&', '-'))
                .validateAll();

        if(result.isFailed()){
            sender.sendMessage(ChatColor.DARK_RED + result.getMessage());
            return;
        }

        this.responderUseCase.responder(sender.getName(), comando.getMensaje());
    }

}
