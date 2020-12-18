package es.serversurvival.mySQL;

import java.sql.*;
import java.util.*;

import es.serversurvival.mySQL.tablasObjetos.Encantamiento;
import es.serversurvival.mySQL.tablasObjetos.Jugador;
import es.serversurvival.mySQL.tablasObjetos.Oferta;
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

/**
 * 363 -> 193
 */
public final class Ofertas extends MySQL {
    public final static Ofertas INSTANCE = new Ofertas();
    private Ofertas () {}

    public final static int maxEspacios = 90;
    public final static String NOMBRE_ITEM_RETIRAR = ChatColor.RED + "" + ChatColor.BOLD + "CLICK PARA RETIRAR";
    public final static String NOMBRE_ITEM_COMPRAR = ChatColor.AQUA + "" + ChatColor.BOLD + "CLICK PARA COMPRAR";
    private final static List<String> bannedItems = Arrays.asList("POTION", "BANNER", "SPLASH_POTION", "LINGERING_POTION");

    public int nuevaOferta(String jugador, String objeto, int cantidad, double precio, int durabilidad) {
        executeUpdate("INSERT INTO ofertas (jugador, objeto, cantidad, precio, durabilidad) VALUES ('" + jugador + "','" + objeto + "','" + cantidad + "','" + precio + "','" + durabilidad + "')");

        return getMaxId();
    }

    private int getMaxId() {
        ResultSet rs = executeQuery("SELECT * FROM ofertas ORDER BY id DESC LIMIT 1");
        Oferta oferta = (Oferta) buildSingleObjectFromResultSet(rs);

        return oferta != null ? oferta.getId() : -1;
    }

    private void borrarOferta(int id) {
        executeUpdate("DELETE FROM ofertas WHERE id=\"" + id + "\"      ");
    }

    public void setPrecio(int id, double precio){
        executeUpdate("UPDATE ofertas SET precio = '"+precio+"' WHERE id = '"+id+"'");
    }

    public void setCantidad(int id, int cantidad) {
        executeUpdate(String.format("UPDATE ofertas SET cantidad = '%d' WHERE id = '%d'", cantidad, id));
    }

    public void setJugador(String jugador, String nuevoJugador) {
        executeUpdate("UPDATE ofertas SET jugador = '"+nuevoJugador+"' WHERE jugador = '"+jugador+"'");
    }

    public Oferta getOferta (int id) {
        ResultSet rs = executeQuery(String.format("SELECT * FROM ofertas WHERE id = '%d'", id));

        return (Oferta) buildSingleObjectFromResultSet(rs);
    }
    
    public List<Oferta> getTodasOfertas(){
        ResultSet rs = executeQuery("SELECT * FROM ofertas");

        return buildListFromResultSet(rs);
    }

    public List<Oferta> getOfertasJugador (String nombreJugador){
        ResultSet rs = executeQuery(String.format("SELECT * FROM ofertas WHERE jugador = '%s'", nombreJugador));

        return buildListFromResultSet(rs);
    }

    public static boolean estaBaneado (String item) {
        return bannedItems.stream().anyMatch( (ite) -> ite.equalsIgnoreCase(item));
    }

    public void borrarOferta(int id, String nombreJugador) {
        encantamientosMySQL.borrarEncantamientosOferta(id);
        borrarOferta(id);

        int espacios = jugadoresMySQL.getJugador(nombreJugador).getEspacios() - 1;
        jugadoresMySQL.setEspacios(nombreJugador, espacios);
    }

    public void crearOferta(ItemStack itemAVender, Player jugadorPlayer, double precio) {
        Inventory inventarioJugador = jugadorPlayer.getInventory();
        String nombreJugador = jugadorPlayer.getName();

        Jugador jugador = jugadoresMySQL.getJugador(jugadorPlayer.getName());
        int nOfertas = getTodasOfertas().size();

        /*if(nOfertas == 53){
            jugadorPlayer.sendMessage(ChatColor.GOLD + "La tienda esta llena");
            return;
        }*/

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

        int idOfetaNueva =  nuevaOferta(nombreJugador, itemAVender.getType().toString(), itemAVender.getAmount(), precio, itemAVender.getDurability());

        int id = this.getMaxId();
        Map<Enchantment, Integer> encantamientos = getEncantamientosDeItem(itemAVender);
        encantamientosMySQL.insertarEncantamientosDeItem(encantamientos, idOfetaNueva);

        int slot = jugadorPlayer.getInventory().getHeldItemSlot();
        inventarioJugador.clear(slot);

        jugadorPlayer.sendMessage(ChatColor.GOLD + "Se ha añadido a la tienda. Para retirarlos /tienda y clikc izquierdo en ellos");
        jugadorPlayer.playSound(jugadorPlayer.getLocation(), Sound.ENTITY_VILLAGER_YES, 10, 1);
        Bukkit.getServer().broadcastMessage(ChatColor.GOLD + nombreJugador + " ha añadido un objeto a la tienda por: " + ChatColor.GREEN + formatea.format(precio) + " PC " + ChatColor.AQUA + "/tienda");
    }

    public ItemStack getItemOferta(Oferta oferta) {
        ItemStack itemToConvert = new ItemStack(Material.getMaterial(oferta.getObjeto()), oferta.getCantidad());
        itemToConvert.setDurability((short) oferta.getDurabilidad());

        List<Encantamiento> enchantments = encantamientosMySQL.getEncantamientosOferta(oferta.getId());

        if(oferta.getObjeto().equalsIgnoreCase("ENCHANTED_BOOK")){
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) itemToConvert.getItemMeta();

            for(Encantamiento enchantment : enchantments){
                Enchantment enchantMentToPut = Enchantment.getByName(enchantment.getEncantamiento());
                int level = enchantment.getNivel();

                meta.addStoredEnchant(enchantMentToPut, level, true);
            }
            itemToConvert.setItemMeta(meta);

        }else{
            ItemMeta itemMeta = itemToConvert.getItemMeta();
            for(Encantamiento encantamiento : enchantments){
                Enchantment encantamientoAPoner = Enchantment.getByName(encantamiento.getEncantamiento());
                int nivel = encantamiento.getNivel();

                itemMeta.addEnchant(encantamientoAPoner, nivel, true);
            }
            itemToConvert.setItemMeta(itemMeta);
        }

        return itemToConvert;
    }

    public void retirarOferta(Player jugadorPlayer, int idARetirar) {
        ItemStack itemARetirar = this.getItemOferta(getOferta(idARetirar));

        borrarOferta(idARetirar, jugadorPlayer.getName());

        jugadorPlayer.getInventory().addItem(itemARetirar);
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

    @Override
    protected Oferta buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new Oferta(
                rs.getInt("id"),
                rs.getString("jugador"),
                rs.getString("objeto"),
                rs.getInt("cantidad"),
                rs.getDouble("precio"),
                rs.getInt("durabilidad")
        );
    }
}
