package es.serversurvival.comandos.comandos;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.comandos.PixelcoinCommand;
import es.serversurvival.mySQL.tablasObjetos.Empresa;
import main.ValidationResult;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static es.serversurvival.validaciones.Validaciones.*;
import static main.ValidationsService.*;
import static org.bukkit.ChatColor.DARK_RED;

@Command(name = "comprar")
public class Comprar extends PixelcoinCommand implements CommandRunner {
    private final String usoIncorrecto = ChatColor.DARK_RED + "Uso incorrecto /comprar <empresa> <precio>";

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        ValidationResult result = startValidating(args, NotNull.message(ChatColor.DARK_RED + "Uso incorrecto /comprar"))
                .and(args.length, Same.as(2, usoIncorrecto))
                .andMayThrowException(() -> args[1], usoIncorrecto, PositiveNumber, SuficientesPixelcoins.of(sender.getName()))
                .validateAll();

        if(result.isFailed()){
            sender.sendMessage(ChatColor.DARK_RED + result.getMessage());
            return;
        }
        Empresa empresaAComprar = empresasMySQL.getEmpresa(args[0]);
        if (empresaAComprar == null) { //TODO
            player.sendMessage(DARK_RED + "Esa empresa no existe");
            return;
        }
        if (empresaAComprar.getOwner().equalsIgnoreCase(player.getName())) {
            player.sendMessage(DARK_RED + "No puedes comprar un servivio de tu propia empresa");
            return;
        }

        transaccionesMySQL.comprarServivio(args[0], Double.parseDouble(args[1]), (Player) sender);
    }
}
