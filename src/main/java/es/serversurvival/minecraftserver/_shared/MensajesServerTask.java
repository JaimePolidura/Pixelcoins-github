package es.serversurvival.minecraftserver._shared;

import es.bukkitclassmapper.task.BukkitTimeUnit;
import es.bukkitclassmapper.task.Task;
import es.bukkitclassmapper.task.TaskRunner;
import es.serversurvival._shared.utils.Funciones;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

@Task(value = 10 * BukkitTimeUnit.MINUTE)
public class MensajesServerTask implements TaskRunner {
    public static final int delay = 1200;
    private List<String> mensajes;

    public MensajesServerTask(){
        buildMensajes();
    }

    private void buildMensajes () {
        mensajes = new ArrayList<>();

        mensajes.add(ChatColor.GOLD + "" + ChatColor.BOLD + "Web del server: " + ChatColor.AQUA + "" + ChatColor.UNDERLINE + "http://serversurvival.ddns.net");
        mensajes.add(ChatColor.GOLD + "Recuerda que puedes invertir en bolsa con el comando /bolsa valores");
        mensajes.add(ChatColor.GOLD + "Puedes consultar todos los comandos en " + ChatColor.AQUA +"/ayuda");
        mensajes.add(ChatColor.GOLD + "Recuerda que puedes endeudar a jugadores con" + ChatColor.AQUA + " /deuda prestar <nombreAccionista> <cantiddad> <dias maximos a pagar> [interes] (El interes no es obligatorio)");
        mensajes.add(ChatColor.GOLD + "¿Necesitas vender un objeto a un nombreAccionista especifico? No uses la tienda, selecciona el objeto con la mano y introduce el siguiente comando: /venderjugador <nombreAccionista> <precio>");
        mensajes.add(ChatColor.GOLD + "¿Queires ganar dinero cuando una accion baja? pon /bolsa vendercorto <ticker> <cantidad de cantidad>");
        mensajes.add(ChatColor.GOLD + "Para conseguir dinero, ve al spawn con /warp y ahi podras intercambiar diamante,cuarzo,lapislazuli por pixelcoins!!");
        mensajes.add(ChatColor.GOLD + "Para ver el ranking de los jugadores en cuanto dinero,bolsa etc haz /top");
    }

    private int generateRandomNumber () {
        return Funciones.generateRandomNumber(0, mensajes.size() - 1);
    }

    @Override
    public void run() {
        if(Bukkit.getOnlinePlayers().size() != 0)
            Bukkit.broadcastMessage(mensajes.get(generateRandomNumber()));
    }
}
