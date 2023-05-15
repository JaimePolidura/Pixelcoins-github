package es.serversurvival.v1.empresas.empresas.ipo.prepare;

import es.bukkitbettermenus.MenuService;
import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.jaime.javaddd.domain.exceptions.IllegalQuantity;
import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.serversurvival.v1.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.v1.empresas.empresas._shared.domain.Empresa;
import es.serversurvival.v1.empresas.empresas.ipo.IPOCommand;
import lombok.AllArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(
        value = "empresas ipo",
        args = {"empresa", "accionesTotales", "accionesOwner", "precioPorAccion"},
        explanation = "Puedes sacar tus empresas a la 'bolsa' donde el resto de jugadores podran comprar las cantidad. " +
                "Por cada venta de la empresa a jugadores tu empresa recaudara las pixelcoins. Para mas ayuda pregunta al admin"
)
@AllArgsConstructor
public final class PrepareIPOCommandRunner implements CommandRunnerArgs<IPOCommand> {
    private final EmpresasService empresasService;
    private final MenuService menuService;

    @Override
    public void execute(IPOCommand command, CommandSender sender) {
        Player player = (Player) sender;
        String empresaNombre = command.getEmpresa();
        String jugadorNombre = sender.getName();
        int accionesTotales = command.getAccionesTotales();
        int accionesOwner = command.getAccionesOwner();
        double precioPorAccion = command.getPrecioPorAccion();
        Empresa empresa = this.empresasService.getByNombre(empresaNombre);

        if(!empresa.getOwner().equalsIgnoreCase(jugadorNombre))
            throw new NotTheOwner("Empresa no encontrada o no eres el owner");
        if(accionesTotales <= 0 || accionesOwner < 0 || precioPorAccion <= 0)
            throw new IllegalQuantity("La cantidad de las cantidad y el precio ha de ser un numero positivo");
        if(accionesOwner >= accionesTotales)
            throw new IllegalQuantity("Las cantidad que van a ser tuyas tienen que ser menores que el total");
        if(accionesTotales <= 2)
            throw new IllegalQuantity("El minimo de cantidad totales han de ser 2");
        if(empresa.isCotizada())
            throw new IllegalStateException("La empresa que quieres sacar a bolsa ya cotiza en bolsa");

        this.menuService.open(player, EmpresaIPOConfirmMenu.class, new EmpresaIPOConfirmMenuState(
                empresa, accionesTotales, precioPorAccion, accionesOwner
        ));
    }
}
