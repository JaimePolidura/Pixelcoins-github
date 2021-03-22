package es.serversurvival.comandos.comandos;

import es.serversurvival.comandos.Comando;
import es.serversurvival.main.Funciones;
import es.serversurvival.objetos.mySQL.Jugador;
import es.serversurvival.objetos.mySQL.PosicionesCerradas;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;


public class TopComando extends Comando {
    Funciones f = new Funciones();
    private final String cnombre = "top";
    private final String sintaxis = "/top";
    private final String ayuda = "ver los jugadores top";
    public final static String titulo = ChatColor.DARK_RED + "" + ChatColor.BOLD + "              TOP";
    ;

    public String getCNombre() {
        return cnombre;
    }

    public String getSintaxis() {
        return sintaxis;
    }

    public String getAyuda() {
        return ayuda;
    }

    public void execute(Player player, String[] args) {
        Inventory inventory = this.crearInventario();
        player.openInventory(inventory);
    }

    private Inventory crearInventario() {
        Inventory inventory = Bukkit.createInventory(null, 54, titulo);
        Jugador j = new Jugador();
        j.conectar();

        ItemStack topricos = new ItemStack(Material.GOLD_BLOCK);
        HashMap<String, Double> listaRicos = j.getTop3PlayersPixelcoins();
        ItemMeta itemMetaRicos = topricos.getItemMeta();
        itemMetaRicos.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "TOP RICOS");
        ArrayList<String> lore = new ArrayList<>();
        int pos = 1;
        for (Map.Entry<String, Double> entry : listaRicos.entrySet()) {
            lore.add(ChatColor.GOLD + "" + pos + "º " + entry.getKey() + ": " + ChatColor.GREEN + formatea.format(entry.getValue()) + " PC");
            pos++;
        }
        pos = 1;
        itemMetaRicos.setLore(lore);
        topricos.setItemMeta(itemMetaRicos);

        ItemStack toppobres = new ItemStack(Material.DIRT);
        HashMap<String, Double> listaPobres = j.getTop3PlayersMenosPixelcoins();
        ItemMeta itemMetaPobres = toppobres.getItemMeta();
        itemMetaPobres.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "TOP POBRES");
        ArrayList<String> lore2 = new ArrayList<>();
        for (Map.Entry<String, Double> entry : listaPobres.entrySet()) {
            lore2.add(ChatColor.GOLD + "" + pos + "º " + entry.getKey() + ": " + ChatColor.GREEN + entry.getValue() + " PC");
            pos++;
        }
        pos = 1;
        itemMetaPobres.setLore(lore2);
        toppobres.setItemMeta(itemMetaPobres);

        ItemStack topvendores = new ItemStack(Material.GOLD_INGOT);
        HashMap<String, Integer> listaVendedores = j.getTop3PlayersNVentas();
        ItemMeta itemMetaVend = topvendores.getItemMeta();
        itemMetaVend.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "TOP VENDEDORES");
        ArrayList<String> lore3 = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : listaVendedores.entrySet()) {
            lore3.add(ChatColor.GOLD + "" + pos + "º " + entry.getKey() + ": " + ChatColor.GREEN + entry.getValue());
            pos++;
        }
        pos = 1;
        itemMetaVend.setLore(lore3);
        topvendores.setItemMeta(itemMetaVend);

        ItemStack topfiables = new ItemStack(Material.GREEN_WOOL);
        HashMap<String, Integer> listaFiables = j.getTop3PlayerFiables();
        ItemMeta itemMetaFia = topfiables.getItemMeta();
        itemMetaFia.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "TOP FIABLES");
        ArrayList<String> lore4 = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : listaFiables.entrySet()) {
            lore4.add(ChatColor.GOLD + "" + pos + "º " + entry.getKey() + ": " + ChatColor.GREEN + entry.getValue());
            pos++;
        }
        pos = 1;
        itemMetaFia.setLore(lore4);
        topfiables.setItemMeta(itemMetaFia);

        PosicionesCerradas posicionesCerradas = new PosicionesCerradas();
        ItemStack topOperaciones = new ItemStack(Material.DIAMOND_BLOCK);
        ArrayList<Integer> ids = posicionesCerradas.getTop3GlobalRentabilidadesID();
        ItemMeta itemMetaRent = topOperaciones.getItemMeta();
        itemMetaRent.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "TOP MEJORES OPERAIONES BOLSA");
        ArrayList<String> lore6 = new ArrayList<>();
        double rentabilidad;
        for (int id : ids) {
            rentabilidad = posicionesCerradas.getRentabilidad(id);
            rentabilidad = Funciones.redondeoDecimales(rentabilidad, 3);
            if (rentabilidad <= 0) {
                continue;
            }
            lore6.add("" + ChatColor.GOLD + pos + "º " + posicionesCerradas.getJugador(id) + ": " + posicionesCerradas.getTicker(id) + ChatColor.GREEN + " +" + rentabilidad + "%");
            pos++;
        }
        itemMetaRent.setLore(lore6);
        topOperaciones.setItemMeta(itemMetaRent);
        pos = 1;

        ItemStack topmenosfiables = new ItemStack(Material.RED_WOOL);
        HashMap<String, Integer> listaMenosFiables = j.getTop3PlayersMenosFiables();
        ItemMeta itemMetaNFia = topmenosfiables.getItemMeta();
        itemMetaNFia.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "TOP MENOS FIABLES");
        ArrayList<String> lore5 = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : listaMenosFiables.entrySet()) {
            lore5.add(ChatColor.GOLD + "" + pos + "º " + entry.getKey() + ": " + ChatColor.GREEN + entry.getValue());
            pos++;
        }
        itemMetaNFia.setLore(lore5);
        topmenosfiables.setItemMeta(itemMetaNFia);
        j.desconectar();

        inventory.setItem(10, topricos);
        inventory.setItem(13, topvendores);
        inventory.setItem(16, toppobres);
        inventory.setItem(37, topfiables);
        inventory.setItem(40, topOperaciones);
        inventory.setItem(43, topmenosfiables);

        return inventory;
    }
}
