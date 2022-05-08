package es.serversurvival.empresas.borrar;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.comandos.PixelcoinCommand;
import es.serversurvival.empresas._shared.application.EmpresasService;
import main.ValidationResult;
import main.ValidatorService;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static es.serversurvival._shared.utils.validaciones.Validaciones.*;
import static org.bukkit.ChatColor.*;

@Command(
        value = "empresas borrar",
        args = {"nombre"},
        explanation = "Borrar una empresa de la que seas owner. El dinero de la empresa se te transferira"
)
public class BorrarEmpresaComandoRunner extends PixelcoinCommand implements CommandRunnerArgs<BorrarEmpresaComando> {
    private final EmpresasService empresasService;

    public BorrarEmpresaComandoRunner(){
        this.empresasService = DependecyContainer.get(EmpresasService.class);
    }

    @Override
    public void execute(BorrarEmpresaComando borrarEmpresaComando, CommandSender player) {
        String empresa = borrarEmpresaComando.getNombre();

        if(!this.empresasService.getEmpresaByNombre(empresa).getOwner().equalsIgnoreCase(player.getName())){
            player.sendMessage(DARK_RED + "No eres el owner de la empresa");
            return;
        }

        BorrrarEmpresaConfirmacion confirmacionMenu = new BorrrarEmpresaConfirmacion((Player) player, empresa);
    }
}
