package es.serversurvival.tienda.mySQL.ofertas;

import java.sql.*;
import java.util.*;

import es.serversurvival.shared.mysql.MySQL;
import es.serversurvival.tienda.mySQL.encantamientos.Encantamiento;
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

import static es.serversurvival.utils.Funciones.*;

/**
 * 363 -> 193
 */
public final class Ofertas extends MySQL {
    public final static Ofertas INSTANCE = new Ofertas();
    private Ofertas () {}

    public final static int MAX_ESPACIOS = 90;
    public final static String NOMBRE_ITEM_RETIRAR = ChatColor.RED + "" + ChatColor.BOLD + "CLICK PARA RETIRAR";
    public final static String NOMBRE_ITEM_COMPRAR = ChatColor.AQUA + "" + ChatColor.BOLD + "CLICK PARA COMPRAR";
    private final static List<String> bannedItems = Arrays.asList("POTION", "BANNER", "SPLASH_POTION", "LINGERING_POTION");

    public int nuevaOferta(String jugador, String objeto, int cantidad, double precio, int durabilidad) {
        executeUpdate("INSERT INTO ofertas (jugador, objeto, cantidad, precio, durabilidad) VALUES ('" + jugador + "','" + objeto + "','" + cantidad + "','" + precio + "','" + durabilidad + "')");

        return getMaxId();
    }

    private int getMaxId() {
        Oferta oferta = (Oferta) buildObjectFromQuery("SELECT * FROM ofertas ORDER BY id DESC LIMIT 1");

        return oferta != null ? oferta.getId() : -1;
    }

    public int getEspacios (String jugador) {
        List<Oferta> ofertas = buildListFromQuery("SELECT * FROM ofertas WHERE jugador = '"+jugador+"'");

        return ofertas != null || ofertas.size() != 0 ? 0 : ofertas.size();
    }

    public void borrarOferta(int id) {
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
        return (Oferta) buildObjectFromQuery(String.format("SELECT * FROM ofertas WHERE id = '%d'", id));
    }
    
    public List<Oferta> getTodasOfertas(){
        return buildListFromQuery("SELECT * FROM ofertas");
    }

    public List<Oferta> getOfertasJugador (String nombreJugador){
        return buildListFromQuery(String.format("SELECT * FROM ofertas WHERE jugador = '%s'", nombreJugador));
    }

    public static boolean estaBaneado (String item) {
        return bannedItems.stream().anyMatch( (ite) -> ite.equalsIgnoreCase(item));
    }

    public void crearOferta(ItemStack itemAVender, Player player, double precio) {
        Inventory inventarioJugador = player.getInventory();
        String nombreJugador = player.getName();
        int idOfetaNueva = nuevaOferta(nombreJugador, itemAVender.getType().toString(), itemAVender.getAmount(), precio, itemAVender.getDurability());

        Map<Enchantment, Integer> encantamientos = getEncantamientosDeItem(itemAVender);
        encantamientosMySQL.insertarEncantamientosDeItem(encantamientos, idOfetaNueva);
        inventarioJugador.clear(player.getInventory().getHeldItemSlot());

        enviarMensajeYSonido(player, ChatColor.GOLD + "Se ha añadido a la tienda. Para retirarlos /tienda y clikc izquierdo en ellos", Sound.ENTITY_VILLAGER_YES);

        Bukkit.getServer().broadcastMessage(ChatColor.GOLD + nombreJugador + " ha añadido un objeto a la tienda por: " + ChatColor.GREEN + formatea.format(precio) + " PC " + ChatColor.AQUA + "/tienda");
    }

    public ItemStack getItemOferta(Oferta oferta) {
        ItemStack itemToConvert = new ItemStack(Material.getMaterial(oferta.getObjeto()), oferta.getCantidad());
        itemToConvert.setDurability((short) oferta.getDurabilidad());

        List<Encantamiento> encantamientos = encantamientosMySQL.getEncantamientosOferta(oferta.getId());

        if(oferta.getObjeto().equalsIgnoreCase("ENCHANTED_BOOK")){
            rellenarEncantamientoLibro(itemToConvert, encantamientos);
        }else{
            rellenarEncantamiento(itemToConvert, encantamientos);
        }

        return itemToConvert;
    }

    private ItemStack rellenarEncantamientoLibro (ItemStack itemToConvert, List<Encantamiento> encantamientos) {
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) itemToConvert.getItemMeta();

        for(Encantamiento enchantment : encantamientos){
            Enchantment enchantMentToPut = Enchantment.getByName(enchantment.getEncantamiento());
            int level = enchantment.getNivel();

            meta.addStoredEnchant(enchantMentToPut, level, true);
        }
        itemToConvert.setItemMeta(meta);

        return itemToConvert;
    }

    private ItemStack rellenarEncantamiento (ItemStack itemToConvert, List<Encantamiento> encantamientos) {
        ItemMeta itemMeta = itemToConvert.getItemMeta();
        for(Encantamiento encantamiento : encantamientos){
            Enchantment encantamientoAPoner = Enchantment.getByName(encantamiento.getEncantamiento());
            int nivel = encantamiento.getNivel();

            itemMeta.addEnchant(encantamientoAPoner, nivel, true);
        }
        itemToConvert.setItemMeta(itemMeta);

        return itemToConvert;
    }

    public ItemStack retirarOferta(Player player, int idARetirar) {
        borrarOferta(idARetirar);

        ItemStack itemARetirar = this.getItemOferta(getOferta(idARetirar));
        player.getInventory().addItem(itemARetirar);

        enviarMensajeYSonido(player, ChatColor.GOLD + "Objeto retirado!", Sound.ENTITY_PLAYER_LEVELUP);
        return itemARetirar;
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
