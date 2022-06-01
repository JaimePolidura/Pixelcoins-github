package es.serversurvival.empresas.empresas.borrar;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerArgs;
import es.jaimetruman.menus.MenuService;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.*;

@Command(
        value = "empresas borrar",
        args = {"nombre"},
        explanation = "Borrar una empresa de la que seas owner. El dinero de la empresa se te transferira"
)
public class BorrarEmpresaComandoRunner implements CommandRunnerArgs<BorrarEmpresaComando> {
    private final EmpresasService empresasService;
    private final MenuService menuService;

    public BorrarEmpresaComandoRunner(){
        this.empresasService = DependecyContainer.get(EmpresasService.class);
        this.menuService = DependecyContainer.get(MenuService.class);
    }

    @Override
    public void execute(BorrarEmpresaComando comando, CommandSender player) {
        String empresa = comando.getNombre();

        if(!this.empresasService.getByNombre(empresa).getOwner().equalsIgnoreCase(player.getName())){
            player.sendMessage(DARK_RED + "No eres el owner de la empresa");
            return;
        }

        var empresaABorrar = this.empresasService.getByNombre(comando.getNombre());
        this.menuService.open((Player) player, new BorrarEmpresaConfirmacionMenu(player.getName(), empresaABorrar));
    }
}
