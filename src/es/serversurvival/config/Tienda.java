package es.serversurvival.config;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import com.mysql.jdbc.Connection;

public class Tienda {
    Funciones f = new Funciones();
    public static Connection conexion;
    public DecimalFormat formatea = new DecimalFormat("###,###.##");
    private Plugin plugin = Pixelcoin.getPlugin(Pixelcoin.class);

    //Metodo para conectarme a la base de datos
    public void conectar(String user, String pass, String dbName) throws Exception {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conexion = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbName, user, pass);
        } catch (ClassNotFoundException e) {
            Logger.getLogger(Metodos.class.getName()).log(Level.SEVERE, null, e);
        } catch (SQLException e) {
            Logger.getLogger(Metodos.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    //Metodo para desconectar
    public void desconectar() {
        try {
            conexion.close();
        } catch (SQLException e) {
            Logger.getLogger(Metodos.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    //-------Mostrar tienda (inventario)---------
    @SuppressWarnings("deprecation")
    public void mostrarTienda(Player p) {
        Inventory tienda = Bukkit.createInventory(null, 54, ChatColor.DARK_RED + "" + ChatColor.BOLD + "            Tienda");

        String nombreItemActual = "";
        String nombreActual = "";
        int encEncontrado = 0;
        int cantidadActual = 0;
        int precioActual = 0;
        int duraItemActual = 0;
        int id_oferta = 0;
        int slotsTienda = 0;

        ItemStack itemA�adido = null;
        ItemMeta im = null;

        //Recorrer tabla de ofertas para sacar la durabilidad, precio, cantidad, objeto y el nombre del que vendio el objeto
        try {
            String consulta = "SELECT * FROM ofertas";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);
            while (rs.next()) {

                //comprobar que haya espacio en la tienda
                if (slotsTienda > 54) {
                    p.sendMessage(ChatColor.DARK_RED + "No hay espacio en la tienda, hablar con el admin");
                    break;
                } else {

                    slotsTienda++;
                    encEncontrado = 0;
                    HashMap<String, Integer> Enc = new HashMap<String, Integer>();

                    //sacar todas los datos de los campos de la tabla ofertas
                    nombreItemActual = rs.getString("objeto");
                    nombreActual = rs.getString("nombre");
                    precioActual = rs.getInt("precio");
                    cantidadActual = rs.getInt("cantidad");
                    duraItemActual = rs.getInt("durabilidad");
                    id_oferta = rs.getInt("id_oferta");

                    //crear el item que se va a�adir de tipo el nombre que se haya guardado en la tabla ofertas y cantidad la que se halla guardado en la tabla ofertas
                    //Y conseguir sus datos
                    itemA�adido = new ItemStack(Material.getMaterial(nombreItemActual), cantidadActual);
                    im = itemA�adido.getItemMeta();

                    //A�adir descripcion al objeto con el precio, el vendedor que se han almacenado en la tabla ofertas
                    ArrayList<String> lore = new ArrayList<String>();
                    lore.add(ChatColor.GOLD + "Precio: " + ChatColor.GREEN + formatea.format(precioActual) + " PC");
                    lore.add(ChatColor.GOLD + "Venderdor: " + nombreActual);
                    lore.add("" + id_oferta);

                    im.setLore(lore);

                    //A�adir nombre al objeto y durabilidad mediante lo que se guardado en la base de datos

                    if (nombreActual.equalsIgnoreCase(p.getName())) {
                        im.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "CLICK PARA RETIRAR");
                    } else {
                        im.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "CLICK PARA COMPRAR");
                    }


                    itemA�adido.setItemMeta(im);

                    itemA�adido.setDurability((short) duraItemActual);

                    //recorrer tabla encantamientos para sacar los encantamientos
                    String consulta2 = "SELECT * FROM encantamientos ";
                    Statement st2 = conexion.createStatement();
                    ResultSet rs2;
                    rs2 = st2.executeQuery(consulta2);

                    //recorrer la tabla encantamientos con la id_oferta. Si encuentra algo se a�ade al hashmap los datos de los campos nivel y encanamiento.
                    while (rs2.next()) {
                        if (id_oferta == rs2.getInt("id_oferta")) {
                            Enc.put(rs2.getString("encantamiento"), rs2.getInt("nivel"));
                            encEncontrado++;
                        } else {

                        }
                    }

                    //Si no se ha encontrado encantamientos
                    if (encEncontrado == 0) {
                        tienda.addItem(itemA�adido);
                        //Se ha encontrado encantamientos
                    } else {
                        //Se recorre el hashmap y se va a�adiendo el encantamiento el itemA�adido.
                        for (Entry<String, Integer> entry : Enc.entrySet()) {
                            int nivelEncontrado = entry.getValue();
                            String encantamientoEncontrado = entry.getKey();
                            Enchantment e = Enchantment.getByName(encantamientoEncontrado);

                            ItemMeta EncMeta = itemA�adido.getItemMeta();
                            EncMeta.addEnchant(e, nivelEncontrado, true);

                            itemA�adido.setItemMeta(EncMeta);
                        }
                        //A�adir item
                        tienda.addItem(itemA�adido);
                    }
                }
            }
            //Mostrar tienda
            p.openInventory(tienda);
        } catch (SQLException e) {
            p.sendMessage(ChatColor.DARK_RED + "Error en Tienda.mostrarTienda.1, hablar con el admin");
        }
    }

    //----------Vender objeto----------
    @SuppressWarnings({"deprecation"})
    public void anadirObjeto(Player p, String sprecio) {
        String nombreJugador = p.getName();
        Inventory in = p.getInventory();
        int precio = 0;
        int cantidad = 0;
        int id_oferta = 0;
        int duraItem;
        String nombreItem = "";
        boolean done = false;
        boolean done2 = false;
        boolean encontrado = false;
        int actualDinero = 0;
        ItemStack itemMano = p.getItemInHand();
        nombreItem = itemMano.getType().toString();

        //Intentar ver si el argumento que se ha puesto es texto o no
        try {
            precio = Integer.parseInt(sprecio);
            done = true;
        } catch (NumberFormatException e) {
            p.sendMessage(ChatColor.DARK_RED + "Introduce un numero, no texto de tal manera: /vender <precio a vender/item>");
        }

        //Comprobar si el precio es negativo
        if (precio < 0) {
            p.sendMessage(ChatColor.DARK_RED + "A ser posible necesitas vender a un precio superior a 0 ");
            done = false;
            //comprobar no tiene objetos en la mano y si ha introducido texto
        } else if (p.getItemInHand().getAmount() < 1 && done == true) {
            p.sendMessage(ChatColor.DARK_RED + "Necesitas tener un objeto en la mano para poder venderlo");
            done = false;
        }

        //Si ha metido bien los argumantes y el objero no es  potion o banner. El if tienes 2 partes
        if (done == true && !nombreItem.equals("POTION") && !nombreItem.equals("BANNER") && !(nombreItem.equals("ENCAHNTED_BOOK"))) {

            //Sumar +1 a la tabla ocupados para que el jugador no tenga mas de 4 items en la tienda.
            try {
                int nventas = 0;
                int ingresos = 0;
                int gastos = 0;
                int beneficios = 0;
                int ocupados = 0;

                String consulta = "SELECT * FROM jugadores";
                Statement st = conexion.createStatement();
                ResultSet rs;
                rs = st.executeQuery(consulta);

                //Sacar los espacios actuales de la tabal espacios
                while (rs.next()) {
                    String nombreActual = rs.getString("nombre");
                    if (nombreActual.equalsIgnoreCase(nombreJugador)) {
                        ocupados = rs.getInt("espacios");
                        actualDinero = rs.getInt("pixelcoin");
                        nventas = rs.getInt("nventas");
                        ingresos = rs.getInt("ingresos");
                        gastos = rs.getInt("gastos");
                        beneficios = rs.getInt("beneficios");

                        encontrado = true;
                        break;
                    }
                }

                //Si son menos de 4 se suma +1 y se pondra done2 = true para que se a�ada el item
                if (ocupados < 4) {
                    done2 = true;
                    //Comprobamos si ha encontrado al jugador
                    if (encontrado == true) {
                        String consulta5 = "UPDATE jugadores SET pixelcoin = ?, espacios = ?, nventas = ?, ingresos = ?, gastos = ?, beneficios = ? WHERE nombre = ?";
                        PreparedStatement pst5 = (PreparedStatement) conexion.prepareStatement(consulta5);
                        pst5.setInt(1, actualDinero);
                        pst5.setInt(2, ocupados + 1);
                        pst5.setInt(3, nventas);
                        pst5.setInt(4, ingresos);
                        pst5.setInt(5, gastos);
                        pst5.setInt(6, beneficios);
                        pst5.setString(7, nombreJugador);
                        pst5.executeUpdate();
                    } else {
                        ocupados = ocupados + 1;
                        String consulta6 = "INSERT INTO jugadores (nombre, pixelcoin, espacios, nventas, ingresos, gastos, beneficios) VALUES ('" + nombreJugador + "','" + actualDinero + "','" + ocupados + "','" + nventas + "','" + ingresos + "','" + gastos + "','" + beneficios + "')";
                        Statement st6 = (Statement) conexion.createStatement();
                        st6.executeUpdate(consulta6);

                    }


                    //Si son 4 no no te deja tener el objeto.
                } else {
                    p.sendMessage(ChatColor.DARK_RED + "Solo puedes tener 4 objetos en la tienda a la vez!");
                }
            } catch (SQLException e) {
                p.sendMessage(ChatColor.DARK_RED + "Error en Tienda.A�adirObjeto.0, hablar con el admin");
            }

            //Si se puede tener espacios en la tienda
            if (done2 == true) {
                cantidad = itemMano.getAmount();
                Map<Enchantment, Integer> encItem = itemMano.getEnchantments();
                duraItem = itemMano.getDurability();

                //A�adir los datos del item: nombre, durabilidad, cantidad, precio y vendedor a la tabla ofertas
                try {
                    String consulta2 = "INSERT INTO ofertas (nombre, objeto, cantidad, precio, durabilidad) VALUES ('" + nombreJugador + "','" + nombreItem + "','" + cantidad + "','" + precio + "','" + duraItem + "')";
                    Statement st2 = (Statement) conexion.createStatement();
                    st2.executeUpdate(consulta2);

                    int slot = p.getInventory().getHeldItemSlot();
                    in.clear(slot);

                    p.sendMessage(ChatColor.GOLD + "Se ha a�adido a la tienda. Para retirarlos /tienda y clikc izquierdo en ellos. Ver objetos que tienes en la tienda /listaOfertas");
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 10, 1);

                    plugin.getServer().broadcastMessage(ChatColor.GOLD + nombreJugador + " ha a�adido un objeto a la tienda por: " + ChatColor.GREEN + precio + " PC");
                } catch (SQLException e) {
                    p.sendMessage(ChatColor.DARK_RED + "Error en Tienda.A�adirObjeto.1, hablar con el admin");
                }

                //Al a�adir un objeto en la tabla ofertas este se el que tendra mayor id; sacar la id de la oferta de la tabla ofertas
                try {
                    String consulta3 = "SELECT * FROM ofertas ORDER BY id_oferta DESC LIMIT 1 ";
                    Statement st3 = (Statement) conexion.createStatement();
                    ResultSet rs3;
                    rs3 = st3.executeQuery(consulta3);

                    while (rs3.next()) {
                        id_oferta = rs3.getInt("id_oferta");
                    }
                } catch (SQLException e) {
                    p.sendMessage(ChatColor.DARK_RED + "Error en Tienda.A�adirObjeto.2, hablar con el admin");
                }

                //recorrer el hashmap y a�adirlos a la tabla encantamientos
                try {
                    Enchantment e;
                    int nivel;
                    //recorrer hashmap encantamientos
                    for (Map.Entry<Enchantment, Integer> entry : encItem.entrySet()) {
                        e = entry.getKey();
                        nivel = entry.getValue();
                        String consulta4 = "INSERT INTO encantamientos (id_oferta, encantamiento, nivel) VALUES ('" + id_oferta + "','" + e.getName() + "','" + nivel + "')";
                        Statement st4 = (Statement) conexion.createStatement();
                        st4.executeUpdate(consulta4);
                    }


                } catch (SQLException e) {
                    p.sendMessage(ChatColor.DARK_RED + "Error en Tienda.A�adirObjeto.3, hablar con el admin");
                }
            }

        } else if (done == false) {

        } else {
            p.sendMessage(ChatColor.DARK_RED + "No se puede comercializar con este objeto");
        }
    }

    //------------Ver los items que tienes en la tienda para vender
    public void verOfertas(Player p) {
        String nombreJugador = p.getName();
        int precio = 0;
        String objeto = "";
        int id = 0;
        int cantidad = 0;

        try {
            String consulta = "SELECT * FROM ofertas";
            Statement st = (Statement) conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);
            //Recorrer la tabla ofertas y ir enviando un mensaje
            while (rs.next()) {
                String nombreActual = rs.getString("nombre");
                if (nombreActual.equalsIgnoreCase(nombreJugador)) {
                    id = rs.getInt("id_oferta");
                    cantidad = rs.getInt("cantidad");
                    precio = rs.getInt("precio");
                    objeto = rs.getString("objeto");

                    p.sendMessage(ChatColor.GOLD + "Para id = " + id + ": " + ChatColor.AQUA + objeto + ChatColor.GOLD + " a " + ChatColor.GREEN + formatea.format(precio) + " PC " + ChatColor.GOLD + " cantidad: " + ChatColor.AQUA
                            + cantidad);
                }
            }
        } catch (SQLException e) {
            p.sendMessage(ChatColor.DARK_RED + "Error en Tienda.verOfertas, hablar con el admin");
        }
    }

    //-----------Reretirar items en la tienda--------------
    @SuppressWarnings("deprecation")
    public void retirarObjeto(Player p, ItemStack i, Inventory in, int slot) {
        int id_oferta = Integer.parseInt(i.getItemMeta().getLore().get(2));
        in.clear(slot);


        String nombreObjeto = "";
        int durabilidad = 0;
        int cantidad = 0;
        boolean encEncontrado = false;
        HashMap<String, Integer> enchants = new HashMap<String, Integer>();
        ItemStack itemA�adido = null;
        //Realizamos las consultas

        //Recorremos la tabla encantamientos 
        try {
            String consulta = "SELECT * FROM encantamientos";
            Statement st = (Statement) conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            //Comprobamos que tenga encantamientos
            //Tiene encantamientos
            int idActual = 0;
            while (rs.next()) {
                idActual = rs.getInt("id_oferta");
                if (idActual == id_oferta) {
                    encEncontrado = true;
                    enchants.put(rs.getString("encantamiento"), rs.getInt("nivel"));
                    String consulta2 = "DELETE FROM encantamientos WHERE id_oferta=\"" + id_oferta + "\"      ";
                    Statement st2 = conexion.createStatement();
                    st2.executeUpdate(consulta2);
                }
            }
        } catch (SQLException e1) {
            p.sendMessage(ChatColor.DARK_RED + "Error en tienda.retirarObjeto.1, hablar con el admin");
        }

        //Recorremos la tabla ofertas
        try {
            String consulta4 = "SELECT * FROM ofertas";
            Statement st4 = (Statement) conexion.createStatement();
            ResultSet rs4;
            rs4 = st4.executeQuery(consulta4);

            while (rs4.next()) {
                if (id_oferta == rs4.getInt("id_oferta")) {
                    nombreObjeto = rs4.getString("objeto");
                    cantidad = rs4.getInt("cantidad");
                    durabilidad = rs4.getInt("durabilidad");
                }
            }
        } catch (SQLException e) {

        }

        //Borramos la oferta del vendedor
        try {
            String consulta3 = "DELETE FROM ofertas WHERE id_oferta=\"" + id_oferta + "\"      ";
            Statement st3 = conexion.createStatement();
            st3.executeUpdate(consulta3);
        } catch (SQLException e) {
            p.sendMessage(ChatColor.DARK_RED + "Error en tienda.retirarObjeto.2, hablar con el admin");
        }

        //Quitamos un espacio ocupado al jugador
        try {
            int nventas = 0;
            int ingresos = 0;
            int gastos = 0;
            int beneficios = 0;
            int ocupados;
            int actual;

            String consulta6 = "UPDATE jugadores SET pixelcoin = ?, espacios = ?, nventas = ?, ingresos = ?, gastos = ?, beneficios = ? WHERE nombre = ?";
            PreparedStatement pst6 = (PreparedStatement) conexion.prepareStatement(consulta6);

            String consulta5 = "SELECT * FROM jugadores";
            Statement st5 = (Statement) conexion.createStatement();
            ResultSet rs5;
            rs5 = st5.executeQuery(consulta5);

            while (rs5.next()) {
                if (rs5.getString("nombre").equalsIgnoreCase(p.getName())) {
                    ocupados = rs5.getInt("espacios");
                    actual = rs5.getInt("pixelcoin");
                    nventas = rs5.getInt("nventas");
                    ingresos = rs5.getInt("ingresos");
                    gastos = rs5.getInt("gastos");
                    beneficios = rs5.getInt("beneficios");

                    pst6.setInt(1, actual);
                    pst6.setInt(2, ocupados - 1);
                    pst6.setInt(3, nventas);
                    pst6.setInt(4, ingresos);
                    pst6.setInt(5, gastos);
                    pst6.setInt(6, beneficios);
                    pst6.setString(7, p.getName());
                    pst6.executeUpdate();
                }
            }
        } catch (SQLException e) {
            p.sendMessage(ChatColor.DARK_RED + "Error en tienda.retirarObjeto.3, hablar con el admin");
        }


        //A�adimos el objeto al inventario
        itemA�adido = new ItemStack(Material.getMaterial(nombreObjeto), cantidad);
        itemA�adido.setDurability((short) durabilidad);

        if (encEncontrado) {
            itemA�adido = new ItemStack(Material.getMaterial(nombreObjeto), cantidad);
            for (Entry<String, Integer> entry : enchants.entrySet()) {
                int nivelEncontrado = entry.getValue();
                String encantamientoEncontrado = entry.getKey();
                Enchantment e = Enchantment.getByName(encantamientoEncontrado);

                ItemMeta EncMeta = itemA�adido.getItemMeta();
                EncMeta.addEnchant(e, nivelEncontrado, true);

                itemA�adido.setItemMeta(EncMeta);
            }
            itemA�adido.setDurability((short) durabilidad);
            p.getInventory().addItem(itemA�adido);
        } else {
            p.getInventory().addItem(itemA�adido);
        }

        p.sendMessage(ChatColor.GOLD + "Objeto retirado!");
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
    }

    //-----------Comprar objeto en la tienda-----------
    @SuppressWarnings({"unused", "deprecation"})
    public void ComprarObjeto(Player p, ItemStack i, Inventory in, int slot) {
        boolean encontradoEnc = false;
        int id_oferta = Integer.parseInt(i.getItemMeta().getLore().get(2));
        HashMap<String, Integer> enchants = new HashMap<String, Integer>();

        boolean compradorEnc = false;
        boolean vendedorEnc = false;

        int durabilidad = 0;
        int cantidad = 0;
        int precio = 0;
        String nombreObjeto = "";
        String vendedor = null;
        String comprador = p.getName();

        int actualComprador = 0;
        int espacioComprador = 0;
        int nventasComprador = 0;
        int ingresosComprador = 0;
        int gastosComprador = 0;
        int beneficiosComprador = 0;

        int actualVendedor = 0;
        int espacioVendedor = 0;
        int nventasVendedor = 0;
        int ingresosVendedor = 0;
        int gastosVendedor = 0;
        int beneficiosVendedor = 0;

        int id_actual = 0;

        int coste = 0;
        int bene = 0;

        ItemStack itemA�adido = null;
        //Recorremos las consulatas

        //OFERTAS, sacamos datos
        try {
            String consulta3 = "SELECT * FROM ofertas";
            Statement st3 = (Statement) conexion.createStatement();
            ResultSet rs3;
            rs3 = st3.executeQuery(consulta3);

            while (rs3.next()) {
                id_actual = rs3.getInt("id_oferta");
                if (id_actual == id_oferta) {
                    durabilidad = rs3.getInt("durabilidad");
                    cantidad = rs3.getInt("cantidad");
                    precio = rs3.getInt("precio");
                    nombreObjeto = rs3.getString("objeto");
                    vendedor = rs3.getString("nombre");
                }
            }
        } catch (SQLException e) {

        }

        //DINERO sacamos el dinero del comprador y el vendedor 
        try {
            String nombreActual = "";
            String consulta7 = "SELECT * FROM jugadores";
            Statement st7 = (Statement) conexion.createStatement();
            ResultSet rs7;
            rs7 = st7.executeQuery(consulta7);

            while (rs7.next()) {
                nombreActual = rs7.getString("nombre");
                if (nombreActual.equalsIgnoreCase(comprador)) {
                    nventasComprador = rs7.getInt("nventas");
                    ingresosComprador = rs7.getInt("ingresos");
                    gastosComprador = rs7.getInt("gastos");
                    beneficiosComprador = rs7.getInt("beneficios");
                    actualComprador = rs7.getInt("pixelcoin");
                    espacioComprador = rs7.getInt("espacios");

                    compradorEnc = true;
                }
                if (nombreActual.equalsIgnoreCase(vendedor)) {
                    espacioVendedor = rs7.getInt("espacios");
                    actualVendedor = rs7.getInt("pixelcoin");
                    nventasVendedor = rs7.getInt("nventas");
                    ingresosVendedor = rs7.getInt("ingresos");
                    gastosVendedor = rs7.getInt("gastos");
                    beneficiosVendedor = rs7.getInt("beneficios");

                    vendedorEnc = true;
                }
            }
        } catch (SQLException e) {
            p.sendMessage("a");
        }


        //Comprobar si el precio es mayor que 
        if (precio > actualComprador) {
            p.sendMessage(ChatColor.DARK_RED + "No puedes comprar nada por encima de tus posibilidades :v, toto");
        } else {
            //ENCANTAMIENTOS, recogemos los encantamientos en el hashmap y borramos la fila
            try {
                String consulta1 = "SELECT * FROM encantamientos";
                Statement st1 = (Statement) conexion.createStatement();
                ResultSet rs1;
                rs1 = st1.executeQuery(consulta1);

                while (rs1.next()) {
                    id_actual = rs1.getInt("id_oferta");

                    if (id_actual == id_oferta) {
                        encontradoEnc = true;
                        enchants.put(rs1.getString("encantamiento"), rs1.getInt("nivel"));
                    }
                }

                //Comprobamos si la cantidad es igual a 0 si lo es lo borramos
                if (cantidad == 1) {
                    String consulta2 = "DELETE FROM encantamientos WHERE id_oferta=\"" + id_oferta + "\"      ";
                    Statement st2 = conexion.createStatement();
                    st2.executeUpdate(consulta2);
                }

            } catch (SQLException e) {
                p.sendMessage(ChatColor.DARK_RED + "Error en tienda.ComprarObjeto.1, hablar con el admin");
            }

            //OFERTAS, borramos la fila oferta
            try {
                //Si la cantidad es 1 la borramos
                if (cantidad == 1) {
                    String consulta4 = "DELETE FROM ofertas WHERE id_oferta=\"" + id_oferta + "\"      ";
                    Statement st4 = conexion.createStatement();
                    st4.executeUpdate(consulta4);
                    //Si no es 1 le restamos 1 y lo modificamos
                } else {
                    String consulta12 = "UPDATE ofertas SET cantidad = ? WHERE id_oferta = ?";
                    PreparedStatement pst12 = (PreparedStatement) conexion.prepareStatement(consulta12);
                    pst12.setInt(1, (cantidad - 1));
                    pst12.setInt(2, id_oferta);
                    ;
                    pst12.executeUpdate();
                }

            } catch (SQLException e) {
                p.sendMessage(ChatColor.DARK_RED + "Error en tienda.ComprarObjeto.2, hablar con el admin");
            }

            //ESPACIOS, Si la cantidad es 1 quiere decir que no hay que borrar un espacio ya que sigue ocupando en la tienda 1 slot
            if (cantidad == 1) {
                espacioVendedor = espacioVendedor - 1;
            }

            //Ponemos los datos al comprador
            try {
                coste = actualComprador - precio;
                String consulta = "UPDATE jugadores SET pixelcoin = ?, espacios = ?, nventas = ?, ingresos = ?, gastos = ?, beneficios = ? WHERE nombre = ?";
                PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
                pst.setInt(1, coste);
                pst.setInt(2, espacioComprador);
                pst.setInt(3, nventasComprador);
                pst.setInt(4, ingresosComprador);
                pst.setInt(5, gastosComprador + precio);
                pst.setInt(6, ingresosComprador - (gastosComprador + precio));
                pst.setString(7, comprador);
                pst.executeUpdate();
            } catch (SQLException e) {

            }

            //Ponemos los datos al vendedor
            try {
                bene = actualVendedor + precio;
                String consulta = "UPDATE jugadores SET pixelcoin = ?, espacios = ?, nventas = ?, ingresos = ?, gastos = ?, beneficios = ? WHERE nombre = ?";
                PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
                pst.setInt(1, bene);
                pst.setInt(2, espacioVendedor);
                pst.setInt(3, nventasVendedor + 1);
                pst.setInt(4, ingresosVendedor + precio);
                pst.setInt(5, gastosVendedor);
                pst.setInt(6, (ingresosVendedor + precio) - gastosVendedor);
                pst.setString(7, vendedor);
                pst.executeUpdate();
            } catch (SQLException e) {

            }

            //A�ADIMOS ITEM
            itemA�adido = new ItemStack(Material.getMaterial(nombreObjeto), 1);
            itemA�adido.setDurability((short) durabilidad);

            if (encontradoEnc) {
                itemA�adido = new ItemStack(Material.getMaterial(nombreObjeto), 1);
                for (Entry<String, Integer> entry : enchants.entrySet()) {
                    int nivelEncontrado = entry.getValue();
                    String encantamientoEncontrado = entry.getKey();
                    Enchantment e = Enchantment.getByName(encantamientoEncontrado);

                    ItemMeta EncMeta = itemA�adido.getItemMeta();
                    EncMeta.addEnchant(e, nivelEncontrado, true);

                    itemA�adido.setItemMeta(EncMeta);
                }
                itemA�adido.setDurability((short) durabilidad);
                p.getInventory().addItem(itemA�adido);
            } else {
                p.getInventory().addItem(itemA�adido);
            }

            p.sendMessage(ChatColor.GOLD + "Has comprado: " + nombreObjeto + " , por " + ChatColor.GREEN + precio + " PC" + ChatColor.GOLD + " .Te quedan: " + ChatColor.GREEN + coste + " PC");
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

            Player vendedorP = Bukkit.getPlayerExact(vendedor);

            if (vendedorP != null) {
                vendedorP.sendMessage(ChatColor.GOLD + comprador + " te ha comprado: " + nombreObjeto + " por: " + ChatColor.GREEN + precio + " PC" + ChatColor.GOLD + " Ahora tienes: " + ChatColor.GREEN + bene + " PC!!");
                p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
            }
        }
    }

}