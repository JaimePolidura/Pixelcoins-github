package es.serversurvival.minecraftserver.jugadores.tpa.tpa;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival.minecraftserver.jugadores.tpa.TpaTokensService;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.*;

@Command(value = "tpa", args = {"player"})
@RequiredArgsConstructor
public final class TpaCommandRunner implements CommandRunnerArgs<TpaCommand> {
    private final TpaTokensService tpaTokensService;

    @Override
    public void execute(TpaCommand tpaCommand, Player player) {
        TextComponent textComponent = new TextComponent(
                GOLD + player.getName() + " quiere teletransportarse a ti. Haz click " + AQUA +  BOLD +
                        UNDERLINE + "AQUI" + RESET + GOLD + " para teletransportarte"
        );
        var token = tpaTokensService.crear(player, tpaCommand.getPlayer());
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaaccpet " + token));

        player.sendMessage(GOLD + "Has enviado la solicitud");

        tpaCommand.getPlayer().spigot().sendMessage(textComponent);
    }
}
