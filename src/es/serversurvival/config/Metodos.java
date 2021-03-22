package es.serversurvival.config;

import java.text.DecimalFormat;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;


public class Metodos {
    private Plugin plugin = Pixelcoin.getPlugin(Pixelcoin.class);
    public DecimalFormat formatea = new DecimalFormat("###,###.##");
    int cambioDiamantes = 750;
    int cambioCuarzo = 10;

    Funciones f = new Funciones();

    //------------Diamantes --> PixelCoins-----------
    public void CambiarDiamantes(Player p) {
        Inventory in = p.getInventory();
        @SuppressWarnings("deprecation")
        ItemStack itemMano = p.getItemInHand();
        boolean done = false;

        for (int i = 0; i < 9; i++) {
            ItemStack itemI = in.getItem(i);
            if (itemMano.isSimilar(itemI) && itemMano.getType() == Material.DIAMOND) {
                int cantidad = itemMano.getAmount();
                int actual = plugin.getConfig().getInt(p.getName());

                plugin.getConfig().set(p.getName(), actual + (cantidad * cambioDiamantes));
                in.clear(i);
                p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
                plugin.saveConfig();
                p.sendMessage(ChatColor.GOLD + "Se ha a�adido: " + ChatColor.GREEN + formatea.format(cantidad * cambioDiamantes) + " PC " + ChatColor.GOLD + "Ahora tienes: " + ChatColor.GREEN +
                        formatea.format(plugin.getConfig().getInt(p.getName())) + " PC");
                done = true;

                break;
            }
        }
        if (done == false) {
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
            p.sendMessage(ChatColor.DARK_RED + "Necesitas tener diamantes en la mano para intercambiarlas por pixel coins");
        }
    }

    //----------Pixel Coins --> Diamantes----------
    @SuppressWarnings("deprecation")
    public void cambiarPixelCoinsDia(Player p) {
        int actuales = plugin.getConfig().getInt(p.getName());
        int slotsLibres = f.espaciosLibres(p.getInventory());
        Inventory in = p.getInventory();

        if (actuales >= cambioDiamantes && slotsLibres > 0) {
            in.addItem(new ItemStack(Material.DIAMOND));
            plugin.getConfig().set(p.getName(), actuales - cambioDiamantes);
            plugin.saveConfig();
            p.sendMessage(ChatColor.GOLD + "Se ha a�adido un diamante. " + ChatColor.RED + "-750 PC " + ChatColor.GOLD + "Quedan " + ChatColor.GREEN + formatea.format(actuales - 750) + " PC");
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
        } else if (slotsLibres == 0) {
            if (p.getItemInHand().getAmount() != 64 && p.getItemInHand().getType() == Material.DIAMOND) {
                in.addItem(new ItemStack(Material.DIAMOND));
                plugin.getConfig().set(p.getName(), actuales - cambioDiamantes);
                plugin.saveConfig();
                p.sendMessage(ChatColor.GOLD + "Se ha a�adido un diamante. " + ChatColor.RED + "-7500 PC " + ChatColor.GOLD + "Quedan " + ChatColor.GREEN + formatea.format(actuales - cambioDiamantes) + " PC");
                p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
            } else {
                p.sendMessage(ChatColor.DARK_RED + "Tienes el inventario lleno");
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
            }
        } else {
            p.sendMessage(ChatColor.DARK_RED + "Necesitas tener 750 o m�s pixel coins, para convertirlas a diamantes");
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
        }
    }

    //---------------Metedo maximas pixelcoins -> Maximos diamantes-------------
    public void cambiarMaxPixelCoinsDiamante(Player p) {
        Inventory in = p.getInventory();
        int actuales = plugin.getConfig().getInt(p.getName());
        int slotsLibres = f.espaciosLibres(in);

        if (actuales >= cambioDiamantes && slotsLibres > 0) {
            int convertibles = actuales - (actuales % cambioDiamantes);
            int diamantes = (convertibles / cambioDiamantes) % 9;
            int bloques = ((convertibles / cambioDiamantes) - diamantes) / 9;

            //int maxSlots = f.slotsBloques(bloques) + f.slotsBloques(diamantes);
            int[] slotsBloques = f.slotsItem(bloques, slotsLibres);

            int bloquesAactuales = 0;
            int diamantesA�actuales = 0;

            //A�adir bloques
            for (int i = 0; i < slotsBloques.length; i++) {
                bloquesA�actuales = bloquesA�actuales + slotsBloques[i];
                in.addItem(new ItemStack(Material.DIAMOND_BLOCK, slotsBloques[i]));
            }

            int[] slotsDiamantes = f.slotsItem(diamantes, f.espaciosLibres(in));
            //A�adir diamantes
            for (int i = 0; i < slotsDiamantes.length; i++) {
                diamantesA�actuales = diamantesA�actuales + slotsDiamantes[i];
                in.addItem(new ItemStack(Material.DIAMOND, slotsDiamantes[i]));
            }

            int coste = (bloquesAadidos * cambioDiamantes * 9)+(diamantesA�actuales * cambioDiamantes);
            plugin.getConfig().set(p.getName(), actuales - coste);
            plugin.saveConfig();

            p.sendMessage(ChatColor.GOLD + "Se ha a�adio: " + ChatColor.AQUA + "+" + bloquesA�actuales + " bloques " + "+" + diamantesA�actuales + " diamantes. " + ChatColor.RED + "-" + formatea.format(coste)
                    + ChatColor.GOLD + " Quedan: " + ChatColor.GREEN + plugin.getConfig().getInt(p.getName()) + " PC");
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);


        } else if (slotsLibres == 0) {
            p.sendMessage(ChatColor.DARK_RED + "Tienes el inventario lleno");
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
        } else {
            p.sendMessage(ChatColor.DARK_RED + "Necesitas tener 10 o m�s pixel coins, para convertirlas a diamantes");
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
        }

    }


    //---------Bloques diamante -> pixelcoins-------
    public void cambiarBloquesDiamante(Player p) {
        Inventory in = p.getInventory();
        @SuppressWarnings("deprecation")
        ItemStack itemMano = p.getItemInHand();
        boolean done = false;

        for (int i = 0; i < 9; i++) {
            ItemStack itemI = in.getItem(i);
            if (itemMano.isSimilar(itemI) && itemMano.getType() == Material.DIAMOND_BLOCK) {
                int cantidad = itemMano.getAmount();
                int actual = plugin.getConfig().getInt(p.getName());

                plugin.getConfig().set(p.getName(), actual + (cantidad * cambioDiamantes * 9));
                in.clear(i);
                p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
                plugin.saveConfig();
                p.sendMessage(ChatColor.GOLD + "Se ha a�adido: " + ChatColor.GREEN + formatea.format(cantidad * cambioDiamantes * 9) + " PC " + ChatColor.GOLD + "Ahora tienes: " + ChatColor.GREEN +
                        formatea.format(plugin.getConfig().getInt(p.getName())) + " PC");
                done = true;

                break;
            }
        }
        if (done == false) {
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
            p.sendMessage(ChatColor.DARK_RED + "Necesitas tener bloques de diamantes en la mano para intercambiarlas por pixel coins");
        }
    }

    //-------Cuarzo -> pixelcoins----------
    public void cambiarCuarzo(Player p) {
        Inventory in = p.getInventory();
        @SuppressWarnings("deprecation")
        ItemStack itemMano = p.getItemInHand();
        boolean done = false;

        for (int i = 0; i < 9; i++) {
            ItemStack itemI = in.getItem(i);
            if (itemMano.isSimilar(itemI) && itemMano.getType() == Material.QUARTZ_BLOCK) {
                int cantidad = itemMano.getAmount();
                int actual = plugin.getConfig().getInt(p.getName());

                plugin.getConfig().set(p.getName(), actual + (cantidad * cambioCuarzo));
                in.clear(i);
                p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
                plugin.saveConfig();
                p.sendMessage(ChatColor.GOLD + "Se ha a�adido: " + ChatColor.GREEN + formatea.format(cantidad * cambioCuarzo) + " PC " + ChatColor.GOLD + "Ahora tienes: " + ChatColor.GREEN +
                        formatea.format(plugin.getConfig().getInt(p.getName())) + " PC");
                done = true;

                break;
            }
        }
        if (done == false) {
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
            p.sendMessage(ChatColor.DARK_RED + "Necesitas tener bloques de cuarzo en la mano para intercambiarlas por pixel coins");
        }
    }


    //--------Pixelcoins -> cuarzo
    @SuppressWarnings("deprecation")
    public void cambiarPixelCoinsCua(Player p) {
        int actuales = plugin.getConfig().getInt(p.getName());
        int slotsLibres = f.espaciosLibres(p.getInventory());
        Inventory in = p.getInventory();

        if (actuales >= 10 && slotsLibres > 0) {

            in.addItem(new ItemStack(Material.QUARTZ_BLOCK));
            plugin.getConfig().set(p.getName(), actuales - cambioCuarzo);
            plugin.saveConfig();
            p.sendMessage(ChatColor.GOLD + "Se ha a�adido un bloque de cuarzo. " + ChatColor.RED + "-10 PC " + ChatColor.GOLD + "Quedan " + ChatColor.GREEN + formatea.format(actuales - cambioCuarzo) + " PC");
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
        } else if (slotsLibres == 0) {
            if (p.getItemInHand().getAmount() != 64 && p.getItemInHand().getType() == Material.QUARTZ_BLOCK) {
                in.addItem(new ItemStack(Material.QUARTZ_BLOCK));
                plugin.getConfig().set(p.getName(), actuales - cambioCuarzo);
                plugin.saveConfig();
                p.sendMessage(ChatColor.GOLD + "Se ha a�adido un bloque de cuarzo. " + ChatColor.RED + "-10 PC " + ChatColor.GOLD + "Quedan " + ChatColor.GREEN + formatea.format(actuales - cambioCuarzo) + " PC");
                p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
            } else {
                p.sendMessage(ChatColor.DARK_RED + "Tienes el inventario lleno");
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
            }
        } else {
            p.sendMessage(ChatColor.DARK_RED + "Necesitas tener 10 o m�s pixel coins, para convertirlas a cuarzo");
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
        }
    }

    //----------Maximas pixelcoins-> Maximos bloques de cuarzo-------
    public void cambiarMaxPixelCoinsCuarzo(Player p) {
        Inventory in = p.getInventory();
        int actuales = plugin.getConfig().getInt(p.getName());
        int slotsLibres = f.espaciosLibres(in);

        if (actuales >= cambioCuarzo && slotsLibres > 0) {
            int max = slotsLibres * 64;
            int noConvertibles = actuales % cambioCuarzo;
            int convertibles = (actuales - noConvertibles) / cambioCuarzo;
            int coste = convertibles * cambioCuarzo;
            ItemStack isB = new ItemStack(Material.QUARTZ_BLOCK);

            if (convertibles <= max) {
                isB.setAmount(convertibles);
                in.addItem(isB);
                plugin.getConfig().set(p.getName(), actuales - coste);
                plugin.saveConfig();
                p.sendMessage(ChatColor.GOLD + "Se ha a�adio: " + ChatColor.GRAY + "+" + formatea.format(convertibles) + " bloques de cuarzo " + ChatColor.RED + "-" + formatea.format(coste) + " PC"
                        + ChatColor.GOLD + " Quedan: " + ChatColor.GREEN + plugin.getConfig().getInt(p.getName()) + "PC");
                p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
            } else {
                isB.setAmount(convertibles);
                in.addItem(isB);
                plugin.getConfig().set(p.getName(), actuales - (max * cambioCuarzo));
                plugin.saveConfig();
                p.sendMessage(ChatColor.GOLD + "Se ha a�adio: " + ChatColor.GRAY + "+" + formatea.format(max) + " bloques de cuarzo " + ChatColor.RED + "-" + formatea.format(max * cambioCuarzo) + " PC"
                        + ChatColor.GOLD + " Quedan: " + ChatColor.GREEN + plugin.getConfig().getInt(p.getName()) + "PC");
                p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
            }
        } else if (slotsLibres == 0) {
            p.sendMessage(ChatColor.DARK_RED + "Tienes el inventario lleno");
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
        } else {
            p.sendMessage(ChatColor.DARK_RED + "Necesitas tener 10 o m�s pixel coins, para convertirlas a bloques de cuarzo");
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
        }
    }
}
