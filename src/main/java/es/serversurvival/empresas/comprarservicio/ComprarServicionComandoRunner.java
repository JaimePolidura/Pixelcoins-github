package es.serversurvival.empresas.comprarservicio;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival.empresas._shared.mysql.Empresa;
import es.serversurvival._shared.comandos.PixelcoinCommand;
import es.serversurvival._shared.utils.Funciones;
import main.ValidationResult;
import main.ValidatorService;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import static es.serversurvival._shared.utils.validaciones.Validaciones.*;
import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.GOLD;

@Command(
        value = "comprar",
        args = {"empresa", "precio"},
        explanation = "Comprar un servicio a la empresa, <empresa> nombre de la empresa <precio> pixelcoisn a dar"
)
public class ComprarServicionComandoRunner extends PixelcoinCommand implements CommandRunnerArgs<ComprarServicionComando> {
    private final String usoIncorrecto = ChatColor.DARK_RED + "Uso incorrecto /comprar <empresa> <precio>";
    private final ComprarServicioUseCase useCase = ComprarServicioUseCase.INSTANCE;

    @Override
    public void execute(ComprarServicionComando comprarServicionComando, CommandSender player) {
        String empresaNombre = comprarServicionComando.getEmpresa();
        double precio = comprarServicionComando.getPrecio();

        ValidationResult result = ValidatorService
                .startValidating(precio, PositiveNumber, SuficientesPixelcoins.of(player.getName()))
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(ChatColor.DARK_RED + result.getMessage());
            return;
        }
        Empresa empresaAComprar = empresasMySQL.getEmpresa(empresaNombre);
        if (empresaAComprar == null) { //TODO
            player.sendMessage(DARK_RED + "Esa empresa no existe");
            return;
        }
        if (empresaAComprar.getOwner().equalsIgnoreCase(player.getName())) {
            player.sendMessage(DARK_RED + "No puedes comprar un servivio de tu propia empresa");
            return;
        }

        //TODO Mejorar para que al enviar el mensaje al owner no tengamos que devolver nada en el metodo
        Empresa empresa = useCase.comprar(player.getName(), empresaNombre, precio);

        player.sendMessage(GOLD + "Has pagado " + GREEN + precio + " PC " + GOLD + " a la empresa: " + empresaNombre + " por su servicio");

        String mensajeOnline = GOLD + player.getName() + " ha comprado vuestro servicio de la empresa: " + empresaNombre +
                " por " + GREEN + formatea.format(precio) + " PC";

        Funciones.enviarMensaje(empresa.getOwner(), mensajeOnline, mensajeOnline);

    }
}
