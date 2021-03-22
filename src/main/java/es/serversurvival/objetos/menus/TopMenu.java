package es.serversurvival.objetos.menus;

import es.serversurvival.main.Funciones;
import es.serversurvival.objetos.mySQL.Jugadores;
import es.serversurvival.objetos.mySQL.PosicionesCerradas;
import es.serversurvival.objetos.mySQL.tablasObjetos.Jugador;
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
import java.util.List;
import java.util.Map;

public class TopMenu extends Menu{
    private Jugadores jugadoresMySQL = new Jugadores();
    private PosicionesCerradas posicionesCerradasMySQL = new PosicionesCerradas();
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

    @Override
    public void closeMenu() {
        activeMenus.remove(this);
    }

    private Inventory buildInv(){
        Inventory inventory = Bukkit.createInventory(null, 54, titulo);

        jugadoresMySQL.conectar();
        ItemStack itemStackTopRicos = buildTopRicosJugadoresItem();
        ItemStack itemStackTopPobres = buildTopPobresJugadoresItem();
        ItemStack itemStackTopVendedores = buildTopVendedoresJugadoresItem();
        ItemStack itemStackTopFiables = buildTopFiablesJugadoresItem();
        ItemStack itemStackTopMenosFiables = buildTopMenosFiablesJugadoresItem();
        ItemStack itemStackTopOperaciones = buildItemTopOperacionesBolsa();
        jugadoresMySQL.desconectar();

        inventory.setItem(10, itemStackTopRicos);
        inventory.setItem(13, itemStackTopVendedores);
        inventory.setItem(16, itemStackTopPobres);
        inventory.setItem(37, itemStackTopFiables);
        inventory.setItem(40, itemStackTopOperaciones);
        inventory.setItem(43, itemStackTopMenosFiables);

        return inventory;
    }

    private ItemStack buildTopRicosJugadoresItem () {
        Map<String, Double> listaRicos = Funciones.crearMapaTopPlayers(false);
        ItemStack topRicos = new ItemStack(Material.GOLD_BLOCK);
        ItemMeta topRicosItemMeta = topRicos.getItemMeta();

        topRicosItemMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "TOP RICOS");

        List<String> lore = new ArrayList<>();
        int pos = 1;
        for(Map.Entry<String, Double> entry : listaRicos.entrySet()){
            if(pos == 6) break;

            lore.add(ChatColor.GOLD + "" + pos + "º " + entry.getKey() + ": " + ChatColor.GREEN + formatea.format(entry.getValue()) + " PC");
            pos++;
        }
        topRicosItemMeta.setLore(lore);
        topRicos.setItemMeta(topRicosItemMeta);

        return topRicos;
    }

    private ItemStack buildTopPobresJugadoresItem () {
        Map<String, Double> listaRicos = Funciones.crearMapaTopPlayers(true);
        ItemStack topPobres = new ItemStack(Material.DIRT);
        ItemMeta topPobresItemMeta = topPobres.getItemMeta();

        topPobresItemMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "TOP POBRES");

        List<String> lore = new ArrayList<>();
        int pos = 1;
        for(Map.Entry<String, Double> entry : listaRicos.entrySet()){
            if(pos == 6) break;

            lore.add(ChatColor.GOLD + "" + pos + "º " + entry.getKey() + ": " + ChatColor.GREEN + formatea.format(entry.getValue()) + " PC");
            pos++;
        }
        topPobresItemMeta.setLore(lore);
        topPobres.setItemMeta(topPobresItemMeta);

        return topPobres;
    }

    private ItemStack buildTopVendedoresJugadoresItem () {
        ItemStack topVendedores = new ItemStack(Material.GOLD_INGOT);
        ItemMeta topVendedoresItemMeta = topVendedores.getItemMeta();
        List<Jugador> listaVendedores = jugadoresMySQL.getTopVendedores();

        topVendedoresItemMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "TOP VENDEDORES");
        List<String> lore = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            lore.add(ChatColor.GOLD + "" + (i + 1)  + "º " + listaVendedores.get(i).getNombre() + ": " + ChatColor.GREEN + formatea.format(listaVendedores.get(i).getNventas()));
        }
        topVendedoresItemMeta.setLore(lore);
        topVendedores.setItemMeta(topVendedoresItemMeta);

        return topVendedores;
    }

    private ItemStack buildTopFiablesJugadoresItem () {
        ItemStack topFiables = new ItemStack(Material.GREEN_WOOL);
        ItemMeta topFiablesItemMeta = topFiables.getItemMeta();
        List<Jugador> listaFiables = jugadoresMySQL.getTopFiables();

        topFiablesItemMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "TOP MENOS MOROSOS");
        List<String> lore = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            lore.add(ChatColor.GOLD + "" + (i + 1)  + "º " + listaFiables.get(i).getNombre() + ": " + ChatColor.GREEN + formatea.format(listaFiables.get(i).getNpagos()));
        }
        topFiablesItemMeta.setLore(lore);
        topFiables.setItemMeta(topFiablesItemMeta);

        return topFiables;
    }

    private ItemStack buildTopMenosFiablesJugadoresItem () {
        ItemStack topMenosFiables = new ItemStack(Material.RED_WOOL);
        ItemMeta topMenosFiablesItemMeta = topMenosFiables.getItemMeta();
        List<Jugador> listaMenosFiables = jugadoresMySQL.getTopMenosFiables();

        topMenosFiablesItemMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "TOP MOROSOS");
        List<String> lore = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            lore.add(ChatColor.GOLD + "" + (i + 1)  + "º " + listaMenosFiables.get(i).getNombre() + ": " + ChatColor.GREEN + formatea.format(listaMenosFiables.get(i).getNpagos()));
        }
        topMenosFiablesItemMeta.setLore(lore);
        topMenosFiables.setItemMeta(topMenosFiablesItemMeta);

        return topMenosFiables;
    }

    private ItemStack buildItemTopOperacionesBolsa () {
        ItemStack topOperaciones = new ItemStack(Material.DIAMOND_BLOCK);
        ItemMeta topOperacionesItemMeta = topOperaciones.getItemMeta();
        List<PosicionCerrada> todasPosicionesCerradas = posicionesCerradasMySQL.getTopRentabilidades(5);

        topOperacionesItemMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "TOP MEJORES OPERAIONES BOLSA");
        List<String> lore6 = new ArrayList<>();
        for(int i = 0; i < todasPosicionesCerradas.size(); i++){
            double rentabilidad = Funciones.redondeoDecimales(todasPosicionesCerradas.get(i).getRentabilidad(), 3);
            if (rentabilidad > 0) {
                lore6.add("" + ChatColor.GOLD + (i + 1)  + "º " + todasPosicionesCerradas.get(i).getJugador() + ": " + todasPosicionesCerradas.get(i).getNombre() + ChatColor.GREEN + " +" + rentabilidad + "%");
            }
        }
        topOperacionesItemMeta.setLore(lore6);
        topOperaciones.setItemMeta(topOperacionesItemMeta);

        return topOperaciones;
    }
}