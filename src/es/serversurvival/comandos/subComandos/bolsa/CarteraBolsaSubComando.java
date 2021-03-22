package es.serversurvival.comandos.subComandos.bolsa;

import es.serversurvival.main.Funciones;
import es.serversurvival.main.Pixelcoin;
import es.serversurvival.objetos.apiHttp.IEXCloud_API;
import es.serversurvival.objetos.mySQL.PosicionesAbiertas;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CarteraBolsaSubComando extends BolsaSubCommand {
    private final String SCNombre = "cartera";
    private final String sintaxis = "/bolsa cartera";
    private final String ayuda = "ver todas las acciones que tienes en cartera";

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
        Bukkit.getScheduler().scheduleAsyncDelayedTask(Pixelcoin.getInstance(), () -> {
            try {
                player.sendMessage(ChatColor.GOLD + "---------------------------------------------------");
                player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "                  TU CARTERA DE ACCIONES");

                PosicionesAbiertas posicionesAbiertas = new PosicionesAbiertas();
                posicionesAbiertas.conectar();
                ArrayList<Integer> ids = posicionesAbiertas.getIdsPosicionesJugador(player.getName());
                int pos = 1;

                for (int id : ids) {
                    //Bukkit.getScheduler().scheduleAsyncDelayedTask(Pixelcoin.getInstance(), () -> {
                    //synchronized (this){
                    String ticker = posicionesAbiertas.getTicker(id);
                    double precioInicial = posicionesAbiertas.getPreciopApertura(id);
                    int nAcciones = posicionesAbiertas.getNAcciones(id);
                    double precio = 0;
                    try {
                        precio = IEXCloud_API.getOnlyPrice(ticker);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    double revalorizacion = (precio * nAcciones) - (precioInicial * nAcciones);
                    double diferenciaPor = Funciones.redondeoDecimales(Funciones.diferenciaPorcntual(precioInicial, precio), 2);

                    if (diferenciaPor > 0) {
                        player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "ID: " + id + ": " + ChatColor.GOLD + ticker + " Compraste " + nAcciones + " accion a " + ChatColor.GREEN + formatea.format(precioInicial) + " PC/Accion "
                                + ChatColor.GOLD + "cotiza a " + ChatColor.GREEN + formatea.format(precio) + " PC/Accion " + ChatColor.GOLD + " -> " + ChatColor.GREEN + "+" + formatea.format(diferenciaPor) + "% " + ChatColor.GREEN + " : " + formatea.format(revalorizacion) + " PC" + ChatColor.GOLD + " de " + ChatColor.GREEN + formatea.format(precioInicial * nAcciones) + " PC " + ChatColor.GOLD + " invertidas en el primer momento");
                    } else {
                        player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "ID:" + id + ": " + ChatColor.GOLD + ticker + " Compraste " + nAcciones + " accion a " + ChatColor.GREEN + formatea.format(precioInicial) + " PC/Accion "
                                + ChatColor.GOLD + "cotiza a " + ChatColor.GREEN + formatea.format(precio) + " PC/Accion " + ChatColor.GOLD + " -> " + ChatColor.RED + formatea.format(diferenciaPor) + "% " + ChatColor.RED + " : " + formatea.format(revalorizacion) + " PC" + ChatColor.GOLD + " de " + ChatColor.GREEN + formatea.format(precioInicial * nAcciones) + " PC " + ChatColor.GOLD + " invertidas en el primer momento");
                    }
                    player.sendMessage("         ");
                    //}
                    //}, 0L);
                }
                player.sendMessage(ChatColor.GOLD + "----------------------------------------------------");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0L);
    }
}