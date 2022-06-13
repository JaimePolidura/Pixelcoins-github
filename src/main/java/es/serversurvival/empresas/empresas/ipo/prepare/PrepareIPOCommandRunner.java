package es.serversurvival.empresas.empresas.ipo.prepare;

import es.jaime.javaddd.domain.exceptions.IllegalQuantity;
import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerArgs;
import es.jaimetruman.menus.MenuService;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;
import es.serversurvival.empresas.empresas.ipo.IPOCommand;
import main.ValidationResult;
import main.ValidatorService;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static es.serversurvival._shared.utils.Funciones.redondeoDecimales;
import static es.serversurvival._shared.utils.validaciones.Validaciones.*;
import static org.bukkit.ChatColor.*;

@Command(
        value = "empresas ipo",
        args = {"empresa", "accionesTotales", "accionesOwner", "precioPorAccion"},
        explanation = "Puedes sacar tus empresas a la 'bolsa' donde el resto de jugadores podran comprar las acciones. " +
                "Por cada venta de la empresa a jugadores tu empresa recaudara las pixelcoins. Para mas ayuda pregunta al admin"
)
public final class PrepareIPOCommandRunner implements CommandRunnerArgs<IPOCommand> {
    private final EmpresasService empresasService;
    private final MenuService menuService;

    public PrepareIPOCommandRunner() {
        this.empresasService = DependecyContainer.get(EmpresasService.class);
        this.menuService = DependecyContainer.get(MenuService.class);
    }

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
            throw new IllegalQuantity("La cantidad de las acciones y el precio ha de ser un numero positivo");
        if(accionesOwner >= accionesTotales)
            throw new IllegalQuantity("Las acciones que van a ser tuyas tienen que ser menores que el total");
        if(accionesTotales <= 2)
            throw new IllegalQuantity("El minimo de acciones totales han de ser 2");
        if(empresa.isCotizada())
            throw new IllegalStateException("La empresa que quieres sacar a bolsa ya cotiza en bolsa");

        double marketCap = command.getAccionesTotales() * command.getPrecioPorAccion();
        double pixelcoinsOwner = command.getAccionesOwner() * command.getPrecioPorAccion();

        this.menuService.open(player, new EmpresaIPOConfirmMenu(
                empresa, accionesTotales, precioPorAccion, accionesOwner
        ));
    }
}
