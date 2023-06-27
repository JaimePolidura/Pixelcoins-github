package es.serversurvival.minecraftserver.deudas.emitir;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import es.serversurvival.minecraftserver._shared.MinecraftUtils;
import es.serversurvival.minecraftserver.webaction.WebActionType;
import es.serversurvival.minecraftserver.webaction.WebActionUrlGenerator;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.AQUA;
import static org.bukkit.ChatColor.GOLD;


@Command(
        value = "deudas emitir",
        explanation = "Poner el en mercado (/deudas mercado) una deuda. Cuando alguien la compre te habra prestado las " +
                "pixelcoins y empezaras a pagarles las cuotas"
)
@AllArgsConstructor
public final class EmitirDeudaCommandRunner implements CommandRunnerNonArgs {
    private final WebActionUrlGenerator webActionUrlGenerator;

    @Override
    public void execute(Player player) {
        String url = webActionUrlGenerator.generate(WebActionType.DEUDAS_EMITIR, player.getUniqueId());

        MinecraftUtils.enviarUrl(player, url, "Haz click aqui para emitir deuda");
    }
}
