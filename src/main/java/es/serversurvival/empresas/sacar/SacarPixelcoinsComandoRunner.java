package es.serversurvival.empresas.sacar;

import es.jaime.EventListener;
import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival.empresas._shared.domain.Empresa;
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
    private final SacarPixelcoinsUseCase useCase;

    public SacarPixelcoinsComandoRunner(){
        this.useCase = new SacarPixelcoinsUseCase();
    }

    @Override
    public void execute(SacarPixelcoinsComando comando, CommandSender player) {
        double pixelcoinsASacar = comando.getPixelcoins();
        String empresaNombre = comando.getEmpresa();

        useCase.sacar(player.getName(), empresaNombre, pixelcoinsASacar);

        Funciones.enviarMensajeYSonido((Player) player, GOLD + "Has sacado " + GREEN + formatea.format(pixelcoinsASacar)
                + " PC" + GOLD + " de tu empresa: " + DARK_AQUA + empresaNombre, Sound.ENTITY_PLAYER_LEVELUP);

    }
}
