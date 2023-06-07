package es.serversurvival.minecraftserver.empresas.depositar;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival.pixelcoins.empresas._shared.empresas.Empresa;
import es.serversurvival.pixelcoins.empresas._shared.empresas.EmpresasService;
import es.serversurvival.pixelcoins.empresas.depositar.DepositarPixelcoinsEmpresaParametros;
import es.serversurvival.pixelcoins.empresas.depositar.DepositarPixelcoinsEmpresaUseCase;
import lombok.AllArgsConstructor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import static es.serversurvival._shared.utils.Funciones.FORMATEA;
import static es.serversurvival.minecraftserver._shared.MinecraftUtils.enviarMensajeYSonido;
import static org.bukkit.ChatColor.*;

@Command(
        value = "empresas depositar",
        args = {"empresa", "pixelcoins"},
        explanation = "Poner pixelcoins de tuyas a una empresa"
)
@AllArgsConstructor
public final class DepositarPixelcoinsEmpresaCommandRunner implements CommandRunnerArgs<DepositarPixelcoinsEmpresaComando> {
    private final DepositarPixelcoinsEmpresaUseCase depositarPixelcoinsEmpresaUseCase;
    private final EmpresasService empresasService;

    @Override
    public void execute(DepositarPixelcoinsEmpresaComando comando, Player player) {
        Empresa empresa = empresasService.getByNombre(comando.getEmpresa());

        depositarPixelcoinsEmpresaUseCase.depositar(DepositarPixelcoinsEmpresaParametros.builder()
                .empresaId(empresa.getEmpresaId())
                .jugadorId(player.getUniqueId())
                .pixelcoins(comando.getPixelcoins())
                .build());

        enviarMensajeYSonido(player, GOLD + "Has depositado " + GREEN + FORMATEA.format(comando.getPixelcoins()) + " PC" + GOLD
                + " en tu empresa: " + DARK_AQUA + comando.getEmpresa(), Sound.ENTITY_PLAYER_LEVELUP);
    }
}
