package es.serversurvival.comandos.subComandos.ayuda;

import es.serversurvival.objetos.mySQL.Transacciones;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class DineroAyudaSubComando extends AyudaSubCommand {
    private final String SCNombre = "dinero";
    private final String sintaxis = "/ayuda dinero";
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
        p.sendMessage(ChatColor.YELLOW + " Puedes intercambiar 1 diamante por " + Transacciones.DIAMANTE + " pixelcoins o 1 un bloque de cuarzo por " + Transacciones.CUARZO + " pixelcoins o lapislazuli por " + Transacciones.LAPISLAZULI + " pixelcoins y viceversa  en los wither en " + ChatColor.WHITE + "/warp Spawn " + ChatColor.YELLOW +
                ". Comandos de pixelcoins:");
        p.sendMessage("          ");
        p.sendMessage("/dinero" + ChatColor.GOLD + " Ver el dinero que tienes");
        p.sendMessage("          ");
        p.sendMessage("/pagar NombreDelJugador CantidadDePixelcoins" + ChatColor.GOLD + " Pagar a un jugador un cierto numero de pixelcoins");
        p.sendMessage("          ");
        p.sendMessage("/top" + ChatColor.GOLD + " Ver los más ricos, pobres, vendedores, fiables y menos fiables (deuda)");
        p.sendMessage("          ");
        p.sendMessage("/estadistcias" + ChatColor.GOLD + " Ver todas tus estadisticas de la tienda, deudas etc. /ayuda estadisticas");
    }
}