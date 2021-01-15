package es.serversurvival.comandos.comandos;

import es.serversurvival.comandos.Comando;
import es.serversurvival.mySQL.ConversacionesWeb;
import es.serversurvival.mySQL.tablasObjetos.ConversacionWeb;
import es.serversurvival.util.Funciones;
import es.serversurvival.validaciones.Validaciones;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static es.serversurvival.validaciones.Validaciones.*;

public class Re extends Comando {
    private final String CNombre = "re";
    private final String sintaxis = "/re <mensaje>";
    private final String ayuda = "Enviar un mensaje al usuario de la pagina web";

    @Override
    public String getCNombre() {
        return CNombre;
    }

    @Override
    public String getSintaxis() {
        return sintaxis;
    }

    @Override
    public String getAyuda() {
        return ayuda;
    }

    @Override
    public void execute(Player player, String[] args) {
        conversacionesWebMySQL.conectar();

        String playerName = player.getName();

        ValidationResult result = ValidationsService.startValidating(args.length, Different.of(0, "Tiene que tener como minomo 2 palabras"))
                .and(Funciones.buildStringFromArray(args), MaxLength.of(50), NotIncludeCharacters.of('&', '-'))
                .and(existeConversacion(player.getName()), True.of("No hay ninguna conversacion pendiente"))
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(ChatColor.DARK_RED + result.getMessage());
            conversacionesWebMySQL.desconectar();
            return;
        }

        conversacionesWebMySQL.nuevoMensaje(conversacionesWebMySQL.getConversacionServer(playerName), player, Funciones.buildStringFromArray(args));

        conversacionesWebMySQL.desconectar();
    }

    private boolean existeConversacion (String jugador) {
        return ConversacionesWeb.INSTANCE.getConversacionServer(jugador) != null;
    }
}
