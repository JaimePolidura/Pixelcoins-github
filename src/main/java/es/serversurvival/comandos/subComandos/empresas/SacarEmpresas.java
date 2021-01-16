package es.serversurvival.comandos.subComandos.empresas;

import es.serversurvival.mySQL.Empresas;
import es.serversurvival.mySQL.MySQL;
import es.serversurvival.util.Funciones;
import es.serversurvival.mySQL.Transacciones;
import es.serversurvival.mySQL.tablasObjetos.Empresa;
import es.serversurvival.validaciones.Validaciones;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.function.Supplier;

import static es.serversurvival.validaciones.Validaciones.*;

public class SacarEmpresas extends EmpresasSubCommand {
    private final String SCNombre = "sacar";
    private final String sintaxis = "/empresas sacar <empresa> <pixelcoins>";
    private final String ayuda = "Sacar determinado numero de pixelcoins de tu empresa";

    public String getSCNombre() {
        return SCNombre;
    }

    public String getSintaxis() {
        return sintaxis;
    }

    public String getAyuda() {
        return ayuda;
    }

    public void execute(Player player, String[] args) {
        MySQL.conectar();

        ValidationResult result = ValidationsService.startValidating(args.length == 3, True.of(mensajeUsoIncorrecto()))
                .andMayThrowException(() -> args[1], mensajeUsoIncorrecto(), OwnerDeEmpresa.of(player.getName()))
                .andMayThrowException(() -> args[2], mensajeUsoIncorrecto(), PositiveNumber)
                .and(suficientesPixelcoinsPredicado(() -> args[1], () -> args[2]), True.of("No puedes sacar mas pixelcoins de la empresa de las que tiene"))
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(ChatColor.DARK_RED + result.getMessage());
            MySQL.desconectar();
            return;
        }

        double pixelcoinsASacar = Double.parseDouble(args[2]);

        transaccionesMySQL.sacarPixelcoinsEmpresa(player, pixelcoinsASacar, args[1]);
        MySQL.desconectar();
    }

    private boolean suficientesPixelcoinsPredicado (Supplier<String> empresaSupplier, Supplier<String> pixelcoins) {
        try{
            Empresa empresa = Empresas.INSTANCE.getEmpresa(empresaSupplier.get());

            return empresa.getPixelcoins() >= Double.parseDouble(pixelcoins.get());
        }catch (Exception e) {
            return false;
        }
    }
}
