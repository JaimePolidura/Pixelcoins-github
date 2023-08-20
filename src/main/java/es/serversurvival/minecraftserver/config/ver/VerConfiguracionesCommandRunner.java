package es.serversurvival.minecraftserver.config.ver;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival.pixelcoins.config._shared.application.ConfigurationService;
import es.serversurvival.pixelcoins.config._shared.domain.ConfigurationEntry;
import lombok.AllArgsConstructor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.*;

@Command(
        value = "config ver",
        args = {"[filtroNombre]"},
        needsOp = true
)
@AllArgsConstructor
public final class VerConfiguracionesCommandRunner implements CommandRunnerArgs<VerConfiguracionesComando> {
    private final ConfigurationService configurationService;

    @Override
    public void execute(VerConfiguracionesComando comando, Player player) {
        for (ConfigurationEntry entry : configurationService.findAll()) {
            if(comando.getFiltroNombre() != null &&
                    entry.getKeyy().name().toLowerCase().contains(comando.getFiltroNombre().toLowerCase())) {
                continue;
            }

            String editCommand = "/config editar " + entry.getKeyy();

            TextComponent textComponent = new TextComponent(
                    GOLD + "" + entry.getKeyy() + ": " + AQUA + entry.getValue()
            );
            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, editCommand));

            player.spigot().sendMessage(textComponent);
        }
    }
}
