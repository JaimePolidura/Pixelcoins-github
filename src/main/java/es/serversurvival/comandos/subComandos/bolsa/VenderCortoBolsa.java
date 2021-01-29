package es.serversurvival.comandos.subComandos.bolsa;

import es.serversurvival.apiHttp.IEXCloud_API;
import es.serversurvival.main.Pixelcoin;
import es.serversurvival.mySQL.MySQL;
import es.serversurvival.util.Funciones;
import javafx.util.Pair;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import javax.swing.text.html.Option;

import java.util.Optional;

import static es.serversurvival.util.Funciones.*;
import static es.serversurvival.validaciones.Validaciones.*;

public class VenderCortoBolsa extends BolsaSubCommand{
    private final String scnombre = "vendercorto";
    private final String sintaxis = "/bolsa vendercorto <ticker> <nº acciones>";
    private final String ayuda = "Vender una accion para despues recomprarla. El jugador se le reembolsara la diferencia de precio, es decir ganara cuando el precio de la acicon baje. Se te cobra un 5% del valor total de la venta (precioPorAccion * cantidadDeAcciones) sobre tus ahorros";

    @Override
    public String getSCNombre() {
        return scnombre;
    }

    @Override
    public String getSintaxis() {
        return sintaxis;
    }

    @Override
    public String getAyuda() {
        return ayuda;
    }

    @Override
    public void execute(Player player, String[] args) {
        ValidationResult result = ValidationsService.startValidating(args.length, Same.as(3, mensajeUsoIncorrecto()))
                .andMayThrowException(() -> args[2], mensajeUsoIncorrecto(), NaturalNumber)
                .validateAll(); //Validado en el servidor de minecraft

        if(result.isFailed()){
            player.sendMessage(ChatColor.DARK_RED + result.getMessage());
            return;
        }

        int numeroAccionesAVender = Integer.parseInt(args[2]);
        String ticker = args[1];

        POOL.submit( () -> {
            MySQL.conectar();

            Optional<Pair<String, Double>> optionalNombrePrecio = llamadasApiMySQL.getPairNombreValorPrecio(ticker);

            if(!optionalNombrePrecio.isPresent()){
                player.sendMessage(ChatColor.DARK_RED + "El nombre que has puesto no existe. Para consultar los tickers: /bolsa valores o en internet");
                MySQL.desconectar();
                return;
            }

            String nombreValor = optionalNombrePrecio.get().getKey();
            double precioAccion = optionalNombrePrecio.get().getValue();

            if(mercadoEstaAbierto()){
                transaccionesMySQL.venderEnCortoBolsa(player.getName(), ticker, nombreValor, numeroAccionesAVender, precioAccion);
            }else{
                ordenesMySQL.abrirOrdenVentaCorto(player, ticker, Integer.parseInt(args[2]));
            }

            MySQL.desconectar();
        });
    }
}
