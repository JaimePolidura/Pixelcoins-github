package es.serversurvival.empresas.empresas.ipo.prepare;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival.empresas.empresas.ipo.IPOCommand;
import main.ValidationResult;
import main.ValidatorService;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static es.serversurvival._shared.utils.Funciones.redondeoDecimales;
import static es.serversurvival._shared.utils.validaciones.Validaciones.*;
import static org.bukkit.ChatColor.*;

@Command(
        value = "empresas ipo",
        args = {"empresa", "accionesTotales", "accionesOwner", "precioPorAccion"},
        explanation = "Puedes sacar tus empresas a la 'bolsa' donde el resto de jugadores podran comprar las acciones. " +
                "Por cada venta de la empresa a jugadores tu empresa recaudara las pixelcoins. Para mas ayuda pregunta al admin"
)
public final class PrepareIPOCommandRunner implements CommandRunnerArgs<IPOCommand> {
    private final String MESSAGE_ON_WRONG_ACCIONES_OWNER = "Tus acciones no pueden ser mayores a las totales";

    @Override
    public void execute(IPOCommand command, CommandSender sender) {
        Player player = (Player) sender;

        ValidationResult validation = ValidatorService.startValidating(command.getEmpresa(), OwnerDeEmpresa)
                .and(command.getAccionesOwner(), NaturalNumber, MaxValue.of(command.getAccionesTotales() - 1, MESSAGE_ON_WRONG_ACCIONES_OWNER))
                .and(command.getPrecioPorAccion(), NaturalNumber, MaxValue.of(1, "Precio maximo 1"))
                .and(command.getAccionesTotales(), NaturalNumber, MaxValue.of(2, "Acciones totales maximo 2"))
                .validateAll();

        if(validation.isFailed()){
            player.sendMessage(DARK_RED + validation.getMessage());
            return;
        }

        double marketCap = command.getAccionesTotales() * command.getPrecioPorAccion();
        double pixelcoinsOwner = command.getAccionesOwner() * command.getPrecioPorAccion();
        double ownershipPercentaje = redondeoDecimales(command.getAccionesOwner() / command.getAccionesTotales(), 1);
        double pixelcoinsEmpresa = marketCap - pixelcoinsOwner;

        TextComponent message = new TextComponent(String.format(
                GOLD + "Seguro que quieres sacar a bolsa la empresa %s. Tendra una capitalizacion de " + GREEN + "%s PC" +
                        GOLD + ". Seras el owner de "+AQUA+"%s %"+GOLD+" con un valor total de "+GREEN+"%s PC",
                command.getEmpresa(), marketCap, ownershipPercentaje, pixelcoinsEmpresa
        ));

        player.spigot().sendMessage(message);
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("empresas confirmipo %s %s %s %s",
                command.getEmpresa(), command.getAccionesTotales(), command.getAccionesOwner(), command.getPrecioPorAccion())));
    }
}
