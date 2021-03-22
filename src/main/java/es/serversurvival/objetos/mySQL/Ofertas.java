package es.serversurvival.objetos.mySQL;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.*;

import es.serversurvival.objetos.menus.Menu;
import es.serversurvival.objetos.menus.OfertasMenu;
import es.serversurvival.objetos.menus.Refreshcable;
import es.serversurvival.objetos.mySQL.tablasObjetos.Encantamiento;
import es.serversurvival.objetos.mySQL.tablasObjetos.Jugador;
import es.serversurvival.objetos.mySQL.tablasObjetos.Oferta;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;


import es.serversurvival.main.Pixelcoin;

/**
 * 363 -> 193
 */
public class Ofertas extends MySQL {
    public final static int maxEspacios = 7;
    public final static String NOMBRE_ITEM_RETIRAR = ChatColor.RED + "" + ChatColor.BOLD + "CLICK PARA RETIRAR";
    public final static String NOMBRE_ITEM_COMPRAR = ChatColor.AQUA + "" + ChatColor.BOLD + "CLICK PARA COMPRAR";
    private final static List<String> bannedItems = Arrays.asList("POTION", "BANNER", "SPLASH_POTION", "LINGERING_POTION");
    private static DecimalFormat formatea = new DecimalFormat("###,###.##");

    public static boolean estaBaneado (String item) {
        return bannedItems.stream().anyMatch( (ite) -> ite.equalsIgnoreCase(item));
    }

    private void nuevaOferta(String nombre, String objeto, int cantidad, double precio, int durabilidad) {
        executeUpdate("INSERT INTO ofertas (nombre, objeto, cantidad, precio, durabilidad) VALUES ('" + nombre + "','" + objeto + "','" + cantidad + "','" + precio + "','" + durabilidad + "')");
    }

    private void borrarOferta(int id_oferta) {
        executeUpdate("DELETE FROM ofertas WHERE id_oferta=\"" + id_oferta + "\"      ");
    }

    public void setPrecio(int id, double precio){
        executeUpdate("UPDATE ofertas SET precio = '"+precio+"' WHERE id_oferta = '"+id+"'");
    }

    public void setCantidad(int id, int cantidad) {
        executeUpdate(String.format("UPDATE ofertas SET cantidad = '%d' WHERE id_oferta = '%d'", cantidad, id));
    }

    public Oferta getOferta (int id_oferta) {
        try {
            ResultSet rs = executeQuery(String.format("SELECT * FROM ofertas WHERE id_oferta = '%d'", id_oferta));
            rs.next();
            return buildOfertaByResultset(rs);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

    public List<Oferta> getTodasOfertas(){
        List<Oferta> ofertas = new ArrayList<>();
        try{
            ResultSet rs = executeQuery("SELECT * FROM ofertas");
            while (rs.next()){
                ofertas.add(buildOfertaByResultset(rs));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return ofertas;
    }

    public List<Oferta> getOfertasJugador (String nombreJugador){
        List<Oferta> ofertas = new ArrayList<>();
        try{
            ResultSet rs = executeQuery(String.format("SELECT * FROM ofertas WHERE nombre = '%s'", nombreJugador));
            while (rs.next()){
                ofertas.add(buildOfertaByResultset(rs));
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return ofertas;
    }

    public void borrarOferta(int id, String nombreJugador) {
        Encantamientos encantamientosMySQL = new Encantamientos();
        Jugadores jugadoresMySQL = new Jugadores();

        encantamientosMySQL.borrarEncantamientosOferta(id);
        borrarOferta(id);

        int espacios = jugadoresMySQL.getEspacios(nombreJugador) - 1;
        jugadoresMySQL.setEspacios(nombreJugador, espacios);
    }

    public void crearOferta(ItemStack itemAVender, Player jugadorPlayer, double precio) {
        Jugadores jugadoresMySQL = new Jugadores();
        Encantamientos encantamientosMySQL = new Encantamientos();
        Inventory inventarioJugador = jugadorPlayer.getInventory();
        String nombreJugador = jugadorPlayer.getName();

        Jugador jugador = jugadoresMySQL.getJugador(jugadorPlayer.getName());
        if(jugador == null){
            jugadoresMySQL.nuevoJugador(nombreJugador, 0, 1, 0, 0, 0, 0, 0, 0);
        }else {
            if (jugador.getEspacios() >= maxEspacios) {
                jugadorPlayer.sendMessage(ChatColor.DARK_RED + "Solo puedes tener "+maxEspacios+" objetos a la vez en la tienda");
                return;
            }else{
                jugadoresMySQL.setEspacios(nombreJugador, jugador.getEspacios() + 1);
            }
        }

        nuevaOferta(nombreJugador, itemAVender.getType().toString(), itemAVender.getAmount(), precio, itemAVender.getDurability());

        int idOfetaNueva = this.getMaxId();
        Map<Enchantment, Integer> encantamientos = getEncantamientosDeItem(itemAVender);
        encantamientosMySQL.insertarEncantamientosDeItem(encantamientos, idOfetaNueva);

        Menu.refreshAll();

        int slot = jugadorPlayer.getInventory().getHeldItemSlot();
        inventarioJugador.clear(slot);

        jugadorPlayer.sendMessage(ChatColor.GOLD + "Se ha a?adido a la tienda. Para retirarlos /tienda y clikc izquierdo en ellos");
        jugadorPlayer.playSound(jugadorPlayer.getLocation(), Sound.ENTITY_VILLAGER_YES, 10, 1);
        Bukkit.getServer().broadcastMessage(ChatColor.GOLD + nombreJugador + " ha a?adido un objeto a la tienda por: " + ChatColor.GREEN + formatea.format(precio) + " PC");
    }

    public ItemStack getItemOferta(int id) {
        Encantamientos encantamientosMySQL = new Encantamientos();
        Oferta ofertaAConvertir = getOferta(id);

        ItemStack itemAdevolver = new ItemStack(Material.getMaterial(ofertaAConvertir.getObjeto()), ofertaAConvertir.getCantidad());
        itemAdevolver.setDurability((short) ofertaAConvertir.getDurabilidad());

        ItemMeta itemMeta = itemAdevolver.getItemMeta();
        List<Encantamiento> encantamientos = encantamientosMySQL.getEncantamientosOferta(id);

        for(Encantamiento encantamiento : encantamientos){
            Enchantment encantamientoAPoner = Enchantment.getByName(encantamiento.getEncantamiento());
            int nivel = encantamiento.getNivel();

            itemMeta.addEnchant(encantamientoAPoner, nivel, true);
        }
        itemAdevolver.setItemMeta(itemMeta);

        return itemAdevolver;
    }

    public void retirarOferta(Player jugadorPlayer, int idARetirar) {
        ItemStack itemARetirar = this.getItemOferta(idARetirar);

        borrarOferta(idARetirar, jugadorPlayer.getName());

        jugadorPlayer.getInventory().addItem(itemARetirar);
        Menu.refreshAll();
        jugadorPlayer.sendMessage(ChatColor.GOLD + "Objeto retirado!");
        jugadorPlayer.playSound(jugadorPlayer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
    }

    private Map<Enchantment, Integer> getEncantamientosDeItem (ItemStack item) {
        if (item.getType() == Material.ENCHANTED_BOOK) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
            return meta.getStoredEnchants();
        } else {
            return item.getEnchantments();
        }
    }

    private int getMaxId() {
        try {
            ResultSet rs = executeQuery("SELECT id_oferta FROM ofertas ORDER BY id_oferta DESC LIMIT 1");
            while (rs.next()) {
                return rs.getInt("id_oferta");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private Oferta buildOfertaByResultset (ResultSet rs) throws SQLException {
        return new Oferta(
                rs.getInt("id_oferta"),
                rs.getString("nombre"),
                rs.getString("objeto"),
                rs.getInt("cantidad"),
                rs.getDouble("precio"),
                rs.getInt("durabilidad")
        );
    }
}