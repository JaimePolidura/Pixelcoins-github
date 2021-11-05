package es.serversurvival.empresas.sacar;

import es.jaime.EventListener;
import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.empresas._shared.mysql.Empresa;
import es.serversurvival.empresas._shared.mysql.Empresas;
import es.serversurvival._shared.comandos.PixelcoinCommand;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival._shared.utils.validaciones.Validaciones;
import io.vavr.control.Try;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.function.Supplier;

import static org.bukkit.ChatColor.*;

@Command("empresas sacar")
public class SacarPixelcoinsComando extends PixelcoinCommand implements CommandRunner {
    private final String usoIncorrecto = DARK_RED + "Uso incorrecto: /empresas sacar <empresa> <pixelcoins>";
    private final SacarPixelcoinsUseCase useCase = SacarPixelcoinsUseCase.INSTANCE;

    @Override
    public void execute(CommandSender player, String[] args) {

        ValidationResult result = ValidationsService.startValidating(args.length == 3, Validaciones.True.of(usoIncorrecto))
                .andMayThrowException(() -> args[1], usoIncorrecto, Validaciones.OwnerDeEmpresa.of(player.getName()))
                .andMayThrowException(() -> args[2], usoIncorrecto, Validaciones.PositiveNumber)
                .and(suficientesPixelcoinsPredicado(() -> args[1], () -> args[2]), Validaciones.True.of("No puedes sacar mas pixelcoins de la empresa de las que tiene"))
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(ChatColor.DARK_RED + result.getMessage());
            return;
        }

        String empresa = args[1];
        double pixelconis = Double.parseDouble(args[2]);

        Funciones.enviarMensajeYSonido((Player) player, GOLD + "Has metido " + GREEN + formatea.format(pixelconis) + " PC" + GOLD + " en tu empresa: "
                + DARK_AQUA + empresa, Sound.ENTITY_PLAYER_LEVELUP);

        useCase.sacar(player.getName(), empresa, pixelconis);
    }

    private boolean suficientesPixelcoinsPredicado (Supplier<String> empresaSupplier, Supplier<String> pixelcoins) {
        Try<Boolean> tryEmpresa = Try.of(() -> Empresas.INSTANCE.getEmpresa(empresaSupplier.get()).getPixelcoins() >=
                Double.parseDouble(pixelcoins.get()) );

        return tryEmpresa.isSuccess() && tryEmpresa.get();
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
