package es.serversurvival.comandos.comandos;

import es.serversurvival.comandos.Comando;
import es.serversurvival.objetos.mySQL.Transacciones;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Comprar extends Comando {
    private final String CNOmbre = "comprar";
    private final String sintaxis = "/comprar <empresa> <precio>";
    private final String ayuda = "comprar un servicio a una empresa por unas pixelcoisn";

    public String getCNombre() {
        return CNOmbre;
    }

    public String getSintaxis() {
        return sintaxis;
    }

    public String getAyuda() {
        return ayuda;
    }

    public void execute(Player p, String[] args) {
        if (args.length != 2) {
            p.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: " + this.sintaxis);
            return;
        }

        String nombreempresaa = args[0];
        String precioS = args[1];
        double precioD = 0;
        try {
            precioD = Double.parseDouble(precioS);
            if (precioD <= 0) {
                p.sendMessage(ChatColor.DARK_RED + "A ser posible mete numeros que sean negativos o que no sean ceros");
                return;
            }
        } catch (NumberFormatException e) {
            p.sendMessage(ChatColor.DARK_RED + "A ser posible mete numero no texto");
            return;
        }

        Transacciones transaccionesMySQL = new Transacciones();
        transaccionesMySQL.conectar();
        transaccionesMySQL.comprarServivio(nombreempresaa, precioD, p);
        transaccionesMySQL.desconectar();
    }
}