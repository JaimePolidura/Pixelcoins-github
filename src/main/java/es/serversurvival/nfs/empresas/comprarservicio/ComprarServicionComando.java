package es.serversurvival.nfs.empresas.comprarservicio;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.legacy.comandos.PixelcoinCommand;
import es.serversurvival.nfs.empresas.mysql.Empresa;
import es.serversurvival.nfs.utils.validaciones.Validaciones;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static es.serversurvival.nfs.utils.Funciones.enviarMensaje;
import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.GOLD;

@Command("comprar")
public class ComprarServicionComando extends PixelcoinCommand implements CommandRunner {
    private final String usoIncorrecto = ChatColor.DARK_RED + "Uso incorrecto /comprar <empresa> <precio>";
    private final ComprarServicioUseCase useCase = ComprarServicioUseCase.INSTANCE;

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        ValidationResult result = ValidationsService.startValidating(args, Validaciones.NotNull.message(ChatColor.DARK_RED + "Uso incorrecto /comprar"))
                .and(args.length, Validaciones.Same.as(2, usoIncorrecto))
                .andMayThrowException(() -> args[1], usoIncorrecto, Validaciones.PositiveNumber, Validaciones.SuficientesPixelcoins.of(sender.getName()))
                .validateAll();

        if(result.isFailed()){
            sender.sendMessage(ChatColor.DARK_RED + result.getMessage());
            return;
        }
        Empresa empresaAComprar = empresasMySQL.getEmpresa(args[0]);
        if (empresaAComprar == null) { //TODO
            player.sendMessage(DARK_RED + "Esa empresa no existe");
            return;
        }
        if (empresaAComprar.getOwner().equalsIgnoreCase(player.getName())) {
            player.sendMessage(DARK_RED + "No puedes comprar un servivio de tu propia empresa");
            return;
        }

        String empresaNombre = args[0];
        double precio = Double.parseDouble(args[1]);

        //TODO Mejorar para que al enviar el mensaje al owner no tengamos que devolver nada en el metodo
        Empresa empresa = useCase.comprar(player.getName(), empresaNombre, precio);

        player.sendMessage(GOLD + "Has pagado " + GREEN + precio + " PC " + GOLD + " a la empresa: " + empresaNombre + " por su servicio");

        String mensajeOnline = GOLD + player.getName() + " ha comprado vuestro servicio de la empresa: " + empresaNombre +
                " por " + GREEN + formatea.format(precio) + " PC";

        enviarMensaje(empresa.getOwner(), mensajeOnline, mensajeOnline);
    }
}
