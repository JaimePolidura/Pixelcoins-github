package es.serversurvival.eventos;

import es.serversurvival.objetos.Empresas;
import es.serversurvival.objetos.Ofertas;
import es.serversurvival.objetos.Transacciones;
import es.serversurvival.config.Funciones;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class PlayerInteract implements Listener {
    private static Location DiaPix = new Location(null, -250, 66, -219);
    private static Location DiaBloPix = new Location(null, -255, 66, -219);
    private static Location PixDia = new Location(null, -265, 66, -219);
    private static Location MaxPixMaxDia = new Location(null, -260, 66, -219);
    private static Location CuaPix = new Location(null, -270, 66, -216);
    private static Location PixCua = new Location(null, -270, 66, -206);
    private static Location MaxPixMaxCua = new Location(null, -270, 66, -211);
    private static Location LapPix = new Location(null, -245, 66, -216);
    private static Location BloLapPix = new Location(null, -245, 66, -211);
    private static Location MaxPixMaxLap = new Location(null, -245, 66, -206);
    private static Location PixLap = new Location(null, -245, 66, -201);
    private static Location tienda = new Location(null, -270, 67, -201);
    private static Location empresas = new Location(null, -270, 67, -196);

    private Funciones f = new Funciones();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEntityEvent e) {
        EquipmentSlot es = e.getHand();
        Entity en = e.getRightClicked();
        Location lec = en.getLocation();
        Player p = e.getPlayer();

        try {
            if (es.equals(EquipmentSlot.HAND) && en instanceof WitherSkeleton) {
                Transacciones t = new Transacciones();
                ItemStack im = p.getInventory().getItemInMainHand();
                int espaciosLibres = f.espaciosLibres(p.getInventory());

                if (f.comparar(lec, DiaPix)) {

                    if (im.getType() == Material.DIAMOND) {
                        int slot = p.getInventory().getHeldItemSlot();
                        t.conectar("root", "", "pixelcoins");
                        t.ingresarItem(im, p, slot);
                        t.desconectar();
                    } else {
                        p.sendMessage(ChatColor.DARK_RED + "Necesitas tener un diamante en la mano para cambiarlo a pixelcoins");
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
                    }

                } else if (f.comparar(lec, DiaBloPix)) {

                    if (im.getType() == Material.DIAMOND_BLOCK) {
                        int slot = p.getInventory().getHeldItemSlot();
                        t.conectar("root", "", "pixelcoins");
                        t.ingresarItem(im, p, slot);
                        t.desconectar();
                    } else {
                        p.sendMessage(ChatColor.DARK_RED + "Necesitas tener un bloque de diamante en la mano para cambiarlo a pixelcoins");
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
                    }

                } else if (f.comparar(lec, PixDia)) {

                    if (espaciosLibres != 0 || (p.getInventory().getItemInMainHand().getType() == Material.DIAMOND && p.getInventory().getItemInMainHand().getAmount() != 64)) {
                        t.conectar("root", "", "pixelcoins");
                        t.sacarItem(p, "DIAMOND");
                        t.desconectar();
                    } else {
                        p.sendMessage(ChatColor.DARK_RED + "Tienes el inventario lleno");
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
                    }

                } else if (f.comparar(lec, MaxPixMaxDia)) {

                    if (espaciosLibres != 0) {
                        t.conectar("root", "", "pixelcoins");
                        t.sacarMaxItem("DIAMOND", p);
                        t.desconectar();
                    } else {
                        p.sendMessage(ChatColor.DARK_RED + "Tienes el inventario lleno");
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
                    }

                } else if (f.comparar(lec, CuaPix)) {

                    if (im.getType() == Material.QUARTZ_BLOCK) {
                        int slot = p.getInventory().getHeldItemSlot();
                        t.conectar("root", "", "pixelcoins");
                        t.ingresarItem(im, p, slot);
                        t.desconectar();
                    } else {
                        p.sendMessage(ChatColor.DARK_RED + "Necesitas tener un diamante en la mano para cambiarlo a pixelcoins");
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
                    }

                } else if (f.comparar(lec, PixCua)) {

                    if (espaciosLibres != 0 || (p.getInventory().getItemInMainHand().getType() == Material.QUARTZ_BLOCK && p.getInventory().getItemInMainHand().getAmount() != 64)) {
                        t.conectar("root", "", "pixelcoins");
                        t.sacarItem(p, "QUARTZ_BLOCK");
                        t.desconectar();
                    } else {
                        p.sendMessage(ChatColor.DARK_RED + "Tienes el inventario lleno");
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
                    }

                } else if (f.comparar(lec, MaxPixMaxCua)) {

                    if (espaciosLibres != 0) {
                        t.conectar("root", "", "pixelcoins");
                        t.sacarMaxItem("QUARTZ_BLOCK", p);
                        t.desconectar();
                    } else {
                        p.sendMessage(ChatColor.DARK_RED + "Tienes el inventario lleno");
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
                    }

                } else if (f.comparar(lec, LapPix)) {

                    if (im.getType() == Material.LAPIS_LAZULI) {
                        int slot = p.getInventory().getHeldItemSlot();
                        t.conectar("root", "", "pixelcoins");
                        t.ingresarItem(im, p, slot);
                        t.desconectar();
                    } else {
                        p.sendMessage(ChatColor.DARK_RED + "Necesitas tener lapislazuli en la mano para cambiarlo a pixelcoins");
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
                    }

                } else if (f.comparar(lec, BloLapPix)) {

                    if (im.getType() == Material.LAPIS_BLOCK) {
                        int slot = p.getInventory().getHeldItemSlot();
                        t.conectar("root", "", "pixelcoins");
                        t.ingresarItem(im, p, slot);
                        t.desconectar();
                    } else {
                        p.sendMessage(ChatColor.DARK_RED + "Necesitas tener 1 bloque de lapislazuli para cambiarlo a pixelcoins");
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
                    }

                } else if (f.comparar(lec, MaxPixMaxLap)) {

                    if (espaciosLibres != 0) {
                        t.conectar("root", "", "pixelcoins");
                        t.sacarMaxItem("LAPIS_LAZULI", p);
                        t.desconectar();
                    } else {
                        p.sendMessage(ChatColor.DARK_RED + "Tienes el inventario lleno");
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
                    }

                } else if (f.comparar(lec, PixLap)) {

                    if (espaciosLibres != 0 || (p.getInventory().getItemInMainHand().getType() == Material.LAPIS_LAZULI && p.getInventory().getItemInMainHand().getAmount() != 64)) {
                        t.conectar("root", "", "pixelcoins");
                        t.sacarItem(p, "LAPIS_LAZULI");
                        t.desconectar();
                    } else {
                        p.sendMessage(ChatColor.DARK_RED + "Tienes el inventario lleno");
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
                    }

                }
            }
            if (f.comparar(lec, tienda)) {
                try {
                    Ofertas o = new Ofertas();
                    o.conectar("root", "", "pixelcoins");
                    o.mostarOfertas(p);
                    o.desconectar();
                } catch (Exception e3) {

                }
            } else if (f.comparar(lec, empresas)) {
                try {
                    Empresas empr = new Empresas();
                    empr.conectar("root", "", "pixelcoins");
                    empr.mostrarEmpresas(p);
                    empr.desconectar();
                } catch (Exception e1) {

                }
            }
        } catch (Exception e2) {

        }
    }
}
