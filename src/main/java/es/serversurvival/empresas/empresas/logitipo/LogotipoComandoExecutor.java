package es.serversurvival.empresas.empresas.logitipo;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival._shared.utils.Funciones;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(
        value = "empresas logotipo",
        args = {"empresa"},
        explanation = "Cambiar el logotipo de tu empresa. Para esto selecciona un item en la mano y ejecuta el comando"
)
public class LogotipoComandoExecutor implements CommandRunnerArgs<LogotipoComando> {
    private final EditarLogitpoUseCase useCase;

    public LogotipoComandoExecutor() {
        this.useCase = new EditarLogitpoUseCase();
    }

    @Override
    public void execute(LogotipoComando comando, CommandSender sender) {
        Player player = (Player) sender;
        Material logitpo = player.getInventory().getItemInMainHand().getType();

        this.useCase.cambiar(comando.getEmpresa(), logitpo, player.getName());

        Funciones.enviarMensajeYSonido(player, ChatColor.GOLD + "Has cambiado el logotipo a: " + logitpo, Sound.ENTITY_PLAYER_LEVELUP);
    }
}
