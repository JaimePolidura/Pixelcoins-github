package es.serversurvival.comandos.comandos;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.comandos.ComandoUtilidades;
import es.serversurvival.mySQL.Ofertas;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static es.serversurvival.validaciones.Validaciones.*;

@Command(name = "vender")
public class Vender extends ComandoUtilidades implements CommandRunner {

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        String nombreItemMano = player.getInventory().getItemInMainHand().getType().toString();
        ItemStack itemMano = player.getInventory().getItemInMainHand();
        
        Ofertas.conectar();

        ValidationResult result = ValidationsService.startValidating(args.length, Same.as(1))
                .and(nombreItemMano, NotEqualsIgnoreCase.of("AIR", "Tienes que tener un objeto en la mano"), ItemNotBaneadoTienda)
                .andMayThrowException(() -> args[0], "Uso incorrecto " + "/vender <precio>", PositiveNumber)
                .and(itemMano, NoHaSidoCompradoItem)
                .and(player.getName(), SuficientesEspaciosTienda)
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(ChatColor.DARK_RED + result.getMessage());
            Ofertas.desconectar();
            return;
        }

        ofertasMySQL.crearOferta(itemMano, player, Double.parseDouble(args[0]));

        Ofertas.desconectar();
    }

    private boolean haSidoComprado (ItemStack item) {
        List<String> lore = item.getItemMeta().getLore();

        if(lore == null || lore.size() == 0)
            return false;

        return lore.get(0).equalsIgnoreCase("Comprado en la tienda");
    }
}
