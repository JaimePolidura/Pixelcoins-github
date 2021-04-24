package es.serversurvival.legacy.comandos.comandos;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.legacy.comandos.PixelcoinCommand;
import es.serversurvival.legacy.validaciones.Validaciones;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@Command(name = "vender")
public class Vender extends PixelcoinCommand implements CommandRunner {

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

        ofertasMySQL.crearOferta(itemMano, player, Double.parseDouble(args[0]));

    }

    private boolean haSidoComprado (ItemStack item) {
        List<String> lore = item.getItemMeta().getLore();

        if(lore == null || lore.size() == 0)
            return false;

        return lore.get(0).equalsIgnoreCase("Comprado en la tienda");
    }
}
