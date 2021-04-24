package es.serversurvival.legacy.comandos.subComandos.bolsa;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.legacy.comandos.PixelcoinCommand;
import es.serversurvival.legacy.mySQL.PosicionesAbiertas;
import es.serversurvival.legacy.mySQL.tablasObjetos.Jugador;
import es.serversurvival.legacy.util.Funciones;
import es.serversurvival.legacy.validaciones.Validaciones;
import javafx.util.Pair;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

import static org.bukkit.ChatColor.*;

@Command(name = "bolsa vendercorto")
public class VenderCortoBolsa extends PixelcoinCommand implements CommandRunner {
    private final String usoIncorrecto = DARK_RED + "Uso incorrecto: /bolsa vendercorto <ticker> <nº acciones>";

    @Override
    public void execute(CommandSender player, String[] args) {
        ValidationResult result = ValidationsService.startValidating(args.length, Validaciones.Same.as(3, usoIncorrecto))
                .andMayThrowException(() -> args[2], usoIncorrecto, Validaciones.NaturalNumber)
                .validateAll(); //Validado en el servidor de minecraft

        if(result.isFailed()){
            player.sendMessage(DARK_RED + result.getMessage());
            return;
        }

        int numeroAccionesAVender = Integer.parseInt(args[2]);
        String ticker = args[1];

        Funciones.POOL.submit( () -> {

            Optional<Pair<String, Double>> optionalNombrePrecio = llamadasApiMySQL.getPairNombreValorPrecio(ticker);

            if(!optionalNombrePrecio.isPresent()){
                player.sendMessage(DARK_RED + "El nombre que has puesto no existe. Para consultar los tickers: /bolsa valores o en internet");
                return;
            }
            Jugador jugador = jugadoresMySQL.getJugador(player.getName());
            double dineroJugador = jugador.getPixelcoins();
            double valorTotal = optionalNombrePrecio.get().getValue() * numeroAccionesAVender;
            double comision = Funciones.redondeoDecimales(Funciones.reducirPorcentaje(valorTotal, 100 - PosicionesAbiertas.PORCENTAJE_CORTO), 2);

            if(comision > dineroJugador){
                player.sendMessage(DARK_RED + "No tienes el dinero suficiente para esa operacion");
                return;
            }

            String nombreValor = optionalNombrePrecio.get().getKey();
            double precioAccion = optionalNombrePrecio.get().getValue();

            if(Funciones.mercadoEstaAbierto()){
                transaccionesMySQL.venderEnCortoBolsa(player.getName(), ticker, nombreValor, numeroAccionesAVender, precioAccion);
            }else{
                ordenesMySQL.abrirOrdenVentaCorto((Player) player, ticker, Integer.parseInt(args[2]));
            }

        });
    }
}
