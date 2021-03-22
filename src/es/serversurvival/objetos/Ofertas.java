package es.serversurvival.objetos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

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


import es.serversurvival.config.Pixelcoin;

public class Ofertas extends MySQL {
    private static DecimalFormat formatea = new DecimalFormat("###,###.##");
    private Inventory tienda = Bukkit.createInventory(null, 54, ChatColor.DARK_RED + "" + ChatColor.BOLD + "            Tienda");

    //Mostar tienda
    public void mostarOfertas(Player p) {
        String vendedor = "";
        int id_oferta = 0;
        int precio = 0;
        ItemStack item = null;
        ItemMeta im = null;
        try {
            String consulta = "SELECT * FROM ofertas";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            while (rs.next()) {
                id_oferta = rs.getInt("id_oferta");
                vendedor = rs.getString("nombre");
                precio = rs.getInt("precio");

                item = this.getItemOferta(id_oferta);
                im = item.getItemMeta();

                ArrayList<String> lore = new ArrayList<String>();
                lore.add(ChatColor.GOLD + "Precio: " + ChatColor.GREEN + formatea.format(precio) + " PC");
                lore.add(ChatColor.GOLD + "Venderdor: " + vendedor);
                lore.add("" + id_oferta);

                im.setLore(lore);

                if (vendedor.equalsIgnoreCase(p.getName())) {
                    im.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "CLICK PARA RETIRAR");
                } else {
                    im.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "CLICK PARA COMPRAR");
                }

                item.setItemMeta(im);

                tienda.addItem(item);

            }
            p.openInventory(tienda);
        } catch (SQLException e) {
            p.sendMessage("wtf");
        }
    }

    //Borrar oferta
    public void borrarOferta(int id, String nombreJugador) {
        //Borramos encantamientos
        try {
            String consulta = "SELECT * FROM encantamientos";
            Statement st = (Statement) conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            String consulta2 = "DELETE FROM encantamientos WHERE id_oferta=\"" + id + "\"      ";
            Statement st2 = conexion.createStatement();
            int idActual = 0;
            while (rs.next()) {
                idActual = rs.getInt("id_oferta");
                if (idActual == id) {
                    st2.executeUpdate(consulta2);
                }
            }
        } catch (SQLException e) {

        }
        //Borramos oferta
        try {
            String consulta3 = "DELETE FROM ofertas WHERE id_oferta=\"" + id + "\"      ";
            Statement st3 = conexion.createStatement();
            st3.executeUpdate(consulta3);
        } catch (SQLException e) {

        }

        int espacios;
        Jugador j = new Jugador();

        //Quitamos espacio
        try {
            j.conectar("root", "", "pixelcoins");
            espacios = j.getEspacios(nombreJugador) - 1;
            j.setEspacios(nombreJugador, espacios);
            j.desconectar();
        } catch (Exception e) {

        }
    }

    //A?adir objeto a la tienda
    @SuppressWarnings("deprecation")
    public void crearOferta(ItemStack is, Player p, int precio) {
        Plugin plugin = Pixelcoin.getPlugin(Pixelcoin.class);
        Inventory in = p.getInventory();
        String nombreJugador = p.getName();
        String nombreItem = is.getType().toString();
        int cantidad;
        int durabilidad;
        int id_oferta = 0;
        int espacios = 0;
        boolean encontrado = false;
        Jugador j = new Jugador();

        //A?ADIMOS UN ESPACIO AL JUGADOR
        try {
            j.conectar("root", "", "pixelcoins");
            encontrado = j.estaRegistrado(nombreJugador);
            espacios = j.getEspacios(nombreJugador);
            //Comprobamos los espacios que tiene y si esta registrado
            if (espacios >= 5 && encontrado == true) {
                p.sendMessage(ChatColor.DARK_RED + "Solo puedes tener 5 objetos a la vez en la tienda");
                return;
            } else {
                espacios = espacios + 1;
                if (encontrado == true) {
                    j.setEspacios(nombreJugador, espacios);
                } else {
                    j.nuevoJugador(nombreJugador, 0, espacios, 0, 0, 0, 0, 0, 0);
                }
            }
            j.desconectar();
        } catch (Exception e) {
            p.sendMessage(ChatColor.DARK_RED + "Error en Ofertas.crearOferta.1");
            return;
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

        //A?adir los datos del item: nombre, durabilidad, cantidad, precio y vendedor a la tabla ofertas
        try {
            String consulta2 = "INSERT INTO ofertas (nombre, objeto, cantidad, precio, durabilidad) VALUES ('" + nombreJugador + "','" + nombreItem + "','" + cantidad + "','" + precio + "','" + durabilidad + "')";
            Statement st2 = (Statement) conexion.createStatement();
            st2.executeUpdate(consulta2);

            int slot = p.getInventory().getHeldItemSlot();
            in.clear(slot);

            p.sendMessage(ChatColor.GOLD + "Se ha a?adido a la tienda. Para retirarlos /tienda y clikc izquierdo en ellos. Ver objetos que tienes en la tienda /listaOfertas");
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 10, 1);

            plugin.getServer().broadcastMessage(ChatColor.GOLD + nombreJugador + " ha a?adido un objeto a la tienda por: " + ChatColor.GREEN + formatea.format(precio) + " PC");
        } catch (SQLException e) {
            p.sendMessage(ChatColor.DARK_RED + "Error en Ofertas.crearOferta.2");
        }

        //Al a?adir un objeto en la tabla ofertas este se el que tendra mayor id; sacar la id de la oferta de la tabla ofertas
        try {
            String consulta3 = "SELECT * FROM ofertas ORDER BY id_oferta DESC LIMIT 1 ";
            Statement st3 = (Statement) conexion.createStatement();
            ResultSet rs3;
            rs3 = st3.executeQuery(consulta3);

            while (rs3.next()) {
                id_oferta = rs3.getInt("id_oferta");
            }
        } catch (SQLException e) {
            p.sendMessage(ChatColor.DARK_RED + "Error en Error en Ofertas.crearOferta.3");
        }

        //recorrer el hashmap y a?adirlos a la tabla encantamientos
        try {
            Enchantment e;
            int nivel;
            String consulta4 = "";
            //recorrer hashmap encantamientos
            for (Map.Entry<Enchantment, Integer> entry : encItem.entrySet()) {
                e = entry.getKey();
                nivel = entry.getValue();
                consulta4 = "INSERT INTO encantamientos (id_oferta, encantamiento, nivel) VALUES ('" + id_oferta + "','" + e.getName() + "','" + nivel + "')";

                Statement st4 = (Statement) conexion.createStatement();
                st4.executeUpdate(consulta4);
            }


        } catch (SQLException e) {
            p.sendMessage(ChatColor.DARK_RED + "Error en TError en Ofertas.crearOferta.4");
        }

    }

    //Devolver item encantado
    @SuppressWarnings({"deprecation"})
    public ItemStack getItemOferta(int id) {
        ItemStack i = null;
        String objeto = "";
        int durabilidad = 0;
        int cantidad = 0;

        //Datos de item


        objeto = this.getObjeto(id);
        durabilidad = this.getDurabilidad(id);
        cantidad = this.getCantidad(id);

        i = new ItemStack(Material.getMaterial(objeto), cantidad);
        i.setDurability((short) durabilidad);

        //Encantamientos
        Enchantment en;
        int nivel;
        ItemMeta ime = i.getItemMeta();
        try {
            String consulta = "SELECT * FROM encantamientos";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            while (rs.next()) {
                if (rs.getInt("id_oferta") == id) {
                    en = Enchantment.getByName(rs.getString("encantamiento"));
                    nivel = rs.getInt("nivel");
                    ime.addEnchant(en, nivel, true);

                    i.setItemMeta(ime);
                }
            }

        } catch (SQLException ex) {
        }
        return i;
    }

    //RetirarOferta
    public void retirarOferta(Player p, int id) {
        ItemStack i = this.getItemOferta(id);
        this.borrarOferta(id, p.getName());
        p.getInventory().addItem(i);
        this.mostarOfertas(p);

        p.sendMessage(ChatColor.GOLD + "Objeto retirado!");
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
    }

    //Conseguir la cantidad del objeto en la tienda
    public int getCantidad(int id) {
        int cantidad = 0;
        try {
            String consulta = "SELECT * FROM ofertas";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            while (rs.next()) {
                if (rs.getInt("id_oferta") == id) {
                    cantidad = rs.getInt("cantidad");
                    break;
                }
            }
        } catch (SQLException e) {

        }
        return cantidad;
    }

    public int getDurabilidad(int id) {
        int durabilidad = 0;
        try {
            String consulta = "SELECT * FROM ofertas";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            while (rs.next()) {
                if (rs.getInt("id_oferta") == id) {
                    durabilidad = rs.getInt("durabilidad");
                    break;
                }
            }
        } catch (SQLException e) {

        }
        return durabilidad;
    }

    //Conseguir el nombre del vendedor
    public String getNombre(int id) {
        String nombre = "";
        try {
            String consulta = "SELECT * FROM ofertas";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            while (rs.next()) {
                if (rs.getInt("id_oferta") == id) {
                    nombre = rs.getString("nombre");
                    break;
                }
            }
        } catch (SQLException e) {

        }
        return nombre;
    }

    //Set cantidad
    public void setCantidad(int id, int cantidad) {
        try {
            String consulta2 = "UPDATE ofertas SET cantidad = ? WHERE id_oferta = ?";
            PreparedStatement pst2 = (PreparedStatement) conexion.prepareStatement(consulta2);

            pst2.setInt(1, cantidad);
            pst2.setInt(2, id);
        } catch (SQLException e) {

        }
    }

    //GetObjeto
    public String getObjeto(int id) {
        String objeto = "";
        try {
            String consulta = "SELECT * FROM ofertas";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            while (rs.next()) {
                if (rs.getInt("id_oferta") == id) {
                    objeto = rs.getString("objeto");
                    break;
                }
            }
        } catch (SQLException e) {

        }
        return objeto;
    }

    //GetPrecio
    public int getPrecio(int id) {
        int precio = 0;
        try {
            String consulta = "SELECT * FROM ofertas";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            while (rs.next()) {
                if (rs.getInt("id_oferta") == id) {
                    precio = rs.getInt("precio");
                    break;
                }
            }
        } catch (SQLException e) {

        }
        return precio;
    }
}