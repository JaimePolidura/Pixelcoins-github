package es.serversurvival.comandos.subComandos.empresas;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.comandos.ComandoUtilidades;
import es.serversurvival.mySQL.MySQL;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static es.serversurvival.validaciones.Validaciones.*;
import static org.bukkit.ChatColor.DARK_RED;

@Command(name = "empresas logotipo")
public class LogotipoEmpresas extends ComandoUtilidades implements CommandRunner {
    private final String usoIncorrecto = DARK_RED + "Uso incorrecto: /empresas logotipo <empresa>";

    @Override
    public void execute(CommandSender sender, String[] args) {
        MySQL.conectar();

        Player player = (Player) sender;
        String itemTipo = player.getInventory().getItemInMainHand().getType().toString();

        ValidationResult result = ValidationsService.startValidating(args.length == 2, True.of(usoIncorrecto))
                .and(itemTipo, NotEqualsIgnoreCase.of("AIR", "Tienes que tener un item en la mano"))
                .andMayThrowException(() -> args[1], usoIncorrecto, OwnerDeEmpresa.of(player.getName()))
                .validateAll();

        if(result.isFailed())
            player.sendMessage(ChatColor.DARK_RED + result.getMessage());
        else
            empresasMySQL.cambiarIcono(args[1], player, itemTipo);

        MySQL.desconectar();
    }
}
