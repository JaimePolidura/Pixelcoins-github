package es.serversurvival.v1.empresas.empresas.logitipo;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival.v1.mensajes._shared.application.EnviadorMensajes;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class LogotipoComandoRunner implements CommandRunnerArgs<LogotipoComando> {
    private final EnviadorMensajes enviadorMensajes;
    private final EditarLogitpoUseCase useCase;

    @Override
    public void execute(LogotipoComando comando, CommandSender sender) {
        Player player = (Player) sender;
        Material logitpo = player.getInventory().getItemInMainHand().getType();

        this.useCase.cambiar(comando.getEmpresa(), logitpo, player.getName());

        enviadorMensajes.enviarMensajeYSonido(player, ChatColor.GOLD + "Has cambiado el logotipo a: " + logitpo, Sound.ENTITY_PLAYER_LEVELUP);
    }
}
