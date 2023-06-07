package es.serversurvival.minecraftserver.empresas.contratar;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import es.serversurvival.minecraftserver.webaction.WebActionType;
import es.serversurvival.minecraftserver.webaction.WebActionUrlGenerator;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.AQUA;
import static org.bukkit.ChatColor.GOLD;

@Command(
        value = "empresas contratar",
        explanation = "Contratar a un empleado en una empresa"
)
@AllArgsConstructor
public final class ContratarEmpeladoCommandRunner implements CommandRunnerNonArgs {
    private final WebActionUrlGenerator webActionUrlGenerator;

    @Override
    public void execute(Player player) {
        String url = webActionUrlGenerator.generate(WebActionType.EMPRESAS_CONTRATAR, player.getUniqueId());
        player.sendMessage(GOLD + "Para contratar: " + AQUA + url);
    }
}
