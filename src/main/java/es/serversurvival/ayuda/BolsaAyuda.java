package es.serversurvival.ayuda;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@Command("ayuda bolsa")
public class BolsaAyuda implements CommandRunner {

    @Override
    public void execute(CommandSender player, String[] args) {
        player.sendMessage("     ");
        player.sendMessage(ChatColor.GOLD + "Se puede invertir en acciones de empresas de verdad que cotizen en estados unidos para ello puedes hacer /bolsa valores, se te abrira un menu " +
                "con diferentes accines de ejemplo por ejemplo si quieres comprar acciones de Santander clickearias en el item y seleccionarias el numero de acciones, supongamos que la accion esta a " +
                "2,4 y si seleciconas dos acciones te gastarias un total de 4,8 PC y se te anotaria que tienes una accion de santander. Si quieres invertir en otra empresa que no este en la lista no hay" +
                " ningun problema, necesitas buscar el ticker de la accion (nombre identificatorio de la empresa cotizada ejemplo google: GOOG) y pondrias el comando /bolsa invertir <ticer> <nºAccuibes> hay que re" +
                "cordar que solo se puede acciones que cotizen en USA");
        player.sendMessage("     ");
        player.sendMessage(ChatColor.GOLD + "Si quieres vender las 2 acciones de Santander por que han subido a 5$, tedrias que poner el comando /bolsa cartera y veras todas tus posiciones" +
                "abiertas (acciones que tienes) y te señalara la id de tu posicion, pongamos que es 2 ID=2, a continueacion tendrias que poner el comando /bolsa vender <id> <nºAcciones>" +
                " que seria en este caso: /bolsa vender 5 2 (no tiene por que ser 2 obligatoriamente puedes vender el nº de acciones que tengas en esa posicion) y te sumaria a la cuenta de minecraft un " +
                "beneficio de 10PC frente a las 4,8 PC que pusiste al principio una rentabilidad algo superior al 50%");
        player.sendMessage("    ");
        player.sendMessage("/bolsa vendercorto <ticker> <nº acciones> " + ChatColor.GOLD + "En bolsa uno puede ganar dinero cuando el precio de la accion baja." +
                " Para eso, se le presta unas acciones a cambio de un pequeño interes. El inversor las vende una vez prestadas y cuando bajen de precio las recompra. " +
                "Por asi decirlo " + "las devuelve enbolsandose la diferencia. No se cobra dividendos. Al venderlo se te cargara a tus ahorros un 5% de todo lo que" +
                " representa tu venta en corto (precioPorAccion * cantidad vendida)");
        player.sendMessage("   ");
        player.sendMessage("/bolsa per <ticker> " + ChatColor.GOLD + "Sirve para ver el ratio per de la accion en concreto mediante un ticker");
        player.sendMessage("   ");
        player.sendMessage("/bolsa operacionescerradas: " + ChatColor.GOLD + "Ver todas las ventas que has hecho en bolsa y sus estadisticas");
        player.sendMessage("    ");
        player.sendMessage("/bolsa estadisticas: " + ChatColor.GOLD + "Ver tus mejores y peores operaciones");
        player.sendMessage("    ");
        player.sendMessage("/bolsa invertir <invertir> <nº acciones>: " + ChatColor.GOLD + "invertir en bolsa con pixelcoins, el ticker: nombre identificatorio de la empresa (buscar en internet, sino prueba a buscarlo en /bolsa valores) y nºacciones las que vas a comprar");
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
        player.sendMessage("    ");
        player.sendMessage("/bolsa dividendo [ticker] " + ChatColor.GOLD + "Si no pones el ticker, te muestra los proximos dividendos para tu cartera de acciones. Si pones el ticker te muestra el proximo dividendo para esa accion.");
        player.sendMessage("    ");
    }
}
