package es.serversurvival.bolsa.venderlargo;

import es.jaime.EventListener;
import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.bolsa.llamadasapi.mysql.TipoActivo;
import es.serversurvival.bolsa.ofertasmercadoserver.mysql.TipoOfertante;
import es.serversurvival.bolsa.ordenespremarket.abrirorden.OrdenAbiertaEvento;
import es.serversurvival.bolsa.ordenespremarket.abrirorden.OrdenNoAbiertaEvento;
import es.serversurvival.bolsa.ordenespremarket.mysql.AccionOrden;
import es.serversurvival.bolsa.posicionesabiertas.mysql.PosicionAbierta;
import es.serversurvival.bolsa.posicionesabiertas.venderlargo.VenderLargoUseCase;
import es.serversurvival.bolsa.posicionesabiertas.venderlargo.PosicionVentaLargoEvento;
import es.serversurvival.bolsa.posicionescerradas.mysql.TipoPosicion;
import es.serversurvival.shared.comandos.PixelcoinCommand;
import es.serversurvival.bolsa.ordenespremarket.abrirorden.AbrirOrdenUseCase;
import es.serversurvival.utils.Funciones;
import es.serversurvival.utils.validaciones.Validaciones;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static es.serversurvival.utils.validaciones.Validaciones.*;
import static org.bukkit.ChatColor.*;
import static org.bukkit.Sound.ENTITY_PLAYER_LEVELUP;
import static org.bukkit.Sound.ENTITY_VILLAGER_NO;

@Command("bolsa vender")
public class VenderBolsaComando extends PixelcoinCommand implements CommandRunner {
    private final String mensajeIncorrecto = DARK_RED + "Uso incorrecto: /bolsa vender <id> [numero a vender]";
    private final VenderLargoUseCase venderUseCase = VenderLargoUseCase.INSTANCE;
    private final AbrirOrdenUseCase abrirOrdenUseCase = AbrirOrdenUseCase.INSTANCE;

    @Override
    public void execute(CommandSender player, String[] args) {
        ValidationResult result =ValidationsService.startValidating(args.length != 3 && args.length != 2, False.of(mensajeIncorrecto))
                .andMayThrowException(() -> args[1], mensajeIncorrecto, NaturalNumber, OwnerPosicionAbierta.de(player.getName(), TipoPosicion.LARGO))
                .andIfExists(() -> args[2], NaturalNumber)
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(DARK_RED + result.getMessage());
            return;
        }

        PosicionAbierta posicionAVender = posicionesAbiertasMySQL.getPosicionAbierta(Integer.parseInt(args[1]));

        int cantidad = args.length == 2 ?
                posicionAVender.getCantidad() :
                Integer.parseInt(args[2]);

        if (posicionAVender.getCantidad() < cantidad)
            cantidad = posicionAVender.getCantidad();

        if(posicionAVender.getTipo_activo() == TipoActivo.ACCIONES_SERVER){
            //TODO:
            return;
        }

        if(Funciones.mercadoEstaAbierto()){
            venderUseCase.venderPosicion(posicionAVender, cantidad, player.getName());
        }else{
            abrirOrdenUseCase.abrirOrden(player.getName(), args[1], cantidad, AccionOrden.LARGO_VENTA, posicionAVender.getId());
        }
    }

    @EventListener
    public void onVentaPosicionLargo (PosicionVentaLargoEvento e) {
        String mensajeAEnviarAlJugador;
        if (e.getRentabilidad() <= 0) {
            mensajeAEnviarAlJugador = GOLD + "Has vendido " + formatea.format(e.getCantidad()) + " de " + e.getTicker() + " a " +
                    GREEN + formatea.format(e.getPrecioCierre()) + " PC/Accion " + GOLD + " cuando la compraste a " + GREEN +
                    formatea.format(e.getPrecioApertura()) + " PC/Unidad " + GOLD + " -> " + RED + formatea.format(e.getRentabilidad())
                    + "% : " + formatea.format(Funciones.redondeoDecimales(e.getResultado(), 3)) + " Perdidas PC " + GOLD + " de "
                    + GREEN + formatea.format(e.getValorTotal()) + " PC";

            Bukkit.broadcastMessage(GOLD + e.getVendedor() + " ha alacanzado una rentabilidad del " + RED +
                    formatea.format(Funciones.redondeoDecimales(e.getRentabilidad(), 3)) + "% " + GOLD + "de las acciones de "
                    + e.getNombreValor() + " (" + e.getTicker() + ")");
        } else {
            mensajeAEnviarAlJugador = GOLD + "Has vendido " + formatea.format(e.getCantidad()) + " de " + e.getTicker() + " a " + GREEN
                    + formatea.format(e.getPrecioApertura()) + " PC/Accion " + GOLD + " cuando la compraste a " + GREEN +
                    formatea.format(e.getPrecioApertura()) + " PC/Unidad " + GOLD + " -> " + GREEN + formatea.format(e.getRentabilidad()) + "% : "
                    + formatea.format(Funciones.redondeoDecimales(e.getResultado(), 3)) + " Beneficios PC " + GOLD + " de " + GREEN + formatea.format(e.getValorTotal()) + " PC";

            Bukkit.broadcastMessage(GOLD + e.getVendedor() + " ha alacanzado una rentabilidad del " + GREEN + "+" +
                    formatea.format(Funciones.redondeoDecimales(e.getRentabilidad(), 3)) + "% " + GOLD +
                    "de las acciones de " + e.getNombreValor() + " (" + e.getTicker() + ")");
        }

        Funciones.enviarMensajeYSonido(Bukkit.getPlayer(e.getVendedor()), mensajeAEnviarAlJugador, Sound.ENTITY_PLAYER_LEVELUP);
    }
}
