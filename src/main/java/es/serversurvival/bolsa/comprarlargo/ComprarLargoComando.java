package es.serversurvival.bolsa.comprarlargo;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.Pixelcoin;
import es.serversurvival.bolsa.llamadasapi.mysql.TipoActivo;
import es.serversurvival.bolsa.ordenespremarket.mysql.AccionOrden;
import es.serversurvival.shared.comandos.PixelcoinCommand;
import es.serversurvival.bolsa.ordenespremarket.abrirorden.AbrirOrdenUseCase;
import es.serversurvival.bolsa.posicionesabiertas.comprarlargo.ComprarLargoUseCase;
import es.serversurvival.utils.Funciones;
import es.serversurvival.utils.validaciones.Validaciones;
import javafx.util.Pair;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

import static org.bukkit.ChatColor.*;

@Command("bolsa invertir")
public class ComprarLargoComando extends PixelcoinCommand implements CommandRunner {
    private final String usoIncorrecto = DARK_RED + "Uso incorrecto: /bolsa invertir <ticker Ejmeplo amazon: AMZN> <nAcciones>";

    public void execute(CommandSender player, String[] args) {
        ValidationResult result = ValidationsService.startValidating(args.length, Validaciones.Same.as(3, usoIncorrecto))
                .andMayThrowException(() -> args[2], usoIncorrecto, Validaciones.NaturalNumber)
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(DARK_RED + result.getMessage());
            return;
        }

        int nAccinesAComprar = Integer.parseInt(args[2]);
        String ticker = args[1];

        Bukkit.getScheduler().runTask(Pixelcoin.getInstance(), () -> {
            player.sendMessage(RED + "Cargando...");

            Optional<Pair<String, Double>> valorOpcional = llamadasApiMySQL.getPairNombreValorPrecio(ticker);

            if(!valorOpcional.isPresent()){
                player.sendMessage(DARK_RED + "Ticker no encontrado, los tickers se ven en /bolsa valores o en inernet como en es.investing.com. Solo se puede invertir en acciones que cotizen en Estados Unidos");
                return;
            }

            String nombreValor = valorOpcional.get().getKey();
            double precio = valorOpcional.get().getValue();

            if(jugadoresMySQL.getJugador(player.getName()).getPixelcoins() < (precio * nAccinesAComprar)){
                player.sendMessage(DARK_RED + "No tienes las suficientes pixelcoins para pagar " + nAccinesAComprar + " " + ticker + " a " + formatea.format(precio) + " $ -> " + formatea.format(precio * nAccinesAComprar) + " PC");
                return;
            }

            if(Funciones.mercadoNoEstaAbierto()) {
                AbrirOrdenUseCase.INSTANCE.abrirOrden(player.getName(), ticker, nAccinesAComprar, AccionOrden.LARGO_COMPRA);

                Funciones.enviarMensajeYSonido((Player) player, ChatColor.GOLD + "Se ha abierto una orden. Cuando el mercado este abierto se ejecutara. " +
                        ChatColor.AQUA + "/bolsa ordenes", Sound.ENTITY_PLAYER_LEVELUP);
            } else {
                ComprarLargoUseCase.INSTANCE.abrir(TipoActivo.ACCIONES, ticker.toUpperCase(), nombreValor, "acciones", precio, nAccinesAComprar, player.getName());

                Bukkit.broadcastMessage(GOLD + player.getName() + " ha comprado " + nAccinesAComprar + " acciones de "
                        + nombreValor + " a " + GREEN + formatea.format(precio) + "PC");

                Funciones.enviarMensajeYSonido(Bukkit.getPlayer(player.getName()), GOLD + "Has comprado " + formatea.format(nAccinesAComprar)
                        + " acciones a " + GREEN + formatea.format(precio) + " PC" + GOLD + " que es un total de " + GREEN +
                        formatea.format(precio) + " PC " + GOLD + " comandos: " + AQUA + "/bolsa vender /bolsa cartera", Sound.ENTITY_PLAYER_LEVELUP);
            }
        });

        Funciones.POOL.execute(() -> {

        });
    }
}
