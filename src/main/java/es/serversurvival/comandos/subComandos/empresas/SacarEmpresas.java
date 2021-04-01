package es.serversurvival.comandos.subComandos.empresas;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.comandos.PixelcoinCommand;
import es.serversurvival.mySQL.Empresas;
import es.serversurvival.mySQL.MySQL;
import es.serversurvival.mySQL.tablasObjetos.Empresa;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.function.Supplier;

import static es.serversurvival.validaciones.Validaciones.*;
import static org.bukkit.ChatColor.DARK_RED;

@Command(name = "empresas sacar")
public class SacarEmpresas extends PixelcoinCommand implements CommandRunner {
    private final String usoIncorrecto = DARK_RED + "Uso incorrecto: /empresas sacar <empresa> <pixelcoins>";

    @Override
    public void execute(CommandSender player, String[] args) {
        MySQL.conectar();

        ValidationResult result = ValidationsService.startValidating(args.length == 3, True.of(usoIncorrecto))
                .andMayThrowException(() -> args[1], usoIncorrecto, OwnerDeEmpresa.of(player.getName()))
                .andMayThrowException(() -> args[2], usoIncorrecto, PositiveNumber)
                .and(suficientesPixelcoinsPredicado(() -> args[1], () -> args[2]), True.of("No puedes sacar mas pixelcoins de la empresa de las que tiene"))
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(ChatColor.DARK_RED + result.getMessage());
        }else{
            double pixelcoinsASacar = Double.parseDouble(args[2]);
            transaccionesMySQL.sacarPixelcoinsEmpresa((Player) player, pixelcoinsASacar, args[1]);
        }

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
