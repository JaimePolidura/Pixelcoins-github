package es.serversurvival.jugadores.cambio.ingresarItem.lapislazuli;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.jugadores.cambio.ingresarItem.IngresarItemUseCase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Command(
        value = "cambio lapis",
        explanation = "Cambiar un objeto por pixelcoins"
)
public final class IngresarLapisCommandRunner implements CommandRunnerNonArgs {
    private final IngresarItemUseCase ingresarItemUseCase;

    public IngresarLapisCommandRunner() {
        this.ingresarItemUseCase = new IngresarItemUseCase();
    }

    @Override
    public void execute(CommandSender sender) {
        Player player = Bukkit.getPlayer(sender.getName());
        ItemStack itemEnMano = player.getInventory().getItemInMainHand();

        if(itemEnMano == null || Funciones.noCuincideNombre(itemEnMano.getType().toString(), "LAPIS_LAZULI", "LAPIS_BLOCK")){
            player.sendMessage(ChatColor.DARK_RED + "Debes de tener lapislazuli en la mano o un bloque de lapislazuli");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
            return;
        }

        ingresarItemUseCase.ingresarItem(itemEnMano, player);
    }
}
