package es.serversurvival.empresas.empresas.comprarservicio;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;
import es.serversurvival._shared.utils.Funciones;
import org.bukkit.command.CommandSender;

import static es.serversurvival._shared.utils.Funciones.FORMATEA;
import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.GOLD;

@Command(
        value = "comprar",
        args = {"empresa", "precio"},
        explanation = "Comprar un servicio a la empresa, <empresa> nombre de la empresa <precio> pixelcoisn a dar"
)
public class ComprarServicionComandoRunner implements CommandRunnerArgs<ComprarServicionComando> {
    private final ComprarServicioUseCase useCase;

    public ComprarServicionComandoRunner(){
        this.useCase = new ComprarServicioUseCase();
    }

    @Override
    public void execute(ComprarServicionComando comprarServicionComando, CommandSender player) {
        String empresaNombre = comprarServicionComando.getEmpresa();
        double precio = comprarServicionComando.getPrecio();

        Empresa empresa = useCase.comprar(player.getName(), empresaNombre, precio);

        player.sendMessage(GOLD + "Has pagado " + GREEN + precio + " PC " + GOLD + " a la empresa: " + empresaNombre + " por su servicio");

        String mensajeOnline = GOLD + player.getName() + " ha comprado vuestro servicio de la empresa: " + empresaNombre +
                " por " + GREEN + FORMATEA.format(precio) + " PC";

        Funciones.enviarMensaje(empresa.getOwner(), mensajeOnline, mensajeOnline);
    }
}
