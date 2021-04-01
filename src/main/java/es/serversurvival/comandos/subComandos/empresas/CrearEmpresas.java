package es.serversurvival.comandos.subComandos.empresas;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.comandos.PixelcoinCommand;
import es.serversurvival.mySQL.Empresas;
import es.serversurvival.mySQL.MySQL;
import es.serversurvival.util.Funciones;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static es.serversurvival.validaciones.Validaciones.*;
import static org.bukkit.ChatColor.DARK_RED;

@Command(name = "empresas crear")
public class CrearEmpresas extends PixelcoinCommand implements CommandRunner {
    private final String usoIncorrecto = DARK_RED + "Uso incorrecto: /empresas crear <nombre> <descripccion...>";

    @Override
    public void execute(CommandSender jugadorPlayer, String[] args) {
        MySQL.conectar();

        ValidationResult result = ValidationsService.startValidating(args.length >= 3, True.of(usoIncorrecto))
                .andMayThrowException(() -> args[1], usoIncorrecto, NombreEmpresaNoPillado, MaxLength.of(Empresas.CrearEmpresaNombreLonMax, "El nombre no puede ser tan grande"))
                .andMayThrowException(() -> Funciones.buildStringFromArray(args, 2), usoIncorrecto, MaxLength.of(Empresas.CrearEmpresaDescLonMax, "La descripcion no puede ser tan larga"))
                .and(empresasMySQL.getEmpresasOwner(jugadorPlayer.getName()).size() + 1 <= Empresas.nMaxEmpresas, True.of("No puedes tener tantas empresas"))
                .validateAll();

        if (result.isFailed()){
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + result.getMessage());
            MySQL.desconectar();
            return;
        }

        empresasMySQL.crearEmpresa(args[1], (Player) jugadorPlayer, Funciones.buildStringFromArray(args, 2));

        MySQL.desconectar();
    }
}
