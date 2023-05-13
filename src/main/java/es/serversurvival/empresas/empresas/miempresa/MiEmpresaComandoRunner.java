package es.serversurvival.empresas.empresas.miempresa;

import es.bukkitbettermenus.MenuService;
import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;
import lombok.AllArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(
        value = "empresas miempresa",
        args = {"empresa"},
        explanation = "Ver todos los datos de tu <empresa>"
)
@AllArgsConstructor
public class MiEmpresaComandoRunner implements CommandRunnerArgs<MiEmpresaComando> {
    private final EmpresasService empresasService;
    private final MenuService menuService;

    @Override
    public void execute(MiEmpresaComando miEmpresaComando, CommandSender sender) {
        Empresa empresa = this.empresasService.getByNombre(miEmpresaComando.getEmpresa());

        if(!empresa.getOwner().equalsIgnoreCase(sender.getName()))
            throw new NotTheOwner("No eres el owner de la empresa");

        this.menuService.open((Player) sender, VerEmpresaMenu.class, empresa);
    }
}
