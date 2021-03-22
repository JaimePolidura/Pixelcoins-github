package es.serversurvival.comandos.subComandos.bolsa;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.apiHttp.IEXCloud_API;
import es.serversurvival.comandos.ComandoUtilidades;
import es.serversurvival.main.Pixelcoin;
import es.serversurvival.mySQL.MySQL;
import es.serversurvival.util.Funciones;
import javafx.util.Pair;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.swing.text.html.Option;

import java.util.Optional;

import static es.serversurvival.util.Funciones.*;
import static es.serversurvival.validaciones.Validaciones.*;
import static org.bukkit.ChatColor.*;

@Command(name = "bolsa vendercorto")
public class VenderCortoBolsa extends ComandoUtilidades implements CommandRunner {
    private final String usoIncorrecto = DARK_RED + "Uso incorrecto: /bolsa vendercorto <ticker> <nº acciones>";

    @Override
    public void execute(CommandSender player, String[] args) {
        ValidationResult result = ValidationsService.startValidating(args.length, Same.as(3, usoIncorrecto))
                .andMayThrowException(() -> args[2], usoIncorrecto, NaturalNumber)
                .validateAll(); //Validado en el servidor de minecraft

        if(result.isFailed()){
            player.sendMessage(DARK_RED + result.getMessage());
            return;
        }

        int numeroAccionesAVender = Integer.parseInt(args[2]);
        String ticker = args[1];

        POOL.submit( () -> {
            MySQL.conectar();

            Optional<Pair<String, Double>> optionalNombrePrecio = llamadasApiMySQL.getPairNombreValorPrecio(ticker);

            if(!optionalNombrePrecio.isPresent()){
                player.sendMessage(DARK_RED + "El nombre que has puesto no existe. Para consultar los tickers: /bolsa valores o en internet");
                MySQL.desconectar();
                return;
            }

            String nombreValor = optionalNombrePrecio.get().getKey();
            double precioAccion = optionalNombrePrecio.get().getValue();

            if(mercadoEstaAbierto()){
                transaccionesMySQL.venderEnCortoBolsa(player.getName(), ticker, nombreValor, numeroAccionesAVender, precioAccion);
            }else{
                ordenesMySQL.abrirOrdenVentaCorto((Player) player, ticker, Integer.parseInt(args[2]));
            }

            MySQL.desconectar();
        });
    }
}
