package es.serversurvival.comandos.subComandos.ayuda;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.mySQL.Empresas;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(name = "ayuda empresario")
public class EmpresarioAyuda implements CommandRunner {
    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage("   ");
        sender.sendMessage(ChatColor.YELLOW + "Puedes hacer tu propia empresa, contratar a gente, venderla, ganar pixelcoins etc, principales comandos:");
        sender.sendMessage("   ");
        sender.sendMessage("/empresas crear <nombre> <descripcion> " + ChatColor.GOLD + "Para crear tu empresa con una descripccion(recomendable poner los precios servicios etc). Solo puedes tener como maximo " + Empresas.nMaxEmpresas);
        sender.sendMessage("   ");
        sender.sendMessage("/empresas logotipo <empresa> " + ChatColor.GOLD + "A la hora de hacer /empresas sirve para poner el propio item que quieres que apareza en el inventario. Para ello selecionas el objeto que quiereas en la mano y pones el comando");
        sender.sendMessage("   ");
        sender.sendMessage("/empresas depositar <empresa> <cantidad>" + ChatColor.GOLD + "Sirve para poner pixelcoins a la empresa de tus pixelcoins, muy importante si quieres pagar a tus empleados");
        sender.sendMessage("   ");
        sender.sendMessage("/empresas sacar <empresa> <cantidad> " + ChatColor.GOLD + "Sirve para sacar las pixelcoins de tu empresa y ponertelas en tu cuenta");
        sender.sendMessage("   ");
        sender.sendMessage("/empresas contratar <jugador> <empresa> <sueldo> <tiposueldo> [cargo] " + ChatColor.GOLD + "Puedes contratar a jugadores a tu propia empresa.");
        sender.sendMessage(ChatColor.GOLD + "      -Sueldo: Pixelcoins que quieres que se le paguen");
        sender.sendMessage(ChatColor.GOLD + "      -Tiposueldo: Frecuencia de pago de las pixelcoins. Puede ser: 'd' (Una vez al dia), 's' (una vez a la semana) '2s' (2 veces a la semana) 'm' (Una vez al mes). Las pixelcoins se sacaran de las pixelcoins que tengan la empresa. En caso de que no tengan las " +
                "suficientes, se le enviara un mensaje al empleado adviertendole que no se le ha podido pagar las PC. Al jugador se le pagaran automaticamente");
        sender.sendMessage(ChatColor.GOLD + "      -Cargo: nombre del cargo que tendra en la empresa, no es obligatorio ponerlo");
        sender.sendMessage("   ");
        sender.sendMessage("/empresas despedir <empleado> <empresa> <razon> " + ChatColor.GOLD + " Puedes despedir a un jugador de tu empresa con una razon");
        sender.sendMessage("   ");
        sender.sendMessage("/empresas editarempleado <empresa> <jugador> <tipo> <valor>" + ChatColor.GOLD + "Puedes editar un empleado de tu empresa. En cuanto a <tipo> hay varios:");
        sender.sendMessage(ChatColor.GOLD + "     -sueldo: cambiar el sueldo de un empleado. en <valor> el nuevo sueldo");
        sender.sendMessage(ChatColor.GOLD + "     -tiposueldo: cambiar la frecuencia con la que el jugador cobra. En <valor> la nueva frecuencia: d, s, 2s, m");
        sender.sendMessage("   ");
        sender.sendMessage("/empresas vender <empresa> <jugador> <precio> " + ChatColor.GOLD + "Puedes vender tu empresa a un jugador por un precio. Al jugador se le enviara una solicitud para que te la compre");
        sender.sendMessage("   ");
        sender.sendMessage("/empresas miempresa <empresa> " + ChatColor.GOLD + "Ver las estadisticas y los trabajadores de tu empresa");
        sender.sendMessage("   ");
        sender.sendMessage("/empresas editarnombre <empresa> <nuevonombre> " + ChatColor.GOLD + "Cambiar el nombre de tu empresa a uno nuevo");
        sender.sendMessage("   ");
        sender.sendMessage("/empresas borrar <empresa> " + ChatColor.GOLD + "Borrar tu propia empresa, se te enviara una solicitud para confirmar que lo quieres borrars");
        sender.sendMessage("   ");
        sender.sendMessage("/empresas editardescripccion <empresa> <nueva descripcion> " + ChatColor.GOLD + "Editar la descripccion de tu empresa");
    }
}
