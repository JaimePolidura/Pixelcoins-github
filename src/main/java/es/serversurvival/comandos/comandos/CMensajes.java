package es.serversurvival.comandos.comandos;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.comandos.PixelcoinCommand;
import es.serversurvival.mySQL.MySQL;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(name = "mensajes")
public class CMensajes extends PixelcoinCommand implements CommandRunner {
    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        MySQL.conectar();
        mensajesMySQL.mostrarMensajesYBorrar((Player) commandSender);
        MySQL.desconectar();
    }
}
