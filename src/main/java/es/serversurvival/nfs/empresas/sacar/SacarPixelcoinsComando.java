package es.serversurvival.nfs.empresas.sacar;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.legacy.comandos.PixelcoinCommand;
import es.serversurvival.nfs.empresas.mysql.Empresas;
import es.serversurvival.nfs.empresas.mysql.Empresa;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.function.Supplier;

import static es.serversurvival.nfs.utils.Funciones.*;
import static es.serversurvival.nfs.utils.validaciones.Validaciones.*;
import static org.bukkit.ChatColor.*;

@Command("empresas sacar")
public class SacarPixelcoinsComando extends PixelcoinCommand implements CommandRunner {
    private final String usoIncorrecto = DARK_RED + "Uso incorrecto: /empresas sacar <empresa> <pixelcoins>";
    private final SacarPixelcoinsUseCase useCase = SacarPixelcoinsUseCase.INSTANCE;

    @Override
    public void execute(CommandSender player, String[] args) {

        ValidationResult result = ValidationsService.startValidating(args.length == 3, True.of(usoIncorrecto))
                .andMayThrowException(() -> args[1], usoIncorrecto, OwnerDeEmpresa.of(player.getName()))
                .andMayThrowException(() -> args[2], usoIncorrecto, PositiveNumber)
                .and(suficientesPixelcoinsPredicado(() -> args[1], () -> args[2]), True.of("No puedes sacar mas pixelcoins de la empresa de las que tiene"))
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(ChatColor.DARK_RED + result.getMessage());
            return;
        }

        String empresa = args[1];
        double pixelconis = Double.parseDouble(args[2]);

        enviarMensajeYSonido((Player) player, GOLD + "Has metido " + GREEN + formatea.format(pixelconis) + " PC" + GOLD + " en tu empresa: "
                + DARK_AQUA + empresa, Sound.ENTITY_PLAYER_LEVELUP);

        useCase.sacar(player.getName(), empresa, pixelconis);
    }

    private boolean suficientesPixelcoinsPredicado (Supplier<String> empresaSupplier, Supplier<String> pixelcoins) {
        try{
            Empresa empresa = Empresas.INSTANCE.getEmpresa(empresaSupplier.get());

            return empresa.getPixelcoins() >= Double.parseDouble(pixelcoins.get());
        }catch (Exception e) {
            return false;
        }
    }
}
