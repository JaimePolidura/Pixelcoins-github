package es.serversurvival.minecraftserver.deudas.prestar;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import es.serversurvival.minecraftserver._shared.MinecraftUtils;
import es.serversurvival.minecraftserver._shared.menus.MenuItems;
import es.serversurvival.minecraftserver.webaction.WebActionType;
import es.serversurvival.minecraftserver.webaction.WebActionUrlGenerator;
import lombok.AllArgsConstructor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
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
        String url = webActionUrlGenerator.generate(WebActionType.DEUDAS_PRESTAR , player.getUniqueId());

        MinecraftUtils.enviarUrl(player, url, "Haz click aqui para prestar dinero");
    }
}
