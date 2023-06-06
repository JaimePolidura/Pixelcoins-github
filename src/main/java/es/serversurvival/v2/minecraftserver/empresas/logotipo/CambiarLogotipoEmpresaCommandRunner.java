package es.serversurvival.v2.minecraftserver.empresas.logotipo;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import org.bukkit.entity.Player;

@Command(
        value = "empresas logotipo",
        args = {"empresa"},
        explanation = "Cambiar el logotipo de tu empresa con el item que tengas en la mano"
)
public final class CambiarLogotipoEmpresaCommandRunner implements CommandRunnerArgs<CambiarLogotipoEmpresaComando> {
    @Override
    public void execute(CambiarLogotipoEmpresaComando comando, Player player) {
        player.performCommand(String.format("empresas editar %s logitipo", comando.getEmpresa()));
    }
}
