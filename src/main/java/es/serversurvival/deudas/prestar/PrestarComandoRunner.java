package es.serversurvival.deudas.prestar;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.bukkitclassmapper.menus.MenuService;
import es.jaime.javaddd.domain.exceptions.CannotBeYourself;
import es.jaime.javaddd.domain.exceptions.IllegalQuantity;
import es.serversurvival._shared.DependecyContainer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.*;

@Command(
        value = "deudas prestar",
        args = {"nombreAccionista", "pixelcoins", "dias", "[interes]¡1!"},
        explanation = "Prestar dinero a un nombreAccionista durante dias. La deuda se pagara cada dia"
)
public class PrestarComandoRunner implements CommandRunnerArgs<PrestarComando> {
    private final MenuService menuService;

    public PrestarComandoRunner() {
        this.menuService = DependecyContainer.get(MenuService.class);
    }

    @Override
    public void execute(PrestarComando comando, CommandSender enviador) {
        Player destinatario = comando.getJugador();
        int pixelcoins = comando.getPixelcoins();
        int dias = comando.getDias();
        int interes = comando.getInteres();
        
        if(dias <= 0 || pixelcoins <= 0 || interes <= 0)
            throw new IllegalQuantity("Los dias, pixelcoins, intereses han de ser positivos,");
        if(enviador.getName().equalsIgnoreCase(comando.getJugador().getName()))
            throw new CannotBeYourself("No te puedes autoprestar dinero");

        this.menuService.open(destinatario, new PrestamoConfirmacionMenu(
                destinatario.getName(),
                enviador.getName(),
                pixelcoins,
                dias,
                interes
        ));

        enviador.sendMessage(GOLD + "Has enviado la solicidtud de prestamo");
    }
}
