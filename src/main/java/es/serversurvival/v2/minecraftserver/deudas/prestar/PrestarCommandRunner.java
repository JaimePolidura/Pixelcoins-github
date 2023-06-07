package es.serversurvival.v2.minecraftserver.deudas.prestar;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import es.serversurvival.v2.minecraftserver.webaction.WebActionType;
import es.serversurvival.v2.minecraftserver.webaction.WebActionUrlGenerator;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.*;

@Command(
        value = "deudas prestar",
        explanation = "Prestar pixelcoins a un jugador"
)
@AllArgsConstructor
public final class PrestarCommandRunner implements CommandRunnerNonArgs {
    private final WebActionUrlGenerator webActionUrlGenerator;

    @Override
    public void execute(Player player) {
        String url =  webActionUrlGenerator.generate(WebActionType.DEUDAS_PRESTAR , player.getUniqueId());
        player.sendMessage(GOLD + "Para prestar: " + AQUA + url);
    }
}
