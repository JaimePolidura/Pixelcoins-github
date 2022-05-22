package es.serversurvival.bolsa.posicionesabiertas.vendercorto;

import es.jaime.EventListener;
import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.ordenespremarket._shared.application.OrderExecutorProxy;
import es.serversurvival.bolsa.activosinfo._shared.application.ActivosInfoService;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.SupportedTipoActivo;
import es.serversurvival.bolsa.ordenespremarket.abrirorden.AbrirOrdenPremarketCommand;
import es.serversurvival.bolsa.ordenespremarket.abrirorden.AbrirOrdenUseCase;
import es.serversurvival.bolsa.ordenespremarket.abrirorden.OrdenAbiertaEvento;
import es.serversurvival.bolsa.ordenespremarket._shared.domain.TipoAccion;
import es.serversurvival.bolsa.posicionesabiertas._shared.domain.PosicionAbiertaEvento;
import es.serversurvival.bolsa.posicionescerradas._shared.domain.TipoPosicion;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;
import es.serversurvival._shared.utils.Funciones;
import main.ValidationResult;
import main.ValidatorService;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


import static es.serversurvival._shared.utils.Funciones.FORMATEA;
import static es.serversurvival._shared.utils.validaciones.Validaciones.*;
import static es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesAbiertasSerivce.PORCENTAJE_CORTO;
import static org.bukkit.ChatColor.*;
import static org.bukkit.Sound.ENTITY_PLAYER_LEVELUP;

@Command(
        value = "bolsa vendercorto",
        isAsync = true,
        args = {"ticker", "cantidad"},
        explanation = "Abrir posicion en corto de una accion, se te cobrara una comision <ticker> " +
                "ticker de la accion, solo se pueden empresas americanas, <cantidad> cantidad de accinoes a vender"
)
public class VenderCortoComandoRunner implements CommandRunnerArgs<VenderCortoComando> {
    private final AbrirOrdenUseCase abrirOrdenUseCase;
    private final JugadoresService jugadoresService;
    private final ActivosInfoService activoInfoService;

    public VenderCortoComandoRunner(){
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
        this.abrirOrdenUseCase = new AbrirOrdenUseCase();
        this.activoInfoService = DependecyContainer.get(ActivosInfoService.class);
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

        double precio = this.activoInfoService.getByNombreActivo(ticker, SupportedTipoActivo.ACCIONES).getPrecio();

        if (precio == -1) {
            Jugador jugador = this.jugadoresService.getByNombre(player.getName());
            double dineroJugador = jugador.getPixelcoins();
            double valorTotal = precio * cantidad;
            double comision = Funciones.redondeoDecimales(Funciones.reducirPorcentaje(valorTotal, 100 - PORCENTAJE_CORTO), 2);

            if (comision > dineroJugador) {
                player.sendMessage(DARK_RED + "No tienes el dinero suficiente para esa operacion");
                return;
            }

            OrderExecutorProxy.execute(AbrirOrdenPremarketCommand.of(player.getName(), ticker, cantidad, TipoAccion.CORTO_VENTA, null), () -> {
                abrirOrdenUseCase.abrirOrden(AbrirOrdenPremarketCommand.of(player.getName(), ticker, cantidad, TipoAccion.CORTO_VENTA, null));
            });
        } else {
            player.sendMessage(DARK_RED + "El nombre que has puesto no existe. Para consultar los tickers: /bolsa valores o en internet");
        }
    }

    @EventListener
    public void onVentaCortoBolsa (PosicionAbiertaEvento evento) {
        if(evento.getTipoPosicion() == TipoPosicion.LARGO) return;

        Player player = Bukkit.getPlayer(evento.getComprador());

        Funciones.enviarMensajeYSonido( player, GOLD + "Te has puesto corto en " + evento.getNombreActivo() + " en " +
                evento.getNombreActivo() + " cada una a " + GREEN + FORMATEA.format(evento.getPrecioUnidad()) + " PC " +
                GOLD + "Para recomprar las acciones: /bolsa comprarcorto <id>. /bolsa cartera" + GOLD +
                "Ademas se te ha cobrado un 5% del valor total de la venta (" + GREEN  + FORMATEA.format(evento.getPrecioTotal())
                + " PC" + GOLD + ") por lo cual: " + RED + "-" + FORMATEA.format(evento.getPrecioTotal()) + " PC", Sound.ENTITY_PLAYER_LEVELUP);

        Bukkit.broadcastMessage(GOLD + player.getName() + " se ha puesto en corto en " + evento.getNombreActivo());
    }

    @EventListener
    public void on(OrdenAbiertaEvento evento) {
        Player player = Bukkit.getPlayer(evento.getPlayerName());

        Funciones.enviarMensajeYSonido(player, GOLD + "Se ha abierto una orden. Cuando el mercado este abierto se ejecutara. " +
                AQUA + "/bolsa ordenes", ENTITY_PLAYER_LEVELUP);
    }
}
