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
        value = "vender",
        args = {"precio"},
        explanation = "Subir a la tienda el objeto que tengas en la mano, para retirarlo /tienda y dar click"
)
public class VenderCommandRunner extends PixelcoinCommand implements CommandRunnerArgs<VenderCommando> {
    private static final String MENSAJE_ITEM_NO_EN_LA_MANO = "Tienes que tener un objeto en la mano";
    private final VenderTiendaUseCase useCase = VenderTiendaUseCase.INSTANCE;

    @Override
    public void execute(VenderCommando comando, CommandSender sender) {
        Player player = (Player) sender;
        String nombreItemMano = player.getInventory().getItemInMainHand().getType().toString();
        ItemStack itemMano = player.getInventory().getItemInMainHand();

        ValidationResult result = ValidatorService.startValidating(nombreItemMano, NotEqualsIgnoreCase.of("AIR", MENSAJE_ITEM_NO_EN_LA_MANO), ItemNotBaneadoTienda)
                .and(comando.getPrecio(), PositiveNumber)
                .and(itemMano, NoHaSidoCompradoItem)
                .and(player.getName(), SuficientesEspaciosTienda)
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(ChatColor.DARK_RED + result.getMessage());
            return;
        }

        Inventory inventarioJugador = player.getInventory();

        useCase.crearOferta(itemMano, player, comando.precio);

        inventarioJugador.clear(player.getInventory().getHeldItemSlot());

        Funciones.enviarMensajeYSonido(player, ChatColor.GOLD + "Se ha añadido a la tienda. Para retirarlos /tienda y clikc izquierdo en ellos", Sound.ENTITY_VILLAGER_YES);
        Bukkit.getServer().broadcastMessage(ChatColor.GOLD + player.getName() + " ha añadido un objeto a la tienda por: " +
                ChatColor.GREEN + AllMySQLTablesInstances.formatea.format(comando.getPrecio()) + " PC " + ChatColor.AQUA + "/tienda");
    }
}
