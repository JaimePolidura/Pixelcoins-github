package es.serversurvival.bolsa.other.vendercorto;

import es.jaime.EventListener;
import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.ordenespremarket.abrirorden.AbrirOrdenUseCase;
import es.serversurvival.bolsa.ordenespremarket.abrirorden.OrdenAbiertaEvento;
import es.serversurvival.bolsa.ordenespremarket._shared.domain.TipoAccion;
import es.serversurvival.bolsa.posicionesabiertas.old.mysql.PosicionesAbiertas;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;
import es.serversurvival._shared.comandos.PixelcoinCommand;
import es.serversurvival._shared.utils.Funciones;
import main.Pair;
import main.ValidationResult;
import main.ValidatorService;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


import java.util.Optional;

import static es.serversurvival._shared.utils.validaciones.Validaciones.*;
import static org.bukkit.ChatColor.*;
import static org.bukkit.Sound.ENTITY_PLAYER_LEVELUP;

@Command(
        value = "bolsa vendercorto",
        isAsync = true,
        args = {"ticker", "cantidad"},
        explanation = "Abrir posicion en corto de una accion, se te cobrara una comision <ticker> " +
                "ticker de la accion, solo se pueden empresas americanas, <cantidad> cantidad de accinoes a vender"
)
public class VenderCortoComandoRunner extends PixelcoinCommand implements CommandRunnerArgs<VenderCortoComando> {
    private final VenderCortoUseCase venderCortoUseCase = VenderCortoUseCase.INSTANCE;
    private final AbrirOrdenUseCase abrirOrdenUseCase = AbrirOrdenUseCase.INSTANCE;
    private final JugadoresService jugadoresService;

    public VenderCortoComandoRunner(){
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
    }

    @Override
    public void execute(VenderCortoComando comando, CommandSender player){
        int cantidad = comando.getCantidad();
        String ticker = comando.getTicker();

        ValidationResult result = ValidatorService
                .startValidating(cantidad, NaturalNumber)
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(DARK_RED + result.getMessage());
            return;
        }

        Optional<Pair<String, Double>> optionalNombrePrecio = llamadasApiMySQL.getPairNombreValorPrecio(ticker);

        if(!optionalNombrePrecio.isPresent()){
            player.sendMessage(DARK_RED + "El nombre que has puesto no existe. Para consultar los tickers: /bolsa valores o en internet");
            return;
        }
        Jugador jugador = this.jugadoresService.getByNombre(player.getName());
        double dineroJugador = jugador.getPixelcoins();
        double valorTotal = optionalNombrePrecio.get().getValue() * cantidad;
        double comision = Funciones.redondeoDecimales(Funciones.reducirPorcentaje(valorTotal, 100 - PosicionesAbiertas.PORCENTAJE_CORTO), 2);

        if(comision > dineroJugador){
            player.sendMessage(DARK_RED + "No tienes el dinero suficiente para esa operacion");
            return;
        }

        String nombreValor = optionalNombrePrecio.get().getKey();
        double precioAccion = optionalNombrePrecio.get().getValue();

        if(Funciones.mercadoEstaAbierto()){
            venderCortoUseCase.venderEnCortoBolsa(player.getName(), ticker, nombreValor, cantidad, precioAccion);
        }else{
            abrirOrdenUseCase.abrirOrden(player.getName(), ticker, cantidad, TipoAccion.CORTO_VENTA, -1);
        }
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

    @EventListener
    public void on(OrdenAbiertaEvento evento) {
        Player player = Bukkit.getPlayer(evento.getPlayerName());

        Funciones.enviarMensajeYSonido(player, GOLD + "Se ha abierto una orden. Cuando el mercado este abierto se ejecutara. " +
                AQUA + "/bolsa ordenes", ENTITY_PLAYER_LEVELUP);
    }
}
