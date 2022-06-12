package es.serversurvival.empresas.empresas.crear;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerArgs;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(
        value = "empresas crear",
        args = {"nombre", "...descripccion"},
        explanation = "Crear empresa en la que podras contratar jugadores, facturar etc"
)
public class CrearEmpresasComandoRunner implements CommandRunnerArgs<CrearEmpresasComando> {
    private final CrearEmpresaUseCase useCase;

    public CrearEmpresasComandoRunner(){
        this.useCase = new CrearEmpresaUseCase();
    }

    @Override
    public void execute(CrearEmpresasComando crearEmpresasComando, CommandSender sender) {
        String nombreEmpresa = crearEmpresasComando.getNombre();
        String desc = crearEmpresasComando.getDescripccion();

        useCase.crear(sender.getName(), nombreEmpresa, desc);

        sender.sendMessage(ChatColor.GOLD + "Has creado una empresa! " + ChatColor.GOLD + "comandos utiles: " + ChatColor.AQUA + "/empresas depositar, /empresas contratar, /empresas logotipo, /empresas vertodas, /empresas miempresa /empresas editarnombre m?s: /ayuda empresario o /empresas ayuda");
        ((Player) sender).playSound(((Player) sender).getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

        Bukkit.getServer().broadcastMessage(ChatColor.GOLD + sender.getName() + " ha creado una nueva empresa: " + ChatColor.DARK_AQUA + nombreEmpresa);
    }
}
