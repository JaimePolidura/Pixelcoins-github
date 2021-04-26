package es.serversurvival.nfs.bolsa.vendercorto;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.legacy.comandos.PixelcoinCommand;
import es.serversurvival.nfs.bolsa.ordenespremarket.abrirorden.AbrirOrdenUseCase;
import es.serversurvival.nfs.bolsa.posicionesabiertas.mysql.PosicionesAbiertas;
import es.serversurvival.nfs.bolsa.posicionesabiertas.vendercorto.VenderCortoUseCase;
import es.serversurvival.nfs.jugadores.mySQL.Jugador;
import es.serversurvival.nfs.utils.Funciones;
import es.serversurvival.nfs.utils.validaciones.Validaciones;
import javafx.util.Pair;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

import static org.bukkit.ChatColor.*;

@Command("bolsa vendercorto")
public class VenderCortoComando extends PixelcoinCommand implements CommandRunner {
    private final String usoIncorrecto = DARK_RED + "Uso incorrecto: /bolsa vendercorto <ticker> <nº acciones>";
    private final VenderCortoUseCase venderCortoUseCase = VenderCortoUseCase.INSTANCE;
    private final AbrirOrdenUseCase abrirOrdenUseCase = AbrirOrdenUseCase.INSTANCE;

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
                venderCortoUseCase.venderEnCortoBolsa(player.getName(), ticker, nombreValor, numeroAccionesAVender, precioAccion);
            }else{
                abrirOrdenUseCase.abrirOrdenCompraLargo(player.getName(), ticker, numeroAccionesAVender);
            }

        });
    }
}
