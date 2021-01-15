package es.serversurvival.comandos.subComandos.empleado;

import es.serversurvival.mySQL.Empresas;
import es.serversurvival.mySQL.MySQL;
import es.serversurvival.mySQL.tablasObjetos.Empresa;
import es.serversurvival.validaciones.Validaciones;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.function.Supplier;

import static es.serversurvival.validaciones.Validaciones.*;

public class IrseEmpleos extends EmpleosSubCommand {
    private final String SCNombre = "irse";
    private final String sintaxis = "/empleos irse <empresa>";
    private final String ayuda = "irse de un trabajo";

    public String getSCNombre() {
        return SCNombre;
    }

    public String getSintaxis() {
        return sintaxis;
    }

    public String getAyuda() {
        return ayuda;
    }

    public void execute(Player jugadorPlayer, String[] args) {
        MySQL.conectar();

        ValidationResult result = ValidationsService.startValidating(args.length, Same.as(2, mensajeUsoIncorrecto()))
                .andMayThrowException(() -> empresasMySQL.getEmpresa(args[1]) != null, mensajeUsoIncorrecto(), True.of("Esa empresa no exsiste"))
                .and(trabajaEnLaEmpresa(() -> args[1], jugadorPlayer.getName()), True.of("Ese jugador no trabaja en la empresa"))
                .validateAll();

        if(result.isFailed()){
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + result.getMessage());
            MySQL.desconectar();
            return;
        }

        empleadosMySQL.irseEmpresa(args[1], jugadorPlayer);
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
