package es.serversurvival.comandos.subComandos.empresas;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.comandos.PixelcoinCommand;
import es.serversurvival.mySQL.MySQL;
import es.serversurvival.menus.menus.solicitudes.VenderSolicitud;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import static es.serversurvival.validaciones.Validaciones.*;
import static org.bukkit.ChatColor.DARK_RED;

@Command(name = "empresas vender")
public class VenderEmpresas extends PixelcoinCommand implements CommandRunner {
    private final String usoIncorrecto = DARK_RED + "Uso incorrecto: /empresas vender <empresa> <jugador> <precio>";

    @Override
    public void execute(CommandSender player, String[] args) {
        MySQL.conectar();

        ValidationResult result = ValidationsService.startValidating(args.length == 4, True.of(usoIncorrecto))
                .andMayThrowException(() -> args[2], usoIncorrecto, JugadorOnline, NotEqualsIgnoreCase.of(player.getName(), "No te lo puedes vender a ti mismo"))
                .andMayThrowException(() -> args[3], usoIncorrecto, PositiveNumber, SuficientesPixelcoins.of(() -> args[2], "No tiene tantas pixelcoins como crees xdd"))
                .andMayThrowException(() -> args[1], usoIncorrecto, OwnerDeEmpresa.of(player.getName()))
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(ChatColor.DARK_RED + result.getMessage());
        }else{
            VenderSolicitud venderSolicitud = new VenderSolicitud(player.getName(), args[2], args[1], Double.parseDouble(args[3]));
            venderSolicitud.enviarSolicitud();
        }

        MySQL.desconectar();
    }
}
