package es.serversurvival.comandos.subComandos.deudas;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.comandos.PixelcoinCommand;
import es.serversurvival.mySQL.Deudas;
import es.serversurvival.mySQL.MySQL;
import es.serversurvival.mySQL.tablasObjetos.Deuda;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.function.Supplier;

import static es.serversurvival.validaciones.Validaciones.*;
import static org.bukkit.ChatColor.*;

@Command(name = "deudas pagar")
public class PagarDeudaDeudas extends PixelcoinCommand implements CommandRunner {
    private final String usoIncorrecto = DARK_RED + "Uso incorrecto: /deudas pagar <id>";

    @Override
    public void execute(CommandSender player, String[] args) {
        MySQL.conectar();

        Supplier<String> supplierPixelcoins = () -> String.valueOf(getDeuda(() -> args[1]).getPixelcoins_restantes());

        ValidationResult result = ValidationsService.startValidating(args.length, Same.as(2, usoIncorrecto))
                .andMayThrowException(() -> args[1], usoIncorrecto, NaturalNumber)
                .and(esDeudor(() -> args[1], player.getName()), True.of("No eres deudor de esa deuda"))
                .andMayThrowException(supplierPixelcoins, usoIncorrecto, SuficientesPixelcoins.of(player.getName(), "No tienes las suficientes pixelcoins"))
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(DARK_RED + result.getMessage());
            MySQL.desconectar();
            return;
        }

        deudasMySQL.pagarDeuda((Player) player, Integer.parseInt(args[1]));
        MySQL.desconectar();
    }

    private boolean esDeudor (Supplier<String> idSupplier, String jugador) {
        try{
            int id = Integer.parseInt(idSupplier.get());

            return Deudas.INSTANCE.esDeudorDeDeuda(id, jugador);
        }catch (Exception e) {
            return false;
        }
    }

    private Deuda getDeuda (Supplier<? extends String> supplierId) {
        try{
            int id = Integer.parseInt(supplierId.get());

            return Deudas.INSTANCE.getDeuda(id);
        }catch (Exception e) {
            return null;
        }
    }
}
