package es.serversurvival.eventos;

import es.serversurvival.comandos.subComandos.bolsa.ValoresBolsa;
import es.serversurvival.objetos.menus.EmpresasMenu;
import es.serversurvival.objetos.menus.OfertasMenu;
import es.serversurvival.objetos.mySQL.Transacciones;
import es.serversurvival.main.Funciones;
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
    private final Location DiaPix = new Location(null, -250, 66, -219);
    private final Location DiaBloPix = new Location(null, -255, 66, -219);
    private final Location PixDia = new Location(null, -265, 66, -219);
    private final Location MaxPixMaxDia = new Location(null, -260, 66, -219);
    private final Location CuaPix = new Location(null, -270, 66, -216);
    private final Location PixCua = new Location(null, -270, 66, -206);
    private final Location MaxPixMaxCua = new Location(null, -270, 66, -211);
    private final Location LapPix = new Location(null, -245, 66, -216);
    private final Location BloLapPix = new Location(null, -245, 66, -211);
    private final Location MaxPixMaxLap = new Location(null, -245, 66, -206);
    private final Location PixLap = new Location(null, -245, 66, -201);
    private final Location tienda = new Location(null, -270, 67, -201);
    private final Location empresas = new Location(null, -270, 67, -196);
    private final Location bolsa = new Location(null, -245, 67, -196);

    @EventHandler
    public void onPlayerInteract(PlayerInteractEntityEvent e) {
        EquipmentSlot es = e.getHand();
        Entity en = e.getRightClicked();
        Location lec = en.getLocation();
        Player p = e.getPlayer();

        if (es.equals(EquipmentSlot.HAND) && en instanceof WitherSkeleton) {
            Transacciones t = new Transacciones();
            ItemStack im = p.getInventory().getItemInMainHand();
            int espaciosLibres = Funciones.espaciosLibres(p.getInventory());

            if (Funciones.comparar(lec, DiaPix)) {

                if (im.getType() == Material.DIAMOND) {
                    int slot = p.getInventory().getHeldItemSlot();
                    t.conectar();
                    t.ingresarItem(im, p, slot);
                    t.desconectar();
                } else {
                    p.sendMessage(ChatColor.DARK_RED + "Necesitas tener un diamante en la mano para cambiarlo a pixelcoins");
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
                }

            } else if (Funciones.comparar(lec, DiaBloPix)) {

                if (im.getType() == Material.DIAMOND_BLOCK) {
                    int slot = p.getInventory().getHeldItemSlot();
                    t.conectar();
                    t.ingresarItem(im, p, slot);
                    t.desconectar();
                } else {
                    p.sendMessage(ChatColor.DARK_RED + "Necesitas tener un bloque de diamante en la mano para cambiarlo a pixelcoins");
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
                }

            } else if (Funciones.comparar(lec, PixDia)) {

                if (espaciosLibres != 0 || (p.getInventory().getItemInMainHand().getType() == Material.DIAMOND && p.getInventory().getItemInMainHand().getAmount() != 64)) {
                    t.conectar();
                    t.sacarItem(p, "DIAMOND");
                    t.desconectar();
                } else {
                    p.sendMessage(ChatColor.DARK_RED + "Tienes el inventario lleno");
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
                }

            } else if (Funciones.comparar(lec, MaxPixMaxDia)) {

                if (espaciosLibres != 0) {
                    t.conectar();
                    t.sacarMaxItem("DIAMOND", p);
                    t.desconectar();
                } else {
                    p.sendMessage(ChatColor.DARK_RED + "Tienes el inventario lleno");
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
                }

            } else if (Funciones.comparar(lec, CuaPix)) {

                if (im.getType() == Material.QUARTZ_BLOCK) {
                    int slot = p.getInventory().getHeldItemSlot();
                    t.conectar();
                    t.ingresarItem(im, p, slot);
                    t.desconectar();
                } else {
                    p.sendMessage(ChatColor.DARK_RED + "Necesitas tener un diamante en la mano para cambiarlo a pixelcoins");
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
                }

            } else if (Funciones.comparar(lec, PixCua)) {

                if (espaciosLibres != 0 || (p.getInventory().getItemInMainHand().getType() == Material.QUARTZ_BLOCK && p.getInventory().getItemInMainHand().getAmount() != 64)) {
                    t.conectar();
                    t.sacarItem(p, "QUARTZ_BLOCK");
                    t.desconectar();
                } else {
                    p.sendMessage(ChatColor.DARK_RED + "Tienes el inventario lleno");
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
                }

            } else if (Funciones.comparar(lec, MaxPixMaxCua)) {

                if (espaciosLibres != 0) {
                    t.conectar();
                    t.sacarMaxItem("QUARTZ_BLOCK", p);
                    t.desconectar();
                } else {
                    p.sendMessage(ChatColor.DARK_RED + "Tienes el inventario lleno");
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
                }

            } else if (Funciones.comparar(lec, LapPix)) {
                if (im.getType() == Material.LAPIS_LAZULI) {
                    int slot = p.getInventory().getHeldItemSlot();
                    t.conectar();
                    t.ingresarItem(im, p, slot);
                    t.desconectar();
                } else {
                    p.sendMessage(ChatColor.DARK_RED + "Necesitas tener lapislazuli en la mano para cambiarlo a pixelcoins");
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
                }

            } else if (Funciones.comparar(lec, BloLapPix)) {
                if (im.getType() == Material.LAPIS_BLOCK) {
                    int slot = p.getInventory().getHeldItemSlot();
                    t.conectar();
                    t.ingresarItem(im, p, slot);
                    t.desconectar();
                } else {
                    p.sendMessage(ChatColor.DARK_RED + "Necesitas tener 1 bloque de lapislazuli para cambiarlo a pixelcoins");
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
                }

            } else if (Funciones.comparar(lec, MaxPixMaxLap)) {
                if (espaciosLibres != 0) {
                    t.conectar();
                    t.sacarMaxItem("LAPIS_LAZULI", p);
                    t.desconectar();
                } else {
                    p.sendMessage(ChatColor.DARK_RED + "Tienes el inventario lleno");
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
                }

            } else if (Funciones.comparar(lec, PixLap)) {
                if (espaciosLibres != 0 || (p.getInventory().getItemInMainHand().getType() == Material.LAPIS_LAZULI && p.getInventory().getItemInMainHand().getAmount() != 64)) {
                    t.conectar();
                    t.sacarItem(p, "LAPIS_LAZULI");
                    t.desconectar();
                } else {
                    p.sendMessage(ChatColor.DARK_RED + "Tienes el inventario lleno");
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
                }
            }
        }
        if (Funciones.comparar(lec, tienda)) {
            OfertasMenu ofertasMenu = new OfertasMenu(p);
            ofertasMenu.openMenu();
        } else if (Funciones.comparar(lec, empresas)) {
            EmpresasMenu empresasMenu = new EmpresasMenu(p);
            empresasMenu.openMenu();
        } else if (Funciones.comparar(lec, bolsa)) {
            ValoresBolsa valoresBolsaSubComando = new ValoresBolsa();
            valoresBolsaSubComando.execute(p, null);
        }
    }
}