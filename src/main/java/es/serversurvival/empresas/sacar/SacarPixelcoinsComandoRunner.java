package es.serversurvival.empresas.sacar;

import es.jaime.EventListener;
import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival.empresas._shared.mysql.Empresa;
import es.serversurvival.empresas._shared.mysql.Empresas;
import es.serversurvival._shared.comandos.PixelcoinCommand;
import es.serversurvival._shared.utils.Funciones;
import main.ValidationResult;
import main.ValidatorService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static es.serversurvival._shared.utils.validaciones.Validaciones.*;
import static org.bukkit.ChatColor.*;

@Command(
        value = "empresas sacar",
        args = {"empresa", "pixelcoins"},
        explanation = "Sacar pixelcoins de tu empresa"
)
public class SacarPixelcoinsComandoRunner extends PixelcoinCommand implements CommandRunnerArgs<SacarPixelcoinsComando> {
    private final String usoIncorrecto = DARK_RED + "Uso incorrecto: /empresas sacar <empresa> <pixelcoins>";
    private final SacarPixelcoinsUseCase useCase = SacarPixelcoinsUseCase.INSTANCE;

    @Override
    public void execute(SacarPixelcoinsComando comando, CommandSender player) {
        String empresa = comando.getEmpresa();
        double pixelcoins = comando.getPixelcoins();

        ValidationResult result = ValidatorService
                .startValidating(empresa, OwnerDeEmpresa.of(player.getName()))
                .and(pixelcoins, PositiveNumber)
                .and(suficientesPixelcoins(empresa, pixelcoins), True.of("No puedes sacar mas pixelcoins de la empresa de las que tiene"))
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(ChatColor.DARK_RED + result.getMessage());
            return;
        }

        Funciones.enviarMensajeYSonido((Player) player, GOLD + "Has metido " + GREEN + formatea.format(pixelcoins) + " PC" + GOLD + " en tu empresa: "
                + DARK_AQUA + empresa, Sound.ENTITY_PLAYER_LEVELUP);

        useCase.sacar(player.getName(), empresa, pixelcoins);
    }


    private boolean suficientesPixelcoins(String empresaSupplier, double pixelcoins) {
        return Empresas.INSTANCE.getEmpresa(empresaSupplier).getPixelcoins() >= pixelcoins;
    }

    @EventListener
    public void onPixelcoinsSacadas (PixelcoinsSacadasEvento evento) {
        Player player = Bukkit.getPlayer(evento.getJugador().getNombre());
        Empresa empresa = evento.getEmpresa();

        Funciones.enviarMensajeYSonido(player, GOLD + "Has sacado " + GREEN + formatea.format(evento.getPixelcoins())
                + " PC" + GOLD + " de tu empresa: " + DARK_AQUA + empresa.getNombre() + GOLD + " ahora tiene: " + GREEN
                + formatea.format(empresa.getPixelcoins() - evento.getPixelcoins()) + " PC", Sound.ENTITY_PLAYER_LEVELUP);
    }
}
