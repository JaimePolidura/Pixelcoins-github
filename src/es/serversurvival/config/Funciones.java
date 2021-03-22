package es.serversurvival.config;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class Funciones {

    //conseguir espacios libres en el inventario
    public int espaciosLibres(Inventory in) {
        int i = 36;
        for (ItemStack is : in.getContents()) {
            if (is == null) {
                continue;
            }
            i--;
        }
        return i;
    }

    //conseguir los slots que ocuparan un numero de bloques
    public int slotsBloques(int bloques) {
        int decimal = bloques % 64;
        int entera = (bloques - decimal) / 64;

        if (decimal == 0) {
            return entera;
        } else {
            return entera + 1;
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
}