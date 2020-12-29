package es.serversurvival.comandos.comandos;

import es.serversurvival.comandos.Comando;
import es.serversurvival.mySQL.tablasObjetos.ConversacionWeb;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

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
        if(args.length == 0){
            player.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: " + this.sintaxis);
            return;
        }
        String mensaje = buildStringFromArray(args);
        if(mensaje.length() > 50){
            player.sendMessage(ChatColor.DARK_RED + "El mensaje tiene que tener como maximo 50 caracteres");
            return;
        }

        conversacionesWebMySQL.conectar();
        ConversacionWeb conversacion = conversacionesWebMySQL.getConversacionServer(player.getName());
        if(conversacion == null){
            player.sendMessage(ChatColor.DARK_RED + "No tienes ninguna conversacion abierta");
            return;
        }

        conversacionesWebMySQL.nuevoMensaje(conversacion, player, mensaje);
        conversacionesWebMySQL.desconectar();
    }

    private String buildStringFromArray (String[] array) {
        StringBuilder builder = new StringBuilder();

        for(int i = 0; i < array.length; i++){
            builder.append(array[i]);

            if((array.length - 1) != i) {
                builder.append(" ");
            }
        }

        return builder.toString();
    }
}
