package es.serversurvival.bolsa.other.comprarcorto;

import es.jaime.EventListener;
import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival.bolsa.other._shared.AbrirOrdenUseCase;
import es.serversurvival.bolsa.other._shared.posicionesabiertas.mysql.PosicionAbierta;
import es.serversurvival.bolsa.other._shared.posicionescerradas.mysql.TipoPosicion;
import es.serversurvival._shared.comandos.PixelcoinCommand;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.bolsa.ordenespremarket._shared.domain.TipoAccion;
import main.ValidationResult;
import main.ValidatorService;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static es.serversurvival._shared.utils.validaciones.Validaciones.*;
import static org.bukkit.ChatColor.*;

@Command(
        value = "bolsa comprarcorto",
        args = {"id", "cantidad"},
        explanation = "Para cerrar una posicion en corto. <id> id de la posicion /bolsa cartera <cantidad> acciones a cerrar de la posicion"
)
public class ComprarCortoComandoRunner extends PixelcoinCommand implements CommandRunnerArgs<ComprarCortoComando> {
    private final AbrirOrdenUseCase abrirOrdenUseCase = AbrirOrdenUseCase.INSTANCE;
    private final ComprarCortoUseCase comprarCortoUseCase = ComprarCortoUseCase.INSTANCE;

    @Override
    public void execute(ComprarCortoComando comando, CommandSender sender) {
        int id = comando.getId();
        int cantidad = comando.getCantidad();

        ValidationResult result = ValidatorService
                .startValidating(comando.getId(), OwnerPosicionAbierta.de(sender.getName(), TipoPosicion.CORTO))
                .and(comando.getCantidad(), NaturalNumber)
                .validateAll();

        if(result.isFailed()){
            sender.sendMessage(DARK_RED + result.getMessage());
            return;
        }

        PosicionAbierta posicionAComprar = posicionesAbiertasMySQL.getPosicionAbierta(id);

        if(cantidad > posicionAComprar.getCantidad()) {
            cantidad = posicionAComprar.getCantidad();
        }

        if(Funciones.mercadoEstaAbierto()){
            comprarCortoUseCase.comprarPosicionCorto(posicionAComprar, cantidad, sender.getName());
        }else{
            abrirOrdenUseCase.abrirOrden(sender.getName(), posicionAComprar.getNombre_activo(), cantidad, TipoAccion.CORTO_COMPRA, posicionAComprar.getId());
        }
    }

    @EventListener
    public void sendMessageOnComplete(PosicionCompraCortoEvento e) {
        String mensaje;
        double revalorizacionTotal =  (e.getPrecioApertura() - e.getPrecioCierre()) * e.getCantidad();

        if (e.getRentabilidad() <= 0)
            mensaje = GOLD + "Has comprado en corto" + formatea.format(e.getCantidad()) + " de " + e.getTicker() + " a " + GREEN + formatea.format(e.getPrecioCierre())
                    + " PC/Accion " + GOLD + " cuando la vendiste a " + GREEN + formatea.format(e.getPrecioCierre()) + " PC/Unidad " + GOLD + " -> " +
                    RED + formatea.format(e.getRentabilidad()) + "% : " + formatea.format(Funciones.redondeoDecimales(revalorizacionTotal, 3)) + " Perdidas PC ";
        else
            mensaje = GOLD + "Has comprado en corto" + formatea.format(e.getCantidad()) + " de " + e.getTicker() + " a " + GREEN + formatea.format(e.getPrecioCierre())
                    + " PC/Accion " + GOLD + " cuando la vendiste a " + GREEN + formatea.format(e.getPrecioApertura()) + " PC/Unidad " + GOLD + " -> " +
                    GREEN + formatea.format(e.getRentabilidad()) + "% : " + formatea.format(Funciones.redondeoDecimales(revalorizacionTotal, 3)) + " Beneficios PC ";

        Player player = Bukkit.getPlayer(e.getVendedor());

        Funciones.enviarMensajeYSonido(player, mensaje, Sound.ENTITY_PLAYER_LEVELUP);

        Bukkit.broadcastMessage(GOLD + player.getName() + " ha alacanzado una rentabilidad del " + GREEN + "+" + formatea.format(Funciones.redondeoDecimales(e.getRentabilidad(), 3)) + "% "
                + GOLD + "de las acciones de " + e.getNombreValor() + " (" + e.getTicker() + "), poniendose en " + BOLD  + "CORTO");
    }

}
