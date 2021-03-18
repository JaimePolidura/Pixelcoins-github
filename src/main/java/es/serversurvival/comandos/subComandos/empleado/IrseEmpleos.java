package es.serversurvival.comandos.subComandos.empleado;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.comandos.ComandoUtilidades;
import es.serversurvival.mySQL.Empresas;
import es.serversurvival.mySQL.MySQL;
import es.serversurvival.mySQL.tablasObjetos.Empresa;
import es.serversurvival.validaciones.Validaciones;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.function.Supplier;

import static es.serversurvival.validaciones.Validaciones.*;

@Command(name = "empleos irse")
public class IrseEmpleos extends ComandoUtilidades implements CommandRunner {
    private final String usoIncorrecto = ChatColor.DARK_RED + "Uso incorrecto: /empleos irse <empresa>";

    @Override
    public void execute(CommandSender jugadorPlayer, String[] args) {
        MySQL.conectar();

        ValidationResult result = ValidationsService.startValidating(args.length, Same.as(2, usoIncorrecto))
                .andMayThrowException(() -> empresasMySQL.getEmpresa(args[1]) != null, usoIncorrecto, True.of("Esa empresa no exsiste"))
                .and(trabajaEnLaEmpresa(() -> args[1], jugadorPlayer.getName()), True.of("Ese jugador no trabaja en la empresa"))
                .validateAll();

        if(result.isFailed()){
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + result.getMessage());
            MySQL.desconectar();
            return;
        }

        empleadosMySQL.irseEmpresa(args[1], (Player) jugadorPlayer);
        MySQL.desconectar();
    }

    private boolean trabajaEnLaEmpresa (Supplier<String> empresaSupplier, String jugador) {
        try{
            String empresaNombre = empresaSupplier.get();

            return empleadosMySQL.trabajaEmpresa(jugador, empresaNombre);
        }catch (Exception e) {
            return false;
        }
    }
}
