package es.serversurvival.objetos.menus;

import es.serversurvival.main.Funciones;
import es.serversurvival.objetos.mySQL.Jugadores;
import es.serversurvival.objetos.mySQL.PosicionesCerradas;
import es.serversurvival.objetos.mySQL.tablasObjetos.PosicionCerrada;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TopMenu extends Menu{
    private DecimalFormat formatea = new DecimalFormat("###,###.##");
    private Player player;
    private String titulo = ChatColor.DARK_RED + "" + ChatColor.BOLD + "              TOP";

    public TopMenu (Player player){
        this.player = player;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public String titulo() {
        return titulo;
    }

    @Override
    public void openMenu() {
        activeMenus.add(this);
        player.openInventory(buildInv());
    }

    private Inventory buildInv(){
        Inventory inventory = Bukkit.createInventory(null, 54, titulo);
        Jugadores j = new Jugadores();
        j.conectar();

        ItemStack topricos = new ItemStack(Material.GOLD_BLOCK);
        Map<String, Double> listaRicos = Funciones.crearMapaTopPlayers(false);
        ItemMeta itemMetaRicos = topricos.getItemMeta();
        itemMetaRicos.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "TOP RICOS");
        ArrayList<String> lore = new ArrayList<>();
        int pos = 1;
        for (Map.Entry<String, Double> entry : listaRicos.entrySet()) {
            if(pos == 6) break;
            lore.add(ChatColor.GOLD + "" + pos + "º " + entry.getKey() + ": " + ChatColor.GREEN + formatea.format(entry.getValue()) + " PC");
            pos++;
        }
        pos = 1;
        itemMetaRicos.setLore(lore);
        topricos.setItemMeta(itemMetaRicos);

        ItemStack toppobres = new ItemStack(Material.DIRT);
        HashMap<String, Double> listaPobres = j.getTop5PlayersMenosPixelcoins();
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
        HashMap<String, Integer> listaVendedores = j.getTop5PlayersNVentas();
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
        HashMap<String, Integer> listaFiables = j.getTop5PlayerFiables();
        ItemMeta itemMetaFia = topfiables.getItemMeta();
        itemMetaFia.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "TOP FIABLES");
        ArrayList<String> lore4 = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : listaFiables.entrySet()) {
            if(pos == 6) break;
            lore4.add(ChatColor.GOLD + "" + pos + "º " + entry.getKey() + ": " + ChatColor.GREEN + entry.getValue());
            pos++;
        }
        pos = 1;
        itemMetaFia.setLore(lore4);
        topfiables.setItemMeta(itemMetaFia);

        PosicionesCerradas posicionesCerradas = new PosicionesCerradas();
        ItemStack topOperaciones = new ItemStack(Material.DIAMOND_BLOCK);
        List<PosicionCerrada> posicionCerradaList = posicionesCerradas.getTop5GlobalRentabilidadesID();
        ItemMeta itemMetaRent = topOperaciones.getItemMeta();
        itemMetaRent.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "TOP MEJORES OPERAIONES BOLSA");
        ArrayList<String> lore6 = new ArrayList<>();
        double rentabilidad;
        for (PosicionCerrada posicionCerrada : posicionCerradaList) {
            if(pos == 6) break;
            rentabilidad = posicionCerrada.getRentabilidad();
            rentabilidad = Funciones.redondeoDecimales(rentabilidad, 3);
            if (rentabilidad <= 0) {
                continue;
            }
            lore6.add("" + ChatColor.GOLD + pos + "º " + posicionCerrada.getJugador() + ": " + posicionCerrada.getNombre() + ChatColor.GREEN + " +" + rentabilidad + "%");
            pos++;
        }
        itemMetaRent.setLore(lore6);
        topOperaciones.setItemMeta(itemMetaRent);
        pos = 1;

        ItemStack topmenosfiables = new ItemStack(Material.RED_WOOL);
        HashMap<String, Integer> listaMenosFiables = j.getTop5PlayersMenosFiables();
        ItemMeta itemMetaNFia = topmenosfiables.getItemMeta();
        itemMetaNFia.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "TOP MENOS FIABLES");
        ArrayList<String> lore5 = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : listaMenosFiables.entrySet()) {
            if(pos == 6) break;
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

    @Override
    public void closeMenu() {
        activeMenus.remove(this);
    }
}