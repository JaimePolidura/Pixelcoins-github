package es.serversurvival.bolsa.vendercorto;

import es.jaime.EventListener;
import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.bolsa.posicionesabiertas.mysql.PosicionesAbiertas;
import es.serversurvival.bolsa.posicionesabiertas.vendercorto.PosicionVentaCortoEvento;
import es.serversurvival.jugadores.mySQL.Jugador;
import es.serversurvival.shared.comandos.PixelcoinCommand;
import es.serversurvival.bolsa.ordenespremarket.abrirorden.AbrirOrdenUseCase;
import es.serversurvival.bolsa.posicionesabiertas.vendercorto.VenderCortoUseCase;
import es.serversurvival.utils.Funciones;
import es.serversurvival.utils.validaciones.Validaciones;
import javafx.util.Pair;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
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

                player.sendMessage(GOLD + "Has abierto una orden, se ejecutara cuando el mercado este abierto");
            }
        });
    }

    @EventListener
    public void onVentaCortoBolsa (PosicionVentaCortoEvento evento) {
        Player player = Bukkit.getPlayer(evento.getComprador());

        Funciones.enviarMensajeYSonido( player, GOLD + "Te has puesto corto en " + evento.getNombreValor() + " en " +
                evento.getNombreValor() + " cada una a " + GREEN + formatea.format(evento.getPrecioUnidad()) + " PC " +
                GOLD + "Para recomprar las acciones: /bolsa comprarcorto <id>. /bolsa cartera" + GOLD +
                "Ademas se te ha cobrado un 5% del valor total de la venta (" + GREEN  + formatea.format(evento.getPrecioTotal())
                + " PC" + GOLD + ") por lo cual: " + RED + "-" + formatea.format(evento.getPrecioTotal()) + " PC", Sound.ENTITY_PLAYER_LEVELUP);

        Bukkit.broadcastMessage(GOLD + player.getName() + " se ha puesto en corto en " + evento.getNombreValor());
    }
}
