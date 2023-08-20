package es.serversurvival.minecraftserver.config.editar;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseBus;
import es.serversurvival.pixelcoins.config.editar.EditarConfigurationParametros;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import static es.serversurvival.minecraftserver._shared.MinecraftUtils.*;

@Command(
        value = "config editar",
        args = {"key", "nuevoValor"},
        needsOp = true
)
@RequiredArgsConstructor
public final class EditarConfiguracionCommandRunner implements CommandRunnerArgs<EditarConfiguracionComando> {
    private final UseCaseBus useCaseBus;

    @Override
    public void execute(EditarConfiguracionComando comando, Player player) {
        useCaseBus.handle(EditarConfigurationParametros.builder()
                .jugadorId(player.getUniqueId())
                .nuevoValor(comando.getNuevoValor())
                .key(comando.getKey())
                .build());

        enviarMensajeYSonido(player,
                ChatColor.GOLD + "Has editado " + comando.getKey() + " a " + comando.getNuevoValor(),
                Sound.ENTITY_PLAYER_LEVELUP);
    }
}
