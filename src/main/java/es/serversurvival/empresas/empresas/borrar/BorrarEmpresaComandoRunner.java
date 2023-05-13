package es.serversurvival.empresas.empresas.borrar;

import es.bukkitbettermenus.MenuService;
import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import lombok.AllArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.*;

@Command(
        value = "empresas borrar",
        args = {"nombre"},
        explanation = "Borrar una empresa de la que seas owner. El dinero de la empresa se te transferira"
)
@AllArgsConstructor
public class BorrarEmpresaComandoRunner implements CommandRunnerArgs<BorrarEmpresaComando> {
    private final EmpresasService empresasService;
    private final MenuService menuService;

    @Override
    public void execute(BorrarEmpresaComando comando, CommandSender player) {
        String empresa = comando.getNombre();

        if(!this.empresasService.getByNombre(empresa).getOwner().equalsIgnoreCase(player.getName())){
            player.sendMessage(DARK_RED + "No eres el owner de la empresa");
            return;
        }

        this.menuService.open((Player) player, BorrarEmpresaConfirmacionMenu.class, BorrarEmpresaConfirmacionMenuState.fromCommand(comando));
    }
}
