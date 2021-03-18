package es.serversurvival.comandos.subComandos.empresas;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.comandos.ComandoUtilidades;
import es.serversurvival.mySQL.MySQL;
import es.serversurvival.util.Funciones;
import es.serversurvival.mySQL.tablasObjetos.Empresa;
import es.serversurvival.validaciones.Validaciones;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static es.serversurvival.validaciones.Validaciones.*;
import static org.bukkit.ChatColor.DARK_RED;

@Command(name = "empresas depositar")
public class DepositarEmpresas extends ComandoUtilidades implements CommandRunner {
    private final String usoIncorrecto = DARK_RED + "Uso incorrecto: /empresas depositar <empresa> <pixelcoins>";

    @Override
    public void execute(CommandSender player, String[] args) {
        MySQL.conectar();

        ValidationResult result = ValidationsService.startValidating(args.length == 3, True.of(usoIncorrecto))
                .andMayThrowException(() -> args[1], usoIncorrecto, OwnerDeEmpresa.of(player.getName()))
                .andMayThrowException(() -> args[2], usoIncorrecto, PositiveNumber, SuficientesPixelcoins.of(player.getName()))
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(ChatColor.DARK_RED + result.getMessage());
            MySQL.desconectar();
            return;
        }

        transaccionesMySQL.depositarPixelcoinsEmpresa((Player) player, Double.parseDouble(args[2]), args[1]);
        MySQL.desconectar();
    }
}
