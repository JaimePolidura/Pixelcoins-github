package es.serversurvival.tienda.vender;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival._shared.comandos.PixelcoinCommand;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import es.serversurvival._shared.utils.Funciones;
import main.ValidationResult;
import main.ValidatorService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static es.serversurvival._shared.utils.validaciones.Validaciones.*;

@Command(
        value = "tienda vender",
        args = {"precio"},
        explanation = "Subir a la tienda el objeto que tengas en la mano, para retirarlo /tienda ver y dar click"
)
public class VenderCommandRunner extends PixelcoinCommand implements CommandRunnerArgs<VenderCommando> {
    private final VenderTiendaUseCase venderTiendaUseCase;

    public VenderCommandRunner(){
        this.venderTiendaUseCase = new VenderTiendaUseCase();
    }

    @Override
    public void execute(VenderCommando comando, CommandSender sender) {
        Player player = (Player) sender;
        ItemStack itemMano = player.getInventory().getItemInMainHand();

        Inventory inventarioJugador = player.getInventory();

        this.venderTiendaUseCase.crearOferta(player.getName(), itemMano, comando.precio);

        inventarioJugador.clear(player.getInventory().getHeldItemSlot());

        Funciones.enviarMensajeYSonido(player, ChatColor.GOLD + "Se ha añadido a la tienda. Para retirarlos /tienda y clikc izquierdo en ellos", Sound.ENTITY_VILLAGER_YES);
        Bukkit.getServer().broadcastMessage(ChatColor.GOLD + player.getName() + " ha añadido un objeto a la tienda por: " +
                ChatColor.GREEN + AllMySQLTablesInstances.formatea.format(comando.getPrecio()) + " PC " + ChatColor.AQUA + "/tienda");
    }
}
