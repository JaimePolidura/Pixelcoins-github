package es.serversurvival.config;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;


public class Funciones {

    //Conseguir los espacios libres de un jugador
    public int espaciosLibres(Inventory inventory) {
        int el = 36;
        for (ItemStack is : inventory.getContents()) {
            if (is == null) {
                continue;
            } else {
                el--;
            }
        }
        return el;
    }

    //Comparar dos locations
    public boolean comparar(Location loc1, Location loc2) {
        if (loc1.getBlockX() == loc2.getBlockX() && loc1.getBlockY() == loc2.getBlockY() && loc1.getBlockZ() == loc2.getBlockZ()) {
            return true;
        } else {
            return false;
        }
    }

    //Delvolver con array todos los slots libres que ocupara un numero de bloques
    public int[] slotsItem(int n, int slotsLibres) {
        int[] arr = new int[slotsLibres];

        for (int i = 0; i < slotsLibres; i++) {
            if (n - 64 > 0) {
                arr[i] = 64;
                n = n - 64;
            } else {
                arr[i] = n;
                break;
            }
        }
        return arr;
    }

    //Aumentar cantidad en un %
    public int interes(int num, int interes) {
        double n = (double) num;
        double i = (double) interes;

        double r = Math.round((n / 100) * i);
        int resultado = num + (int) r;

        return resultado;
    }

    public ArrayList<String> dividirDesc(ArrayList<String> lore, String descripcion, int k) {
        char[] descripcionChar = descripcion.toCharArray();
        String a = "";
        int pos = 0;
        int longitud = descripcionChar.length;
        int posRestantes = longitud;

        if (posRestantes >= k) {
            for (int i = 0; i < longitud; i++) {
                if (posRestantes >= k) {
                    for (int j = 0; j < k; j++) {
                        posRestantes--;
                        a = a + descripcionChar[pos];
                        pos++;
                    }
                    lore.add(ChatColor.GOLD + a);
                    a = "";
                } else {
                    for (int j = 0; j < posRestantes; j++) {
                        a = a + descripcionChar[pos];
                        pos++;
                    }
                    lore.add(ChatColor.GOLD + a);
                    a = "";
                    break;
                }
            }
        } else {
            for (int i = 0; i < longitud; i++) {
                a = a + descripcionChar[pos];
                pos++;
            }
            lore.add(ChatColor.GOLD + a);
        }
        return lore;
    }

    public int diferenciaDias(Date d1, Date d2) {
        long difMil = Math.abs(d1.getTime() - d2.getTime());
        long dif = TimeUnit.DAYS.convert(difMil, TimeUnit.MILLISECONDS);

        return (int) dif;
    }
}