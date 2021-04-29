package es.serversurvival.bolsa.comprarcorto;

import es.jaime.EventListener;
import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.bolsa.posicionesabiertas.comprarcorto.PosicionCompraCortoEvento;
import es.serversurvival.bolsa.posicionesabiertas.mysql.PosicionAbierta;
import es.serversurvival.bolsa.posicionescerradas.mysql.TipoPosicion;
import es.serversurvival.shared.comandos.PixelcoinCommand;
import es.serversurvival.bolsa.ordenespremarket.abrirorden.AbrirOrdenUseCase;
import es.serversurvival.bolsa.posicionesabiertas.comprarcorto.ComprarCortoUseCase;
import es.serversurvival.utils.Funciones;
import es.serversurvival.utils.validaciones.Validaciones;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.*;

@Command("bolsa comprarcorto")
public class ComprarCortoComando extends PixelcoinCommand implements CommandRunner{
    private final String usoIncorrecto = DARK_RED + "Uso incorrecto: /bolsa comprarcorto <id> <cantidad>";
    private final AbrirOrdenUseCase abrirOrdenUseCase = AbrirOrdenUseCase.INSTANCE;
    private final ComprarCortoUseCase comprarCortoUseCase = ComprarCortoUseCase.INSTANCE;

    @Override
    public void execute(CommandSender player, String[] args) {
        //TODO: Cuando args[1] es 1 no funciona pero con el resto de valores funciona perfectamente, Revisar
        ValidationResult result = ValidationsService.startValidating(args.length, Validaciones.Same.as(3, usoIncorrecto))
                .andMayThrowException(() -> args[1], usoIncorrecto, Validaciones.OwnerPosicionAbierta.de(player.getName(), TipoPosicion.CORTO))
                .andMayThrowException(() -> args[2], usoIncorrecto, Validaciones.NaturalNumber)
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(DARK_RED + result.getMessage());
            return;
        }

        int id = Integer.parseInt(args[1]);
        int cantidad = Integer.parseInt(args[2]);
        PosicionAbierta posicionAComprar = posicionesAbiertasMySQL.getPosicionAbierta(id);

        if(cantidad > posicionAComprar.getCantidad()) {
            cantidad = posicionAComprar.getCantidad();
        }

        if(Funciones.mercadoEstaAbierto()){
            comprarCortoUseCase.comprarPosicionCorto(posicionAComprar, cantidad, player.getName());
        }else{
            abrirOrdenUseCase.abrirOrdenCompraLargo(player.getName(), args[1], cantidad);
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
