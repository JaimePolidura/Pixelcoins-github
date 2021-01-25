package es.serversurvival.comandos.subComandos.deudas;

import es.serversurvival.mySQL.Deudas;
import es.serversurvival.mySQL.MySQL;
import es.serversurvival.util.Funciones;
import es.serversurvival.mySQL.tablasObjetos.Deuda;
import es.serversurvival.validaciones.Validaciones;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.function.Supplier;

import static es.serversurvival.validaciones.Validaciones.*;

public class PagarDeudaDeudas extends DeudasSubCommand {
    private String scnombre = "pagar";
    private String sintaxis = "/deudas pagar <id>";
    private String ayuda = "Pagar toda la deuda, la id se ve en /deudas ver";

    public String getSCNombre() {
        return scnombre;
    }

    public String getSintaxis() {
        return sintaxis;
    }

    public String getAyuda() {
        return ayuda;
    }

    public void execute(Player player, String[] args) {
        MySQL.conectar();

        Supplier<String> supplierPixelcoins = () -> String.valueOf(getDeuda(() -> args[1]).getPixelcoins_restantes());

        ValidationResult result = ValidationsService.startValidating(args.length, Same.as(2, mensajeUsoIncorrecto()))
                .andMayThrowException(() -> args[1], mensajeUsoIncorrecto(), NaturalNumber)
                .and(esDeudor(() -> args[1], player.getName()), True.of("No eres deudor de esa deuda"))
                .andMayThrowException(supplierPixelcoins, mensajeUsoIncorrecto(), SuficientesPixelcoins.of(player.getName(), "No tienes las suficientes pixelcoins"))
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(ChatColor.DARK_RED + result.getMessage());
            MySQL.desconectar();
            return;
        }

        deudasMySQL.pagarDeuda(player, Integer.parseInt(args[1]));
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
