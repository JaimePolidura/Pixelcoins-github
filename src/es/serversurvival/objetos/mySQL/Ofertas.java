package es.serversurvival.objetos.mySQL;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.*;

import es.serversurvival.objetos.menus.Menu;
import es.serversurvival.objetos.menus.OfertasMenu;
import es.serversurvival.objetos.mySQL.tablasObjetos.Encantamiento;
import es.serversurvival.objetos.mySQL.tablasObjetos.Oferta;
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

public class Ofertas extends MySQL {
    public final static int maxEspacios = 7;
    private static DecimalFormat formatea = new DecimalFormat("###,###.##");

    private void nuevaOferta(String nombre, String objeto, int cantidad, double precio, int durabilidad) {
        try {
            String consulta2 = "INSERT INTO ofertas (nombre, objeto, cantidad, precio, durabilidad) VALUES ('" + nombre + "','" + objeto + "','" + cantidad + "','" + precio + "','" + durabilidad + "')";
            Statement st2 = conexion.createStatement();
            st2.executeUpdate(consulta2);
        } catch (Exception e) {

        }
    }

    private void borrarOferta(int id_oferta) {
        try {
            String consulta2 = "DELETE FROM ofertas WHERE id_oferta=\"" + id_oferta + "\"      ";
            Statement st2 = conexion.createStatement();
            st2.executeUpdate(consulta2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getMaxId() {
        int maxId = 0;
        try {
            String consulta = "SELECT id_oferta FROM ofertas ORDER BY id_oferta DESC LIMIT 1 ";
            Statement st = (Statement) conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            while (rs.next()) {
                maxId = rs.getInt("id_oferta");
                break;
            }
            rs.close();
        } catch (SQLException e) {

        }
        return maxId;
    }

    public List<Oferta> getTodasOfertas(){
        List<Oferta> ofertas = new ArrayList<>();
        try{
            String consulta = "SELECT * FROM ofertas";
            ResultSet rs = conexion.createStatement().executeQuery(consulta);

            while (rs.next()){
                ofertas.add(new Oferta(
                        rs.getInt("id_oferta"),
                        rs.getString("nombre"),
                        rs.getString("objeto"),
                        rs.getInt("cantidad"),
                        rs.getDouble("precio"),
                        rs.getInt("durabilidad")
                ));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return ofertas;
    }

    public void borrarOferta(int id, String nombreJugador) {
        Encantamientos encan = new Encantamientos();
        encan.borrarEncantamientosOferta(id);
        this.borrarOferta(id);

        int espacios;
        Jugadores j = new Jugadores();
        espacios = j.getEspacios(nombreJugador) - 1;
        j.setEspacios(nombreJugador, espacios);
    }

    @SuppressWarnings("deprecation")
    public void crearOferta(ItemStack is, Player p, double precio) {
        Plugin plugin = Pixelcoin.getPlugin(Pixelcoin.class);
        Inventory in = p.getInventory();
        String nombreJugador = p.getName();
        String nombreItem = is.getType().toString();
        int cantidad;
        int durabilidad;
        int id_oferta = 0;
        int espacios = 0;
        boolean encontrado = false;
        Jugadores j = new Jugadores();

        encontrado = j.estaRegistrado(nombreJugador);
        espacios = j.getEspacios(nombreJugador);

        if (espacios >= maxEspacios && encontrado) {
            p.sendMessage(ChatColor.DARK_RED + "Solo puedes tener 5 objetos a la vez en la tienda");
            return;
        } else {
            espacios = espacios + 1;
            if (encontrado) {
                j.setEspacios(nombreJugador, espacios);
            } else {
                j.nuevoJugador(nombreJugador, 0, espacios, 0, 0, 0, 0, 0, 0);
            }
        }

        cantidad = is.getAmount();
        durabilidad = is.getDurability();
        Map<Enchantment, Integer> encItem;

        if (is.getType() == Material.ENCHANTED_BOOK) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) is.getItemMeta();
            encItem = meta.getStoredEnchants();
        } else {
            encItem = is.getEnchantments();
        }

        this.nuevaOferta(nombreJugador, nombreItem, cantidad, precio, durabilidad);

        int slot = p.getInventory().getHeldItemSlot();
        in.clear(slot);

        p.sendMessage(ChatColor.GOLD + "Se ha a?adido a la tienda. Para retirarlos /tienda y clikc izquierdo en ellos. Ver objetos que tienes en la tienda /listaOfertas");
        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 10, 1);
        plugin.getServer().broadcastMessage(ChatColor.GOLD + nombreJugador + " ha a?adido un objeto a la tienda por: " + ChatColor.GREEN + formatea.format(precio) + " PC");

        id_oferta = this.getMaxId();

        Enchantment e;
        String eNombre;
        int nivel;

        Encantamientos encan = new Encantamientos();
        for (Map.Entry<Enchantment, Integer> entry : encItem.entrySet()) {
            eNombre = entry.getKey().getName();
            nivel = entry.getValue();

            encan.nuevoEncantamiento(eNombre, nivel, id_oferta);
        }

        Set<Menu> ofertaSet = new HashSet<>();
        ofertaSet.addAll(Menu.activeMenus);

        ofertaSet.forEach(menu -> {
            if(menu instanceof OfertasMenu){
                ((OfertasMenu) menu).refresh();
            }
        });
    }

    @SuppressWarnings({"deprecation"})
    public ItemStack getItemOferta(int id) {
        ItemStack i = null;
        String objeto = "";
        int durabilidad = 0;
        int cantidad = 0;

        objeto = this.getObjeto(id);
        durabilidad = this.getDurabilidad(id);
        cantidad = this.getCantidad(id);

        i = new ItemStack(Material.getMaterial(objeto), cantidad);
        i.setDurability((short) durabilidad);

        ItemMeta ime = i.getItemMeta();

        Encantamientos encan = new Encantamientos();
        List<Encantamiento> encantamientos = encan.getEncantamientosOferta(id);

        for(Encantamiento encantamiento : encantamientos){
            ime.addEnchant(
                    Enchantment.getByName(encantamiento.getEncantamiento()),
                    encantamiento.getNivel(),
                    true
            );
        }
        i.setItemMeta(ime);

        return i;
    }

    public void retirarOferta(Player p, int id) {
        ItemStack i = this.getItemOferta(id);
        this.borrarOferta(id, p.getName());
        p.getInventory().addItem(i);

        OfertasMenu ofertasMenu = (OfertasMenu) Menu.getByPlayer(p);
        ofertasMenu.refresh();

        p.sendMessage(ChatColor.GOLD + "Objeto retirado!");
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
    }

    public int getCantidad(int id) {
        int cantidad = 0;

        try {
            String consutla = "SELECT cantidad FROM ofertas WHERE id_oferta = ? ";
            PreparedStatement pst = conexion.prepareStatement(consutla);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                cantidad = rs.getInt("cantidad");
                break;
            }
            rs.close();
        } catch (SQLException e) {

        }
        return cantidad;
    }

    public int getDurabilidad(int id) {
        int durabilidad = 0;

        try {
            String consutla = "SELECT durabilidad FROM ofertas WHERE id_oferta = ? ";
            PreparedStatement pst = conexion.prepareStatement(consutla);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                durabilidad = rs.getInt("durabilidad");
                break;
            }
            rs.close();
        } catch (SQLException e) {

        }
        return durabilidad;
    }

    public String getNombre(int id) {
        String name = "";

        try {
            String consutla = "SELECT nombre FROM ofertas WHERE id_oferta = ? ";
            PreparedStatement pst = conexion.prepareStatement(consutla);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                name = rs.getString("nombre");
                break;
            }
            rs.close();
        } catch (SQLException e) {

        }
        return name;
    }

    public void setCantidad(int id, int cantidad) {
        try {
            String consulta2 = "UPDATE ofertas SET cantidad = ? WHERE id_oferta = ?";
            PreparedStatement pst2 = (PreparedStatement) conexion.prepareStatement(consulta2);

            pst2.setInt(1, cantidad);
            pst2.setInt(2, id);
            pst2.executeUpdate();
        } catch (SQLException e) {

        }
    }

    public String getObjeto(int id) {
        String objeto = "";

        try {
            String consutla = "SELECT objeto FROM ofertas WHERE id_oferta = ? ";
            PreparedStatement pst = conexion.prepareStatement(consutla);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                objeto = rs.getString("objeto");
                break;
            }
            rs.close();
        } catch (SQLException e) {

        }
        return objeto;
    }

    public double getPrecio(int id) {
        double precio = 0;
        try {
            String consutla = "SELECT precio FROM ofertas WHERE id_oferta = ? ";
            PreparedStatement pst = conexion.prepareStatement(consutla);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                precio = rs.getDouble("precio");
                break;
            }
            rs.close();
        } catch (SQLException e) {

        }
        return precio;
    }
}