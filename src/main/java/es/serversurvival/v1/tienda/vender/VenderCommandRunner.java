package es.serversurvival.v1.tienda.vender;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival.v1.mensajes._shared.application.EnviadorMensajes;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static es.serversurvival.v1._shared.utils.Funciones.FORMATEA;

@Command(
        value = "tienda vender",
        args = {"precio"},
        explanation = "Subir a la tienda el objeto que tengas en la mano, para retirarlo /tienda ver y dar click"
)
@AllArgsConstructor
public class VenderCommandRunner implements CommandRunnerArgs<VenderCommando> {
    private final VenderTiendaUseCase venderTiendaUseCase;
    private final EnviadorMensajes enviadorMensajes;

    @Override
    public void execute(VenderCommando comando, CommandSender sender) {
        Player player = (Player) sender;
        ItemStack itemMano = player.getInventory().getItemInMainHand();

        Inventory inventarioJugador = player.getInventory();

        var tiendaObjeto = this.venderTiendaUseCase.crearOferta(player.getName(), itemMano, comando.precio);

        inventarioJugador.clear(player.getInventory().getHeldItemSlot());

        enviadorMensajes.enviarMensajeYSonido(player, ChatColor.GOLD + "Se ha añadido a la tienda. Para retirarlos /tienda y clikc izquierdo en ellos", Sound.ENTITY_VILLAGER_YES);
        Bukkit.getServer().broadcastMessage(ChatColor.GOLD + player.getName() + " ha añadido un objeto a la tienda por: " +
                ChatColor.GREEN + FORMATEA.format(comando.getPrecio()) + " PC " + ChatColor.AQUA + "/tienda");
    }
}
