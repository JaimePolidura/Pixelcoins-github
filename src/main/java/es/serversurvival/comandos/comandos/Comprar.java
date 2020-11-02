package es.serversurvival.comandos.comandos;

import es.serversurvival.comandos.Comando;
import es.serversurvival.mySQL.MySQL;
import es.serversurvival.mySQL.Transacciones;
import es.serversurvival.util.Funciones;
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
        if(!Funciones.esDouble(args[1])){
            p.sendMessage(ChatColor.DARK_RED + "A ser posible mete numero no texto");
            return;
        }
        double precioD = Double.parseDouble(args[1]);
        if(precioD <= 0){
            p.sendMessage(ChatColor.DARK_RED + "A ser posible mete numeros que sean negativos o que no sean ceros");
            return;
        }

        MySQL.conectar();
        transaccionesMySQL.comprarServivio(args[0], precioD, p);
        MySQL.desconectar();
    }
}