package es.serversurvival.bolsa.posicionesabiertas.vendercorto;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.jaime.EventListener;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.activosinfo._shared.domain.ActivoInfo;
import es.serversurvival.bolsa.ordenespremarket._shared.application.OrderExecutorProxy;
import es.serversurvival.bolsa.activosinfo._shared.application.ActivosInfoService;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.TipoActivo;
import es.serversurvival.bolsa.ordenespremarket.abrirorden.AbrirOrdenPremarketCommand;
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
    private final JugadoresService jugadoresService;
    private final ActivosInfoService activoInfoService;
    private final VenderCortoUseCase venderCortoUseCase;

    public VenderCortoComandoRunner(){
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
        this.activoInfoService = DependecyContainer.get(ActivosInfoService.class);
        this.venderCortoUseCase = new VenderCortoUseCase();
    }

    @Override
    public void execute(VenderCortoComando comando, CommandSender player){
        int cantidad = comando.getCantidad();
        String ticker = comando.getTicker();

        ValidationResult result = ValidatorService
                .startValidating(cantidad, NaturalNumber)
                .validateAll();

        if(comando.getCantidad() <= 0){
            player.sendMessage(DARK_RED + result.getMessage());
            return;
        }

        player.sendMessage(RED + "Cargando...");

        ActivoInfo activoInfoAVenderCorto = this.activoInfoService.getByNombreActivo(ticker, TipoActivo.ACCIONES);
        String nombreActivoLargo = activoInfoAVenderCorto.getNombreActivoLargo();
        double precio = activoInfoAVenderCorto.getPrecio();

        if(precio == -1){
            player.sendMessage(DARK_RED + "El nombre que has puesto no existe. Para consultar los tickers: /bolsa valores o en internet");
            return;
        }

        Jugador jugador = this.jugadoresService.getByNombre(player.getName());
        double dineroJugador = jugador.getPixelcoins();
        double valorTotal = precio * cantidad;
        double comision = Funciones.redondeoDecimales(Funciones.reducirPorcentaje(valorTotal, 100 - PORCENTAJE_CORTO), 2);

        if (comision > dineroJugador) {
            player.sendMessage(DARK_RED + "No tienes el dinero suficiente para esa operacion");
            return;
        }

        var executedInMarket = OrderExecutorProxy.execute(AbrirOrdenPremarketCommand.of(player.getName(), ticker, cantidad, TipoAccion.CORTO_VENTA, null), () -> {
            this.venderCortoUseCase.venderEnCortoBolsa(player.getName(), ticker, cantidad);
        });

        if(executedInMarket){
            Funciones.enviarMensajeYSonido((Player) player, GOLD + "Te has puesto corto en " +nombreActivoLargo  + " en " +
                    nombreActivoLargo + " cada una a " + GREEN + FORMATEA.format(precio) + " PC " +
                    GOLD + "Para recomprar las cantidad: /bolsa comprarcorto <id>. /bolsa cartera" + GOLD +
                    "Ademas se te ha cobrado un "+PORCENTAJE_CORTO+"% del valor total de la venta (" + GREEN  + FORMATEA.format(valorTotal)
                    + " PC" + GOLD + ") por lo cual: " + RED + "-" + FORMATEA.format(comision) + " PC", Sound.ENTITY_PLAYER_LEVELUP);

            Bukkit.broadcastMessage(GOLD + player.getName() + " ha vendido en corto " + cantidad + " cantidad en " + nombreActivoLargo);
        }else{
            player.sendMessage(GOLD + "La compra no se ha podida ejecutar por que el mercado esta cerrado, cuando abra se ejecutara");
        }
    }

    @EventListener
    public void onVentaCortoBolsa (PosicionAbiertaEvento evento) {
        if(evento.getTipoPosicion() == TipoPosicion.LARGO) return;

        Player player = Bukkit.getPlayer(evento.getComprador());

        Funciones.enviarMensajeYSonido( player, GOLD + "Te has puesto corto en " + evento.getNombreActivoLargo() + " en " +
                evento.getNombreActivoLargo() + " cada una a " + GREEN + FORMATEA.format(evento.getPrecioUnidad()) + " PC " +
                GOLD + "Para recomprar las cantidad: /bolsa comprarcorto <id>. /bolsa cartera" + GOLD +
                "Ademas se te ha cobrado un 5% del valor total de la venta (" + GREEN  + FORMATEA.format(evento.getPrecioTotal())
                + " PC" + GOLD + ") por lo cual: " + RED + "-" + FORMATEA.format(evento.getPrecioTotal()) + " PC", Sound.ENTITY_PLAYER_LEVELUP);

        Bukkit.broadcastMessage(GOLD + player.getName() + " se ha puesto en corto en " + evento.getNombreActivoLargo());
    }

    @EventListener
    public void on(OrdenAbiertaEvento evento) {
        Player player = Bukkit.getPlayer(evento.getPlayerName());

        Funciones.enviarMensajeYSonido(player, GOLD + "Se ha abierto una orden. Cuando el mercado este abierto se ejecutara. " +
                AQUA + "/bolsa ordenes", ENTITY_PLAYER_LEVELUP);
    }
}
