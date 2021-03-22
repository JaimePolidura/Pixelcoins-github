package es.serversurvival.comandos.subComandos.ayuda;

import es.serversurvival.objetos.mySQL.Empresas;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class EmpresarioAyudaSubComando extends AyudaSubCommand {
    private final String SCNombre = "empresario";
    private final String sintaxis = "/ayuda empresario";
    private final String ayuda = "";

    public String getSCNombre() {
        return SCNombre;
    }

    public String getSintaxis() {
        return sintaxis;
    }

    public String getAyuda() {
        return ayuda;
    }

    public void execute(Player p, String[] args) {
        p.sendMessage("   ");
        p.sendMessage(ChatColor.YELLOW + "Puedes hacer tu propia empresa, contratar a gente, venderla, ganar pixelcoins etc, principales comandos:");
        p.sendMessage("   ");
        p.sendMessage("/empresas crear <nombre> <descripcion> " + ChatColor.GOLD + "Para crear tu empresa con una descripccion(recomendable poner los precios servicios etc). Solo puedes tener como maximo " + Empresas.nMaxEmpresas);
        p.sendMessage("   ");
        p.sendMessage("/empresas logotipo <empresa> " + ChatColor.GOLD + "A la hora de hacer /empresas sirve para poner el propio item que quieres que apareza en el inventario. Para ello selecionas el objeto que quiereas en la mano y pones el comando");
        p.sendMessage("   ");
        p.sendMessage("/empresas depositar <empresa> <cantidad>" + ChatColor.GOLD + "Sirve para poner pixelcoins a la empresa de tus pixelcoins, muy importante si quieres pagar a tus empleados");
        p.sendMessage("   ");
        p.sendMessage("/empresas sacar <empresa> <cantidad> " + ChatColor.GOLD + "Sirve para sacar las pixelcoins de tu empresa y ponertelas en tu cuenta");
        p.sendMessage("   ");
        p.sendMessage("/empresas contratar <jugador> <empresa> <sueldo> <tiposueldo> [cargo] " + ChatColor.GOLD + "Puedes contratar a jugadores a tu propia empresa.");
        p.sendMessage(ChatColor.GOLD + "      -Sueldo: Pixelcoins que quieres que se le paguen");
        p.sendMessage(ChatColor.GOLD + "      -Tiposueldo: Frecuencia de pago de las pixelcoins. Puede ser: 'd' (Una vez al dia), 's' (una vez a la semana) '2s' (2 veces a la semana) 'm' (Una vez al mes). Las pixelcoins se sacaran de las pixelcoins que tengan la empresa. En caso de que no tengan las " +
                "suficientes, se le enviara un mensaje al empleado adviertendole que no se le ha podido pagar las PC. Al jugador se le pagaran automaticamente");
        p.sendMessage(ChatColor.GOLD + "      -Cargo: nombre del cargo que tendra en la empresa, no es obligatorio ponerlo");
        p.sendMessage("   ");
        p.sendMessage("/empresas despedir <empleado> <empresa> <razon> " + ChatColor.GOLD + " Puedes despedir a un jugador de tu empresa con una razon");
        p.sendMessage("   ");
        p.sendMessage("/empresas editarempleado <empresa> <jugador> <tipo> <valor>" + ChatColor.GOLD + "Puedes editar un empleado de tu empresa. En cuanto a <tipo> hay varios:");
        p.sendMessage(ChatColor.GOLD + "     -sueldo: cambiar el sueldo de un empleado. en <valor> el nuevo sueldo");
        p.sendMessage(ChatColor.GOLD + "     -tiposueldo: cambiar la frecuencia con la que el jugador cobra. En <valor> la nueva frecuencia: d, s, 2s, m");
        p.sendMessage("   ");
        p.sendMessage("/empresas vender <empresa> <jugador> <precio> " + ChatColor.GOLD + "Puedes vender tu empresa a un jugador por un precio. Al jugador se le enviara una solicitud para que te la compre");
        p.sendMessage("   ");
        p.sendMessage("/empresas miempresa <empresa> " + ChatColor.GOLD + "Ver las estadisticas y los trabajadores de tu empresa");
        p.sendMessage("   ");
        p.sendMessage("/empresas editarnombre <empresa> <nuevonombre> " + ChatColor.GOLD + "Cambiar el nombre de tu empresa a uno nuevo");
        p.sendMessage("   ");
        p.sendMessage("/empresas borrar <empresa> " + ChatColor.GOLD + "Borrar tu propia empresa, se te enviara una solicitud para confirmar que lo quieres borrars");
        p.sendMessage("   ");
        p.sendMessage("/empresas editardescripccion <empresa> <nueva descripcion> " + ChatColor.GOLD + "Editar la descripccion de tu empresa");
    }
}
