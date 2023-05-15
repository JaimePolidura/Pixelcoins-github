package es.serversurvival.v1.empresas.empresas.editarnombre;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival.v1.mensajes._shared.application.EnviadorMensajes;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(
        value = "empresas editarnombre",
        args = {"empresa", "nuevoNombre"},
        explanation = "Cambiar el nombre de tu empresa a otro, el nombre no puede estar cogido"
)
@AllArgsConstructor
public class EditarNombreComandoRunner implements CommandRunnerArgs<EditarNombreComando> {
    private final EnviadorMensajes enviadorMensajes;
    private final EditarNombreUseCase useCase;

    @Override
    public void execute(EditarNombreComando editarNombreComando, CommandSender sender) {
        useCase.edit(editarNombreComando.getEmpresa(), editarNombreComando.getNuevoNombre(), sender.getName());
        
        enviadorMensajes.enviarMensajeYSonido((Player) sender, ChatColor.GOLD + "Has cambiado de nombre a tu empresa!", Sound.ENTITY_PLAYER_LEVELUP);
    }
}
