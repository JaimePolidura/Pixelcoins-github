package es.serversurvival.bolsa.other.venderlargo;

import es.jaime.EventListener;
import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival.bolsa.other._shared.AbrirOrdenUseCase;
import es.serversurvival.bolsa.ordenespremarket._shared.domain.TipoAccion;
import es.serversurvival.bolsa.other._shared.posicionesabiertas.mysql.PosicionAbierta;
import es.serversurvival.bolsa.other._shared.llamadasapi.mysql.TipoActivo;
import es.serversurvival._shared.comandos.PixelcoinCommand;
import es.serversurvival._shared.utils.Funciones;
import main.ValidationResult;
import main.ValidatorService;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;

import static es.serversurvival._shared.utils.validaciones.Validaciones.*;
import static org.bukkit.ChatColor.*;

@Command(
        value = "bolsa vender",
        args = {"id", "[cantidad]¡1!"},
        explanation = "Vender una posicion que tengas en cartera. <id> id de la posicion, para ver /bolsa cartera"
)
public class VenderBolsaComandoExecutor extends PixelcoinCommand implements CommandRunnerArgs<VenderBolsaComando> {
    private final String mensajeIncorrecto = DARK_RED + "Uso incorrecto: /bolsa vender <id> [numero a vender]";
    private final VenderLargoUseCase venderUseCase = VenderLargoUseCase.INSTANCE;
    private final AbrirOrdenUseCase abrirOrdenUseCase = AbrirOrdenUseCase.INSTANCE;

    @Override
    public void execute(VenderBolsaComando comando, CommandSender player) {
        int id = comando.getId();
        int cantidad = comando.getCantidad();

        ValidationResult result = ValidatorService
                .startValidating(id, NaturalNumber, OwnerPosicionAbierta.de(player.getName(), LARGO))
                .and(cantidad, NaturalNumber)
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(DARK_RED + result.getMessage());
            return;
        }

        PosicionAbierta posicionAVender = posicionesAbiertasMySQL.getPosicionAbierta(id);

        if (posicionAVender.getCantidad() < cantidad)
            cantidad = posicionAVender.getCantidad();

        if(posicionAVender.getTipo_activo() == TipoActivo.ACCIONES_SERVER){
            //TODO:
            return;
        }

        if(Funciones.mercadoEstaAbierto()){
            venderUseCase.venderPosicion(posicionAVender, cantidad, player.getName());
        }else{
            abrirOrdenUseCase.abrirOrden(player.getName(), posicionAVender.getNombre_activo(), cantidad, TipoAccion.LARGO_VENTA, posicionAVender.getId());
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
