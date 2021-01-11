package es.serversurvival.comandos.comandos;

import com.sun.deploy.security.ValidationState;
import es.serversurvival.comandos.Comando;
import es.serversurvival.mySQL.MySQL;
import es.serversurvival.util.Funciones;
import es.serversurvival.mySQL.Ofertas;
import es.serversurvival.validaciones.misValidaciones.NoHaSidoCompradoItem;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static es.serversurvival.validaciones.Validaciones.*;

public class Vender extends Comando {
    private final String CNombre = "vender";
    private final String sintaxis = "/vender <precio>";
    private final String ayuda = "vender objetos en la tienda a un precio, para retirarlos en /tienda";

    public String getCNombre() {
        return CNombre;
    }

    public String getSintaxis() {
        return sintaxis;
    }

    public String getAyuda() {
        return ayuda;
    }

    public void execute(Player player, String[] args) {
        String nombreItemMano = player.getInventory().getItemInMainHand().getType().toString();
        ItemStack itemMano = player.getInventory().getItemInMainHand();
        
        Ofertas.conectar();

        ValidationResult result = ValidationsService.startValidating(args.length, Same.as(1))
                .and(nombreItemMano, NotEqualsIgnoreCase.of("AIR", "Tienes que tener un objeto en la mano"), ItemNotBaneadoTienda)
                .andMayThrowException(() -> args[0], "Uso incorrecto " + this.sintaxis, PositiveNumber)
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
