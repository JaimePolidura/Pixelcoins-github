package es.serversurvival.comandos.subComandos.ayuda;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class EstadisticasAyudaSubComando extends AyudaSubCommand {
    private final String SCNombre = "estadisticas";
    private final String sintaxis = "/ayuda estadisticas";
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
        p.sendMessage("      ");
        p.sendMessage(ChatColor.GOLD + "Las estadisticas se miden solo de la tienda y por pagamos mediante el comando /pagar");
        p.sendMessage("        ");
        p.sendMessage("Nº Ventas " + ChatColor.GOLD + "El numero de veces que te han comprado en la tienda y te han pagado mediante /pagar");
        p.sendMessage("      ");
        p.sendMessage("Precio/Venta " + ChatColor.GOLD + "Se calcula: ingresos/nº de ventas. Trata sobre dar el precio medio por el cual vendes en la tienda y te te pagan con /pagar");
        p.sendMessage("      ");
        p.sendMessage("Ingresos " + ChatColor.GOLD + "Es la suma de todo el dinero que te han pagado mediante /pagar y del dinero que te han pagado en la tienda");
        p.sendMessage("         ");
        p.sendMessage("Gastos " + ChatColor.GOLD + "Es la suma de todo el dinero que te has gastado en la tienda y todo el dinero que has pagado a jugadores mediante /pagar");
        p.sendMessage("        ");
        p.sendMessage("Beneficios " + ChatColor.GOLD + "Se calcula mediante ingresos - gastos. Es el total del dinero que has ganado o perdido");
        p.sendMessage("       ");
        p.sendMessage("Rentabilidad " + ChatColor.GOLD + "Se calcula: (beneficios / ingresos) * 100. Representa sobre 100 PC cuantas has ganado o perdido(Si se indica en rojo)");
        p.sendMessage("       ");
        p.sendMessage("Pixelcoins que debes " + ChatColor.GOLD + "El total de dinero que debes a jugadores (/ayuda deuda o /deudas)");
        p.sendMessage("       ");
        p.sendMessage("Pixelcoin que te deben " + ChatColor.GOLD + "El total de dinero que te deben (ayuda deudas o /deudas)");
        p.sendMessage("       ");
        p.sendMessage("Nº inpago" + ChatColor.GOLD + "El total de veces que no has podido pagar la deuda en un dia");
        p.sendMessage("       ");
        p.sendMessage("Nº pago " + ChatColor.GOLD + "El total de veces que has pagado la deuda");
    }
}
