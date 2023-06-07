package es.serversurvival.v2.minecraftserver.empresas.proponerdirector;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import es.serversurvival.v2.minecraftserver.webaction.WebActionType;
import es.serversurvival.v2.minecraftserver.webaction.WebActionUrlGenerator;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@Command(
        value = "empresas nuevodirector",
        explanation = "En una empresa cotizada, un accionista puede proponer un nuevo director a la que los accionistas " +
                "de la empresa podran votar a favor o en contra de la propuesta"
)
@RequiredArgsConstructor
public final class ProponerNuevoDirectorCommandRunner implements CommandRunnerNonArgs {
    private final WebActionUrlGenerator webActionUrlGenerator;

    @Override
    public void execute(Player player) {
        String url = webActionUrlGenerator.generate(WebActionType.EMPRESAS_PROPONER_DIRECTOR, player.getUniqueId());
        player.sendMessage(ChatColor.GOLD + "Para proponer un nuevo director: " + url);
    }
}
