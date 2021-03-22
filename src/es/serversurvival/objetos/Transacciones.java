package es.serversurvival.objetos;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.mysql.jdbc.Connection;

import es.serversurvival.config.Funciones;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Transacciones extends MySQL {
    public DecimalFormat formatea = new DecimalFormat("###,###.##");

    public static final int DIAMANTE = 750;
    public static final int CUARZO = 20;
    public static final int LAPISLAZULI = 10;

    public String getFecha(int id) {
        String fecha = "";
        try {
            String consulta = "SELECT * FROM transacciones";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            while (rs.next()) {
                if (rs.getInt("id") == id) {
                    fecha = rs.getString("fecha");
                    break;
                }
            }
        } catch (SQLException e) {

        }
        return fecha;
    }

    public String getCcomprador(int id) {
        String comprador = "";
        try {
            String consulta = "SELECT * FROM transacciones";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            while (rs.next()) {
                if (rs.getInt("id") == id) {
                    comprador = rs.getString("comprador");
                    break;
                }
            }
        } catch (SQLException e) {

        }
        return comprador;
    }

    public String getVendedor(int id) {
        String vendedor = "";
        try {
            String consulta = "SELECT * FROM transacciones";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            while (rs.next()) {
                if (rs.getInt("id") == id) {
                    vendedor = rs.getString("vendedor");
                    break;
                }
            }
        } catch (SQLException e) {

        }
        return vendedor;
    }

    public int getCantidad(int id) {
        int cantidad = 0;
        try {
            String consulta = "SELECT * FROM transacciones";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            while (rs.next()) {
                if (rs.getInt("id") == id) {
                    cantidad = rs.getInt("cantidad");
                    break;
                }
            }
        } catch (SQLException e) {

        }
        return cantidad;
    }

    public String getObjeto(int id) {
        String objeto = "";
        try {
            String consulta = "SELECT * FROM transacciones";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            while (rs.next()) {
                if (rs.getInt("id") == id) {
                    objeto = rs.getString("objeto");
                    break;
                }
            }
        } catch (SQLException e) {

        }
        return objeto;
    }

    public String getTipo(int id) {
        String tipo = "";
        try {
            String consulta = "SELECT * FROM transacciones";
            Statement st = conexion.createStatement();
            ResultSet rs;
            rs = st.executeQuery(consulta);

            while (rs.next()) {
                if (rs.getInt("id") == id) {
                    tipo = rs.getString("tipo");
                    break;
                }
            }
        } catch (SQLException e) {

        }
        return tipo;
    }

    //A?adir transaccion
    public void nuevaTransaccion(String comprador, String vendedor, int cantidad, String objeto, String tipo) {
        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fecha = sdf.format(dt);

        try {
            String consulta = "INSERT INTO transacciones (fecha, comprador, vendedor, cantidad, objeto, tipo) VALUES ('" + fecha + "','" + comprador + "','" + vendedor + "','" + cantidad + "','" + objeto + "','" + tipo + "')";
            Statement st = (Statement) conexion.createStatement();
            st.executeUpdate(consulta);
        } catch (SQLException e) {

        }
    }

    //Get la id maxima
    public int getMaxId() {
        int id = 0;

        try {
            String consulta3 = "SELECT * FROM transacciones ORDER BY id DESC LIMIT 1 ";
            Statement st3 = (Statement) conexion.createStatement();
            ResultSet rs3;
            rs3 = st3.executeQuery(consulta3);

            while (rs3.next()) {
                id = rs3.getInt("id");
            }
        } catch (SQLException e) {

        }
        return id;
    }

    public void borrarTransaccione(int id) {
        try {
            String consulta = "DELETE FROM transacciones WHERE id= ?";
            PreparedStatement pst = (PreparedStatement) conexion.prepareStatement(consulta);
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {

        }
    }

    public void realizarVenta(String comprador, int id, Player p) {
        Jugador j = new Jugador();
        Ofertas o = new Ofertas();
        int cantidad = 0;
        int precio = 0;
        int dineroComprador = 0;
        String vendedor = "";
        ItemStack is = null;
        String objeto = "";

        try {
            j.conectar("root", "", "pixelcoins");
            dineroComprador = j.getDinero(comprador);
            j.desconectar();
        } catch (Exception e) {

        }

        try {
            o.conectar("root", "", "pixelcoins");
            //----------CODIGO------------
            is = o.getItemOferta(id);
            is.setAmount(1);

            cantidad = o.getCantidad(id);
            vendedor = o.getNombre(id);
            objeto = o.getObjeto(id);
            precio = o.getPrecio(id);

            if (dineroComprador < precio) {
                p.sendMessage(ChatColor.DARK_RED + "No puedes comprar por encima de tu dinero");
                return;
            }

            if (cantidad == 1) {
                o.borrarOferta(id, vendedor);
            } else {
                cantidad = cantidad - 1;
                o.setCantidad(id, cantidad);
            }
            o.mostarOfertas(p);

            o.desconectar();
        } catch (Exception e) {
            p.sendMessage(ChatColor.DARK_RED + "Error en Transacciones.realizarVenta");
        }

        p.getInventory().addItem(is);

        this.realizarTransferencia(comprador, vendedor, precio, objeto, "TIENDA", true);

        p.sendMessage(ChatColor.GOLD + "Has comprado: " + objeto + " , por " + ChatColor.GREEN + formatea.format(precio) + " PC" + ChatColor.GOLD + " .Te quedan: " + ChatColor.GREEN + formatea.format(dineroComprador - precio) + " PC");
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

        Player vendedorP = Bukkit.getPlayerExact(vendedor);

        if (vendedorP != null) {
            vendedorP.sendMessage(ChatColor.GOLD + comprador + " te ha comprado: " + objeto + " por: " + ChatColor.GREEN + formatea.format(precio) + " PC " + ChatColor.AQUA + "(/estadisticas)");
            ;
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
        }
    }

    //Realizar pago de dos jugadores
    public void realizarTransferencia(String nombrePagador, String nombrePagado, int cantidad, String objeto, String tipo, boolean editarEstadisticas) {
        Jugador j = new Jugador();

        try {
            j.conectar("root", "", "pixelcoins");
            //-------------CODIGO-------------
            int dineroPagador = 0;
            dineroPagador = j.getDinero(nombrePagador);

            boolean pagadoRe = j.estaRegistrado(nombrePagado);
            //Si se editan estadisticas se edita los ingresos, gastos,bene, nventas si no no
            if (pagadoRe == false) {

                if (editarEstadisticas) {
                    j.nuevoJugador(nombrePagado, cantidad, 0, 1, cantidad, 0, cantidad, 0, 0);
                } else {
                    j.nuevoJugador(nombrePagado, cantidad, 0, 0, 0, 0, 0, 0, 0);
                }

            } else {
                if (editarEstadisticas) {
                    int dineroPagado = j.getDinero(nombrePagado) + cantidad;
                    int nventasPagado = j.getNventas(nombrePagado) + 1;
                    int ingresosPagado = j.getIngresos(nombrePagado) + cantidad;
                    int gastosPagado = j.getGastos(nombrePagado);

                    j.setEstadisticas(nombrePagado, dineroPagado, nventasPagado, ingresosPagado, gastosPagado);
                } else {
                    int dineroPagado = j.getDinero(nombrePagado) + cantidad;
                    j.setPixelcoin(nombrePagado, dineroPagado);
                }

            }

            if (editarEstadisticas) {
                dineroPagador = dineroPagador - cantidad;
                int nventasPagador = j.getNventas(nombrePagador);
                int ingresosPagador = j.getIngresos(nombrePagador);
                int gastosPagador = j.getGastos(nombrePagador) + cantidad;

                j.setEstadisticas(nombrePagador, dineroPagador, nventasPagador, ingresosPagador, gastosPagador);
            } else {
                dineroPagador = dineroPagador - cantidad;
                j.setPixelcoin(nombrePagador, dineroPagador);
            }


            j.desconectar();
        } catch (Exception e) {

        }
        this.nuevaTransaccion(nombrePagador, nombrePagado, cantidad, objeto, tipo);

    }

    //Realizar pago manual
    public void realizarPagoManual(String nombrePagador, String nombrePagado, int cantidad, Player p, String objeto, String tipo) {
        int dineroPagador = 0;
        try {
            Jugador j = new Jugador();
            j.conectar("root", "", "pixelcoins");
            dineroPagador = j.getDinero(nombrePagador);
            j.desconectar();
        } catch (Exception e) {

        }

        if (dineroPagador >= cantidad) {
            this.realizarTransferencia(nombrePagador, nombrePagado, cantidad, objeto, tipo, true);
            Player tp = Bukkit.getServer().getPlayer(nombrePagado);

            p.sendMessage(ChatColor.GOLD + "Has pagado: " + ChatColor.GREEN + formatea.format(cantidad) + " PC " + ChatColor.GOLD + "a " + nombrePagado);

            tp.playSound(tp.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
            tp.sendMessage(ChatColor.GOLD + nombrePagador + " te ha pagado: " + ChatColor.GREEN + "+" + formatea.format(cantidad) + " PC " + ChatColor.AQUA + "(/estadisticas)");
        } else {
            p.sendMessage(ChatColor.DARK_RED + "No puedes pagar a un jugador por encima de tu dinero :v");
        }
    }

    //ingresarItem
    public void ingresarItem(ItemStack im, Player p, int slot) {
        int pixelcoinsAnadir = 0;
        String tipoIm = im.getType().toString();
        Jugador j = new Jugador();
        try {
            j.conectar("root", "", "pixelcoins");
            //----------CODIGO----------
            boolean registrado = j.estaRegistrado(p.getName());
            int cantidad = im.getAmount();
            int dineroActual = j.getDinero(p.getName());

            switch (tipoIm) {
                case "DIAMOND":
                    pixelcoinsAnadir = cantidad * DIAMANTE;
                    break;
                case "DIAMOND_BLOCK":
                    pixelcoinsAnadir = cantidad * DIAMANTE * 9;
                    break;
                case "LAPIS_LAZULI":
                    pixelcoinsAnadir = cantidad * LAPISLAZULI;
                    break;
                case "LAPIS_BLOCK":
                    pixelcoinsAnadir = cantidad * LAPISLAZULI * 9;
                    break;
                case "QUARTZ_BLOCK":
                    pixelcoinsAnadir = cantidad * CUARZO;
                    break;
                default:
                    break;

            }

            if (registrado == true) {
                j.setPixelcoin(p.getName(), pixelcoinsAnadir + dineroActual);
            } else {
                j.nuevoJugador(p.getName(), pixelcoinsAnadir, 0, 0, 0, 0, 0, 0, 0);
            }

            p.getInventory().clear(slot);

            p.sendMessage(ChatColor.GOLD + "Se ha a?adido: " + ChatColor.GREEN + formatea.format(pixelcoinsAnadir) + " PC " + ChatColor.GOLD + "Ahora tienes: " + ChatColor.GREEN + formatea.format(pixelcoinsAnadir + dineroActual) + "PC");
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
            j.desconectar();
        } catch (Exception e) {

        }
        this.nuevaTransaccion(p.getName(), "", pixelcoinsAnadir, tipoIm, "INGRESAR");
    }

    //SacarItem
    public void sacarItem(Player p, String tipo) {
        Jugador j = new Jugador();
        Inventory in = p.getInventory();
        try {
            j.conectar("root", "", "pixelcoins");
            //--------CODIGO-----------
            int actualPixelcoins = j.getDinero(p.getName());
            int cantidad = 0;

            switch (tipo) {
                case "DIAMOND":
                    if (actualPixelcoins >= DIAMANTE) {
                        cantidad = DIAMANTE;
                        j.setPixelcoin(p.getName(), actualPixelcoins - DIAMANTE);
                        in.addItem(new ItemStack(Material.DIAMOND, 1));

                        p.sendMessage(ChatColor.GOLD + "Se ha a?adido un diamante. " + ChatColor.RED + "-" + DIAMANTE + " PC" + ChatColor.GOLD + "Quedan " + ChatColor.GREEN + formatea.format(actualPixelcoins - DIAMANTE) + " PC");
                        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
                        break;
                    } else {
                        p.sendMessage(ChatColor.DARK_RED + "Necesitas tener minimo " + DIAMANTE + " pixelcoins para convertirlo a diamantes.");
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);

                        return;
                    }

                case "QUARTZ_BLOCK":
                    if (actualPixelcoins >= CUARZO) {
                        cantidad = CUARZO;
                        j.setPixelcoin(p.getName(), actualPixelcoins - CUARZO);
                        p.getInventory().addItem(new ItemStack(Material.QUARTZ_BLOCK, 1));

                        p.sendMessage(ChatColor.GOLD + "Se ha a?adido un bloque de cuarzo. " + ChatColor.RED + "-" + CUARZO + " PC" + ChatColor.GOLD + "Quedan " + ChatColor.GREEN + formatea.format(actualPixelcoins - CUARZO) + " PC");
                        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
                        break;
                    } else {
                        p.sendMessage(ChatColor.DARK_RED + "Necesitas tener minimo " + CUARZO + " pixelcoins para convertirlo a bloques de cuarzo.");
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);

                        return;
                    }

                case "LAPIS_LAZULI":
                    if (actualPixelcoins >= LAPISLAZULI) {
                        cantidad = LAPISLAZULI;
                        j.setPixelcoin(p.getName(), actualPixelcoins - LAPISLAZULI);
                        p.getInventory().addItem(new ItemStack(Material.LAPIS_LAZULI, 1));

                        p.sendMessage(ChatColor.GOLD + "Se ha a?adido lapislazuli. " + ChatColor.RED + "-" + LAPISLAZULI + " PC" + ChatColor.GOLD + "Quedan " + ChatColor.GREEN + formatea.format(actualPixelcoins - LAPISLAZULI) + " PC");
                        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
                        break;
                    } else {
                        p.sendMessage(ChatColor.DARK_RED + "Necesitas tener minimo " + LAPISLAZULI + " pixelcoins para convertirlo a lapislazuli .");
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);

                        return;
                    }
                default:
                    break;
            }
            this.nuevaTransaccion(p.getName(), "", cantidad, tipo, "SACAR");

            j.desconectar();
        } catch (Exception e) {

        }
    }

    //SacarMaximos items
    public void sacarMaxItem(String tipo, Player p) {
        Funciones f = new Funciones();
        Jugador j = new Jugador();
        int espaciosLibres = f.espaciosLibres(p.getInventory());
        int cambio = 0;
        int convertibles = 0;
        int items = 0;
        int bloques = 0;
        int bloquesAnadidos = 0;
        int itemsAnadidos = 0;
        int actuales = 0;
        Inventory in = p.getInventory();
        String item = "";
        String bloque = "";
        int coste = 0;

        try {
            j.conectar("root", "", "pixelcoins");
            actuales = j.getDinero(p.getName());
            //--------CODIGO------------
            switch (tipo) {
                case "DIAMOND":
                    if (actuales >= DIAMANTE) {
                        item = tipo;
                        bloque = "DIAMOND_BLOCK";
                        cambio = DIAMANTE;
                        break;
                    } else {
                        p.sendMessage(ChatColor.DARK_RED + "Necesitas tener minimo " + DIAMANTE + " pixelcoins para intermcabiarlas a diamantes");
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
                        return;
                    }
                case "LAPIS_LAZULI":
                    if (actuales >= LAPISLAZULI) {
                        item = tipo;
                        bloque = "LAPIS_BLOCK";
                        cambio = LAPISLAZULI;
                        break;
                    } else {
                        p.sendMessage(ChatColor.DARK_RED + "Necesitas tener minimo " + LAPISLAZULI + " pixelcoins para intermcabiarlas a diamantes");
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
                        return;
                    }
                case "QUARTZ_BLOCK":
                    if (actuales >= CUARZO) {
                        item = tipo;
                        bloque = "QUARTZ_BLOCK";
                        cambio = CUARZO;
                        break;
                    } else {
                        p.sendMessage(ChatColor.DARK_RED + "Necesitas tener minimo " + LAPISLAZULI + " pixelcoins para intermcabiarlas a diamantes");
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
                        return;
                    }
                default:
                    break;
            }
            if (cambio == CUARZO) {
                bloques = (actuales - (actuales % cambio)) / cambio;
            } else {
                convertibles = actuales - (actuales % cambio);
                items = (convertibles / cambio) % 9;
                bloques = ((convertibles / cambio) - items) / 9;
            }

            int[] slotsBloques = f.slotsItem(bloques, espaciosLibres);

            //A?adir bloques
            for (int i = 0; i < slotsBloques.length; i++) {
                bloquesAnadidos = bloquesAnadidos + slotsBloques[i];
                in.addItem(new ItemStack(Material.getMaterial(bloque), slotsBloques[i]));
            }

            int[] slotsDiamantes = f.slotsItem(items, f.espaciosLibres(in));
            //A?adir diamantes
            for (int i = 0; i < slotsDiamantes.length; i++) {
                itemsAnadidos = itemsAnadidos + slotsDiamantes[i];
                in.addItem(new ItemStack(Material.getMaterial(item), slotsDiamantes[i]));
            }

            if (cambio == CUARZO) {
                coste = (cambio * bloquesAnadidos);
                j.setPixelcoin(p.getName(), actuales - coste);
            } else {
                coste = (cambio * bloquesAnadidos * 9) + (cambio * itemsAnadidos);
                j.setPixelcoin(p.getName(), actuales - coste);
            }


            j.desconectar();
        } catch (Exception e) {

        }

        if (cambio == DIAMANTE) {
            p.sendMessage(ChatColor.GOLD + "Se ha a?adio: " + ChatColor.AQUA + "+" + bloquesAnadidos + " bloques " + "+" + itemsAnadidos + " diamantes. " + ChatColor.RED + "-" + formatea.format(coste)
                    + ChatColor.GOLD + " Quedan: " + ChatColor.GREEN + formatea.format(actuales - coste) + " PC");
        } else if (cambio == LAPISLAZULI) {
            p.sendMessage(ChatColor.GOLD + "Se ha a?adio: " + ChatColor.BLUE + "+" + bloquesAnadidos + " bloques " + "+" + itemsAnadidos + " Lapislazuli. " + ChatColor.RED + "-" + formatea.format(coste)
                    + ChatColor.GOLD + " Quedan: " + ChatColor.GREEN + formatea.format(actuales - coste) + " PC");
        } else {
            p.sendMessage(ChatColor.GOLD + "Se ha a?adio: " + ChatColor.GRAY + "+" + bloquesAnadidos + " bloques de cuarzo " + ChatColor.RED + "-" + formatea.format(coste)
                    + ChatColor.GOLD + " Quedan: " + ChatColor.GREEN + formatea.format(actuales - coste) + " PC");
        }
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
        this.nuevaTransaccion(p.getName(), "", coste, tipo, "SACAR_MAX");
    }

    //DEPOSITAR
    public void depositarPixelcoins(Player p, int pixelcoins, String nombreEmpresa) {
        try {
            Jugador j = new Jugador();
            Empresas em = new Empresas();
            j.conectar("root", "", "pixelcoins");
            String nombreJugador = p.getName();
            int pixelcoinsJugador = j.getDinero(nombreJugador);

            if (pixelcoinsJugador < pixelcoins) {
                p.sendMessage(ChatColor.DARK_RED + "No puedes meter mas dinero en la empresa del que tienes");
                j.desconectar();
                return;
            }
            em.conectar("root", "", "pixelcoins");
            boolean reg = em.estaRegistradoNombre(nombreEmpresa);

            if (reg == false) {
                p.sendMessage(ChatColor.DARK_RED + "Esa empresa no existe");
                em.desconectar();
                j.desconectar();
                return;
            }
            boolean es = em.esOwner(nombreJugador, nombreEmpresa);
            if (es == false) {
                p.sendMessage(ChatColor.DARK_RED + "No eres due?o de esa empresa");
                em.desconectar();
                j.desconectar();
                return;
            }

            int pixelcoinsEmpresa = em.getPixelcoins(nombreEmpresa);
            em.setPixelcoins(nombreEmpresa, pixelcoinsEmpresa + pixelcoins);
            j.setEstadisticas(nombreJugador, pixelcoinsJugador - pixelcoins, j.getNventas(nombreJugador), j.getIngresos(nombreJugador), j.getGastos(nombreJugador) + pixelcoins);

            this.nuevaTransaccion(nombreJugador, nombreEmpresa, pixelcoins, "", "DEPOSITAR");

            p.sendMessage(ChatColor.GOLD + "Has metido " + ChatColor.GREEN + formatea.format(pixelcoins) + " PC" + ChatColor.GOLD + " en tu empresa: " + ChatColor.DARK_AQUA + nombreEmpresa + ChatColor.GOLD + " ahora tiene: " + ChatColor.GREEN + formatea.format(pixelcoinsEmpresa + pixelcoins) + " PC");
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

            em.desconectar();
            j.desconectar();
        } catch (Exception e) {
            p.sendMessage("error");
        }
    }

    //SACAR
    public void sacarPixelcoins(Player p, int pixelcoins, String nombreEmpresa) {
        try {
            Empresas em = new Empresas();
            em.conectar("root", "", "pixelcoins");
            String nombreJugador = p.getName();

            boolean reg = em.estaRegistradoNombre(nombreEmpresa);

            if (reg == false) {
                p.sendMessage(ChatColor.DARK_RED + "Esa empresa no existe");
                em.desconectar();
                return;
            }
            boolean es = em.esOwner(nombreJugador, nombreEmpresa);
            if (es == false) {
                p.sendMessage(ChatColor.DARK_RED + "No eres due?o de esa empresa");
                em.desconectar();
                return;
            }
            int pixelcoinsEmpresa = em.getPixelcoins(nombreEmpresa);
            if (pixelcoinsEmpresa < pixelcoins) {
                p.sendMessage(ChatColor.DARK_RED + "No puedes sacar mas dinero" +
                        "+ del que la empresa tiene");
                em.desconectar();
                return;
            }
            Jugador j = new Jugador();
            j.conectar("root", "", "pixelcoins");

            int pixelcoinsJugador = j.getDinero(nombreJugador);
            em.setPixelcoins(nombreEmpresa, pixelcoinsEmpresa - pixelcoins);
            j.setEstadisticas(nombreJugador, pixelcoinsJugador + pixelcoins, j.getNventas(nombreJugador), j.getIngresos(nombreJugador), j.getGastos(nombreJugador));

            this.nuevaTransaccion(nombreEmpresa, nombreJugador, pixelcoins, "", "SACAR");

            p.sendMessage(ChatColor.GOLD + "Has sacado " + ChatColor.GREEN + formatea.format(pixelcoins) + " PC" + ChatColor.GOLD + " de tu empresa: " + ChatColor.DARK_AQUA + nombreEmpresa + ChatColor.GOLD + " ahora tiene: " + ChatColor.GREEN + formatea.format(pixelcoinsEmpresa - pixelcoins) + " PC");
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

            em.desconectar();
            j.desconectar();
        } catch (Exception e) {
            p.sendMessage("error");
        }
    }

    public void comprarEmpresa(String vendedor, String comprador, String empresa, int precio, Player p) {
        try {
            Empresas em = new Empresas();
            em.conectar("root", "", "pixelcoins");
            em.setOwner(empresa, comprador);
            em.desconectar();
        } catch (Exception e) {
            p.sendMessage(ChatColor.DARK_RED + "Error 1");
        }

        try {
            Jugador j = new Jugador();
            j.conectar("root", "", "pixelcoins");

            j.setEstadisticas(vendedor, j.getDinero(vendedor) + precio, j.getNventas(vendedor) + 1, j.getIngresos(vendedor) + precio, j.getGastos(vendedor));
            j.setEstadisticas(comprador, j.getDinero(comprador) - precio, j.getNventas(comprador), j.getIngresos(comprador), j.getGastos(comprador) + precio);

            j.desconectar();
        } catch (Exception e) {
            p.sendMessage(ChatColor.DARK_RED + "eRROR 2");
        }
    }

    public boolean pagarSalario(String jugador, String empresa, int salario) {
        try {
            Empresas em = new Empresas();
            em.conectar("root", "", "pixelcoins");
            if (em.getPixelcoins(empresa) < salario) {
                return false;
            }

            em.setPixelcoins(empresa, em.getPixelcoins(empresa) - salario);
            em.setGastos(empresa, em.getGastos(empresa) + salario);
            em.desconectar();
        } catch (Exception e) {
            return false;
        }
        try {
            Jugador j = new Jugador();
            j.conectar("root", "", "pixelcoins");
            j.setEstadisticas(jugador, j.getDinero(jugador) + salario, j.getNventas(jugador), j.getIngresos(jugador) + salario, j.getGastos(jugador));
            j.desconectar();
            this.nuevaTransaccion(jugador, empresa, salario, "", "SUELDO");
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}