package es.serversurvival.minecraftserver.jugadores.tpa.tpaaccpet;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival.minecraftserver.jugadores.tpa.TpaTokensService;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Optional;

@Command(
        value = "tpaaccpet",
        args = {"token"}
)
@RequiredArgsConstructor
public final class TpaAcceptCommandRunner implements CommandRunnerArgs<TpaAcceptCommand> {
    private final TpaTokensService tpaTokensService;

    @Override
    public void execute(TpaAcceptCommand command, Player destino) {
        Optional<TpaTokensService.Token> tokenOptional = tpaTokensService.retrieve(command.getToken());

        if(tokenOptional.isEmpty()) {
            destino.sendMessage(ChatColor.GOLD + "Prueba mas tarde");
            return;
        }

        TpaTokensService.Token token = tokenOptional.get();
        Player playerToBeTeleported = Bukkit.getPlayer(token.getPlayerToBeTeleported());
        if (playerToBeTeleported == null) {
            playerToBeTeleported.sendMessage(ChatColor.DARK_RED + token.getDestino() + " no esta online");
            return;
        }

        playerToBeTeleported.teleport(destino.getLocation());
    }
}
