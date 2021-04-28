package es.serversurvival.tienda.vender;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.shared.comandos.PixelcoinCommand;
import es.serversurvival.utils.validaciones.Validaciones;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.utils.Funciones;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Command("vender")
public class VenderCommand extends PixelcoinCommand implements CommandRunner {
    private final VenderTiendaUseCase useCase = VenderTiendaUseCase.INSTANCE;

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        String nombreItemMano = player.getInventory().getItemInMainHand().getType().toString();
        ItemStack itemMano = player.getInventory().getItemInMainHand();

        ValidationResult result = ValidationsService.startValidating(args.length, Validaciones.Same.as(1))
                .and(nombreItemMano, Validaciones.NotEqualsIgnoreCase.of("AIR", "Tienes que tener un objeto en la mano"), Validaciones.ItemNotBaneadoTienda)
                .andMayThrowException(() -> args[0], "Uso incorrecto " + "/vender <precio>", Validaciones.PositiveNumber)
                .and(itemMano, Validaciones.NoHaSidoCompradoItem)
                .and(player.getName(), Validaciones.SuficientesEspaciosTienda)
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(ChatColor.DARK_RED + result.getMessage());
            return;
        }

        double precio = Double.parseDouble(args[0]);
        Inventory inventarioJugador = player.getInventory();

        useCase.crearOferta(itemMano, player, precio);

        inventarioJugador.clear(player.getInventory().getHeldItemSlot());

        Funciones.enviarMensajeYSonido(player, ChatColor.GOLD + "Se ha añadido a la tienda. Para retirarlos /tienda y clikc izquierdo en ellos", Sound.ENTITY_VILLAGER_YES);
        Bukkit.getServer().broadcastMessage(ChatColor.GOLD + player.getName() + " ha añadido un objeto a la tienda por: " +
                ChatColor.GREEN + AllMySQLTablesInstances.formatea.format(precio) + " PC " + ChatColor.AQUA + "/tienda");
    }

    private boolean haSidoComprado (ItemStack item) {
        List<String> lore = item.getItemMeta().getLore();

        if(lore == null || lore.size() == 0)
            return false;

        return lore.get(0).equalsIgnoreCase("Comprado en la tienda");
    }
}
