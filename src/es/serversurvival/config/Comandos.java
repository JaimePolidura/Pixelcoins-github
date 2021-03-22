package es.serversurvival.config;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


public class Comandos {
    private Plugin plugin = Pixelcoin.getPlugin(Pixelcoin.class);
    int cambio = 750;
    public DecimalFormat formatea = new DecimalFormat("###,###.##");

    //----------- mostrar dinero-----------
    public void mostrarDinero(Player p) {
        int dinero = plugin.getConfig().getInt(p.getName());
        if (dinero > 0) {
            p.sendMessage(ChatColor.GOLD + "Tienes: " + ChatColor.GREEN + formatea.format(dinero) + " PC");
        } else {
            p.sendMessage(ChatColor.GOLD + "Tienes: " + ChatColor.RED + "0 PC");
        }
    }

    //----------pagar dinero-------------
    public void pagarPixelCoins(Player p, String tname, String scantidad) {
        int cantidad = 0;
        boolean done = false;
        Player tp = p.getServer().getPlayerExact(tname);

        try {
            cantidad = Integer.parseInt(scantidad);
            if (tp != null) {
                done = true;
            } else {
                p.sendMessage(ChatColor.DARK_RED + "El juagdor no esta online.");
            }
        } catch (NumberFormatException e) {
            p.sendMessage(ChatColor.DARK_RED + "Introduce un numero, no texto de tal manera: /pagar <nombre del jugador> <cantidad a pagar>");
        }
        if (done) {
            int pixelcoinsp = plugin.getConfig().getInt(p.getName());

            if (p.getName().equalsIgnoreCase(tname)) {
                p.sendMessage(ChatColor.DARK_RED + "tu crees q el que ha hecho el plugin es tonto o algo xd");
            } else if (pixelcoinsp == 0) {
                p.sendMessage(ChatColor.DARK_RED + "No tienes dinero :(");
            } else if (cantidad > pixelcoinsp) {
                p.sendMessage(ChatColor.DARK_RED + "No puedes dar mas dinero del que tienes :v");
            } else {
                cantidad = Integer.parseInt(scantidad);
                int pixelcoinst = plugin.getConfig().getInt(tname);
                int perdida = pixelcoinsp - cantidad;
                int ganancia = pixelcoinst + cantidad;

                plugin.getConfig().set(p.getName(), perdida);
                plugin.saveConfig();
                p.sendMessage(ChatColor.GOLD + "Has pagado: " + ChatColor.RED + formatea.format(cantidad) + " PC." + ChatColor.GOLD + " Ahora tienes: " + ChatColor.GREEN + formatea.format(perdida) + " PC");

                plugin.getConfig().set(tname, ganancia);
                plugin.saveConfig();
                tp.playSound(tp.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
                tp.sendMessage(ChatColor.GOLD + p.getName() + " te ha pagado: " + ChatColor.GREEN + "+" + formatea.format(cantidad) + " PC " + ChatColor.GOLD + "Ahora tienes: " + ChatColor.GREEN + formatea.format(ganancia) + " PC");

            }
        }
    }

    //----------top ricos----------------
    public void mostarTopRicos(Player p) {
        ArrayList<Jugador> jugadores = new ArrayList<Jugador>();

        for (String key : plugin.getConfig().getKeys(false)) {
            int value;
            value = plugin.getConfig().getInt(key);

            jugadores.add(new Jugador(value, key));
        }

        Collections.sort(jugadores, new Comparator<Object>() {
            @Override
            public int compare(Object j1, Object j2) {
                return new Integer(((Jugador) j2).getCantidad()).compareTo(new Integer(((Jugador) j1).getCantidad()));
            }
        });

        p.sendMessage("    ");
        for (int i = 0; i < 3; i++) {
            Jugador j = jugadores.get(i);
            int puesto = i + 1;

            p.sendMessage(ChatColor.GOLD + "" + puesto + "� " + j.getNombre() + ": " + ChatColor.GREEN + formatea.format(j.getCantidad()) + " PC");
        }
    }

    //--------- top pobres------------------
    public void mostrarTopPobres(Player p) {
        ArrayList<Jugador> jugadores = new ArrayList<Jugador>();

        for (String key : plugin.getConfig().getKeys(false)) {
            int value;
            value = plugin.getConfig().getInt(key);

            jugadores.add(new Jugador(value, key));
        }

        Collections.sort(jugadores, new Comparator<Object>() {
            @Override
            public int compare(Object j1, Object j2) {
                return new Integer(((Jugador) j1).getCantidad()).compareTo(new Integer(((Jugador) j2).getCantidad()));
            }
        });
        p.sendMessage("    ");
        for (int i = 0; i < 3; i++) {
            Jugador j = jugadores.get(i);
            int puesto = i + 1;

            p.sendMessage(ChatColor.GOLD + "" + puesto + "� " + j.getNombre() + ": " + ChatColor.GREEN + formatea.format(j.getCantidad()) + " PC");
        }
    }

    //---------Ver dinero de otros jugadores------------
    public void verPixelCoins(Player p, String tname) {
        if (p.getName().equalsIgnoreCase("JaimeTruman") || p.getName().equalsIgnoreCase("Juanxli")) {
            int dinero = plugin.getConfig().getInt(tname);
            p.sendMessage(ChatColor.GOLD + "El dinero de " + tname + " : " + ChatColor.GREEN + formatea.format(dinero) + "PC");
        } else {
            p.sendMessage(ChatColor.DARK_RED + "No tienes permisos");
        }
    }

    //--------Poner dinero a otros jugadores---------
    public void setDinero(Player p, String tname, String scantidad) {
        boolean done = true;
        int cantidad = 0;

        try {
            cantidad = Integer.parseInt(scantidad);
        } catch (NumberFormatException e) {
            done = false;
            p.sendMessage(ChatColor.DARK_RED + "Introduce un numero, no texto de tal manera: /setdinero <nombre del jugador> <cantidad a poner>");
        }

        if (done == true) {
            Player tp = plugin.getServer().getPlayer(tname);

            plugin.getConfig().set(tname, cantidad);
            plugin.saveConfig();

            if (tp.isOnline() == true) {
                p.sendMessage(ChatColor.GOLD + "Has puesto a: " + tname + " la cantidad de: " + ChatColor.GREEN + cantidad + " PC");
                tp.sendMessage(ChatColor.GOLD + "" + p.getName() + " te ha puesto la cantidad de: " + ChatColor.GREEN + cantidad + " PC");
            } else {
                p.sendMessage(ChatColor.GOLD + "Has puesto a: " + tname + " la cantidad de: " + ChatColor.GREEN + cantidad + " PC");
            }
        }
    }
}
