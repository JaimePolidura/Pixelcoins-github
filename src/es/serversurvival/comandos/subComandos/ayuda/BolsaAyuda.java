package es.serversurvival.comandos.subComandos.ayuda;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class BolsaAyuda extends AyudaSubCommand {
    private final String SCNombre = "bolsa";
    private final String sintaxis = "/ayuda bolsa";
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

    public void execute(Player player, String[] args) {
        player.sendMessage("     ");
        player.sendMessage(ChatColor.GOLD + "Se pueden invertir en empesas que cotizen en Estados Unidos. Por ejemplo si quieres invertir en 2 acciones de Santander, supongamos que la accion esta a 2,4" +
                "2,4, tendrias que buscar el ticker de la accion (nombre identificatorio de la accion en este caso Santander es SAN, para buscar los tickers puedes poner el comando /bolsa valores o buscarlo en internet) y poner el comando /invertir <ticker <nº accion> que seria en estea caso" +
                " que seria en este caso: /invertir SAN 2 y se te anotaria que tienes 2 acciones y se te restaria 4,8 PC.");
        player.sendMessage("     ");
        player.sendMessage(ChatColor.GOLD + "Si quieres vender las 2 acciones de Santander por que han subido a 5$, tedrias que poner el comando /bolsa cartera y veras todas tus posiciones" +
                "abiertas (acciones que tienes) y te señalara la id de tu posicion, pongamos que es 2 ID=2, a continueacion tendrias que poner el comando /bolsa vender <id> <nºAcciones>" +
                " que seria en este caso: /bolsa vender 5 2 (no tiene por que ser 2 obligatoriamente puedes vender el nº de acciones que tengas en esa posicion) y te sumaria a la cuenta de minecraft un " +
                "beneficio de 10PC frente a las 4,8 PC que pusiste al principio una rentabilidad algo superior al 50%");
        player.sendMessage("    ");
        player.sendMessage("/bolsa operacionescerradas: " + ChatColor.GOLD + "Ver todas las ventas que has hecho en bolsa y sus estadisticas");
        player.sendMessage("    ");
        player.sendMessage("/bolsa estadisticas: " + ChatColor.GOLD + "Ver tus mejores y peores operaciones");
        player.sendMessage("    ");
        player.sendMessage("/bolsa invertir <invertir> <nº acciones>: " + ChatColor.GOLD + "invertir en bolsa con pixelcoins, el nicker nombre identificatorio de la empresa (se ve en /bolsa valores) y nºacciones las que vas a comprar");
        player.sendMessage("    ");
        player.sendMessage("/bolsa precio <ticker> " + ChatColor.GOLD + "Consultar el precio de una accion con el ticker de la empresa");
        player.sendMessage("          ");
        player.sendMessage("/bolsa valores " + ChatColor.GOLD + "Ver un ejemplo de empresas reconocidas en estados unidos con su ticker y su precio/accion");
        player.sendMessage("          ");
        player.sendMessage("/bolsa vender <id> <nº accion> " + ChatColor.GOLD + "Vender una o varias acciones que tengas, la id se ve en /bolsa cartera");
        player.sendMessage("          ");
        player.sendMessage("/bolsa cartera " + ChatColor.GOLD + "Ver todas las posciones que tienes abiertas (acciones) con su id, ganacias perdidas etc");
        player.sendMessage("          ");
        player.sendMessage("/bolsa vercartera <jugador> " + ChatColor.GOLD + "Ver la cartera de acciones de otro jugador y el peso que tiene sus accines en cartera");
    }
}
