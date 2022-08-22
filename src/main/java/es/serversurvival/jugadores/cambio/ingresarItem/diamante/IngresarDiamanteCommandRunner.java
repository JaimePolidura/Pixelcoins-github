package es.serversurvival.jugadores.cambio.ingresarItem.diamante;


import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerNonArgs;
import es.jaimetruman.menus.MenuService;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.jugadores.cambio.ingresarItem.IngresarItemUseCase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Command(
        value = "cambio diamante",
        explanation = "Cambiar un objeto por pixelcoins"
)
public final class IngresarDiamanteCommandRunner implements CommandRunnerNonArgs {
    private final IngresarItemUseCase ingresarItemUseCase;

    public IngresarDiamanteCommandRunner() {
        this.ingresarItemUseCase = new IngresarItemUseCase();
    }

    @Override
    public void execute(CommandSender sender) {
        Player player = Bukkit.getPlayer(sender.getName());
        ItemStack itemEnMano = player.getInventory().getItemInMainHand();

        if(itemEnMano == null || Funciones.noCuincideNombre(itemEnMano.getType().toString(), "DIAMOND", "DIAMOND_BLOCK")){
            player.sendMessage(ChatColor.DARK_RED + "Debes de tener un diamante en la mano o un bloque de diamante");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
            return;
        }

        ingresarItemUseCase.ingresarItem(itemEnMano, player);
    }
}
