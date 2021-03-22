package es.serversurvival.objetos.solicitudes;

import es.serversurvival.main.Funciones;
import es.serversurvival.objetos.mySQL.Deudas;
import es.serversurvival.objetos.mySQL.Transacciones;
import es.serversurvival.objetos.task.ScoreboardPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class PrestamosSolicitud extends Solicitud {
    public static String titulo = ChatColor.DARK_RED + "" + ChatColor.BOLD + "    Solicitud Prestamo";
    private Inventory inventory;
    private String enviador;
    private String destinatario;
    private int pixelcoins;
    private int dias;
    private int interes;

    public PrestamosSolicitud(String enviador, String destinatario, int pixelcoins, int dias, int interes) {
        this.enviador = enviador;
        this.destinatario = destinatario;
        this.pixelcoins = pixelcoins;
        this.dias = dias;
        this.interes = interes;
    }

    @Override
    public String getTitulo() {
        return titulo;
    }

    @Override
    public String getDestinatario() {
        return destinatario;
    }

    private Inventory contruirInventario() {
        inventory = Bukkit.createInventory(null, InventoryType.HOPPER, titulo);

        ItemStack aceptar = new ItemStack(Material.GREEN_WOOL);
        ItemMeta itemMetaAceptar = aceptar.getItemMeta();
        itemMetaAceptar.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "ACEPTAR");
        ArrayList<String> lore = new ArrayList<>();
        String desc = ChatColor.GOLD + "Prestamo de " + enviador + " de " + ChatColor.GREEN + formatea.format(pixelcoins) + ChatColor.GOLD + " a " + dias + " dias  con un interes del " + interes + "% (" + ChatColor.GREEN + formatea.format(Funciones.interes(pixelcoins, interes)) + " PC" + ChatColor.GOLD + ")";
        lore = Funciones.dividirDesc(lore, desc, 40);
        itemMetaAceptar.setLore(lore);
        aceptar.setItemMeta(itemMetaAceptar);

        ItemStack denegar = new ItemStack(Material.RED_WOOL);
        ItemMeta itemMetadenegar = denegar.getItemMeta();
        itemMetadenegar.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "DENEGAR");
        denegar.setItemMeta(itemMetadenegar);

        inventory.setItem(0, aceptar);
        inventory.setItem(4, denegar);

        return inventory;
    }

    @Override
    public void enviarSolicitud() {
        Player destinatario = Bukkit.getPlayer(this.destinatario);
        Player enviador = Bukkit.getPlayer(this.enviador);

        inventory = contruirInventario();
        destinatario.openInventory(inventory);
        enviador.sendMessage(ChatColor.GOLD + "Has enviado la solicitud");
        solicitudes.add(this);
    }

    @Override
    public void aceptar() {
        Player p = Bukkit.getPlayer(this.enviador);
        Player tp = Bukkit.getPlayer(this.destinatario);
        Transacciones t = new Transacciones();
        Deudas d = new Deudas();

        t.conectar();
        t.realizarTransferencia(enviador, destinatario, pixelcoins, "", Transacciones.TIPO.DEUDAS_PRIMERPAGO, false);
        d.nuevaDeuda(destinatario, enviador, Funciones.interes(pixelcoins, interes), dias, interes);
        t.desconectar();
        ScoreboardPlayer sp = new ScoreboardPlayer();

        tp.sendMessage(ChatColor.GOLD + "Has aceptado la solicitud de: " + ChatColor.GREEN + formatea.format(pixelcoins) + " PC " + ChatColor.GOLD + "con un interes del: " + ChatColor.GREEN + interes +
                ChatColor.GOLD + " a " + ChatColor.GREEN + dias + ChatColor.GOLD + " dias");

        if (p != null) {
            sp.updateScoreboard(p);
            p.sendMessage(ChatColor.GOLD + p.getName() + " Te ha aceptado la solicitud de deuda");
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
        }
        sp.updateScoreboard(tp);
        solicitudes.remove(this);
        tp.closeInventory();
    }

    @Override
    public void cancelar() {
        Player p = Bukkit.getPlayer(enviador);
        Player tp = Bukkit.getPlayer(destinatario);

        tp.sendMessage(ChatColor.GOLD + "Has cancelado la solicitud");

        if (p != null) {
            p.sendMessage(ChatColor.GOLD + tp.getName() + " te ha cancelado la solicitud!");
            p.playSound(tp.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
        }
        solicitudes.remove(this);
        tp.closeInventory();
    }
}