package es.serversurvival.tienda._shared.mySQL.ofertas;

import java.sql.*;
import java.util.*;

import es.jaimetruman.delete.Delete;
import es.jaimetruman.insert.Insert;
import es.jaimetruman.select.Order;
import es.jaimetruman.select.Select;
import es.jaimetruman.select.SelectOptionInitial;
import es.jaimetruman.update.Update;
import es.jaimetruman.update.UpdateOptionInitial;
import es.serversurvival._shared.mysql.MySQLRepository;
import es.serversurvival.tienda._shared.mySQL.encantamientos.Encantamiento;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * 363 -> 193
 */
public final class Ofertas extends MySQLRepository {
    public final static Ofertas INSTANCE = new Ofertas();

    private final UpdateOptionInitial update;
    private final SelectOptionInitial select;

    private Ofertas () {
        this.update = Update.table("ofertas");
        this.select = Select.from("ofertas");
    }

    public final static int MAX_ESPACIOS = 90;
    public final static String NOMBRE_ITEM_RETIRAR = ChatColor.RED + "" + ChatColor.BOLD + "CLICK PARA RETIRAR";
    public final static String NOMBRE_ITEM_COMPRAR = ChatColor.AQUA + "" + ChatColor.BOLD + "CLICK PARA COMPRAR";
    private final static List<String> bannedItems = Arrays.asList("POTION", "BANNER", "SPLASH_POTION", "LINGERING_POTION");

    public int nuevaOferta(String jugador, String objeto, int cantidad, double precio, int durabilidad) {
        String query = Insert.table("ofertas")
                .fields("jugador", "objeto", "cantidad", "precio", "durabilidad")
                .values(jugador, objeto, cantidad, precio, durabilidad);

        executeUpdate(query);

        return getMaxId();
    }

    private int getMaxId() {
        Oferta oferta = (Oferta) buildObjectFromQuery(select.orderBy("id", Order.DESC).limit(1));

        return oferta != null ? oferta.getId() : -1;
    }

    public int getEspacios (String jugador) {
        List<Oferta> ofertas = buildListFromQuery(select.where("jugador").equal(jugador));

        return ofertas != null || ofertas.size() != 0 ? 0 : ofertas.size();
    }

    public void borrarOferta(int id) {
        executeUpdate(Delete.from("ofertas").where("id").equal(id));
    }

    public void setPrecio(int id, double precio){
        executeUpdate(update.set("precio", precio).where("id").equal(id));
    }

    public void setCantidad(int id, int cantidad) {
        executeUpdate(update.set("cantidad", cantidad).where("id").equal(id));
    }

    public void setJugador(String jugador, String nuevoJugador) {
        executeUpdate(update.set("jugador", nuevoJugador).where("jugador").equal(jugador));
    }

    public Oferta getOferta (int id) {
        return (Oferta) buildObjectFromQuery(select.where("id").equal(id));
    }

    public List<Oferta> getTodasOfertas(){
        return buildListFromQuery(select);
    }

    public List<Oferta> getOfertasJugador (String nombreJugador){
        return buildListFromQuery(select.where("jugador").equal(nombreJugador));
    }

    public static boolean estaBaneado (String item) {
        return bannedItems.stream().anyMatch( (ite) -> ite.equalsIgnoreCase(item));
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

    private void rellenarEncantamientoLibro (ItemStack itemToConvert, List<Encantamiento> encantamientos) {
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) itemToConvert.getItemMeta();

        for(Encantamiento enchantment : encantamientos){
            Enchantment enchantMentToPut = Enchantment.getByName(enchantment.getEncantamiento());
            int level = enchantment.getNivel();

            meta.addStoredEnchant(enchantMentToPut, level, true);
        }
        itemToConvert.setItemMeta(meta);

    }

    private void rellenarEncantamiento (ItemStack itemToConvert, List<Encantamiento> encantamientos) {
        ItemMeta itemMeta = itemToConvert.getItemMeta();
        for(Encantamiento encantamiento : encantamientos){
            Enchantment encantamientoAPoner = Enchantment.getByName(encantamiento.getEncantamiento());
            int nivel = encantamiento.getNivel();

            itemMeta.addEnchant(encantamientoAPoner, nivel, true);
        }
        itemToConvert.setItemMeta(itemMeta);

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
