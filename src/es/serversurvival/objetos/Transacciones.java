package es.serversurvival.objetos;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

import es.serversurvival.task.ScoreboardPlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


import es.serversurvival.main.Funciones;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

@SuppressWarnings("SpellCheckingInspection")
public class Transacciones extends MySQL {
    public DecimalFormat formatea = new DecimalFormat("###,###.##");
    private ScoreboardPlayer sp = new ScoreboardPlayer();

    public static final int DIAMANTE = 750;
    public static final int CUARZO = 20;
    public static final int LAPISLAZULI = 10;

    public String getFecha(int id) {
        String fecha = "";
        try {
            String consulta = "SELECT fecha FROM transacciones WHERE id = ?";
            PreparedStatement pst = conexion.prepareStatement(consulta);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                fecha = rs.getString("fecha");
                break;
            }
            rs.close();
        } catch (SQLException e) {

        }
        return fecha;
    }

    public String getCcomprador(int id) {
        String comprador = "";
        try {
            String consulta = "SELECT comprador FROM transacciones WHERE id = ?";
            PreparedStatement pst = conexion.prepareStatement(consulta);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                comprador = rs.getString("comprador");
                break;
            }
            rs.close();
        } catch (SQLException e) {

        }
        return comprador;
    }

    public String getVendedor(int id) {
        String vendedor = "";
        try {
            String consulta = "SELECT vendedor FROM transacciones WHERE id = ?";
            PreparedStatement pst = conexion.prepareStatement(consulta);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                vendedor = rs.getString("vendedor");
                break;
            }
            rs.close();
        } catch (SQLException e) {

        }
        return vendedor;
    }

    public int getCantidad(int id) {
        int cantidad = 0;
        try {
            String consulta = "SELECT cantidad FROM transacciones WHERE id = ?";
            PreparedStatement pst = conexion.prepareStatement(consulta);
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

    public String getObjeto(int id) {
        String objeto = "";
        try {
            String consulta = "SELECT objeto FROM transacciones WHERE id = ?";
            PreparedStatement pst = conexion.prepareStatement(consulta);
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

    public String getTipo(int id) {
        String tipo = "";
        try {
            String consulta = "SELECT tipo FROM transacciones WHERE id = ?";
            PreparedStatement pst = conexion.prepareStatement(consulta);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                tipo = rs.getString("tipo");
                break;
            }
            rs.close();
        } catch (SQLException e) {

        }
        return tipo;
    }

    //A?adir transaccion
    public void aux(String comprador, String vendedor, int cantidad, String objeto, String tipo) {
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

    public void nuevaTransaccion(String comprador, String vendedor, int cantidad, String objeto, TIPO tipos) {
        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fecha = sdf.format(dt);
        String tipo = tipos.toString();

        try {
            String consulta = "INSERT INTO transacciones (fecha, comprador, vendedor, cantidad, objeto, tipo) VALUES ('" + fecha + "','" + comprador + "','" + vendedor + "','" + cantidad + "','" + objeto + "','" + tipo + "')";
            Statement st = (Statement) conexion.createStatement();
            st.executeUpdate(consulta);
        } catch (SQLException e) {

        }
    }

    public int getMaxId() {
        int id = 0;
        try {
            String consulta3 = "SELECT id FROM transacciones ORDER BY id DESC LIMIT 1 ";
            Statement st3 = (Statement) conexion.createStatement();
            ResultSet rs3;
            rs3 = st3.executeQuery(consulta3);

            while (rs3.next()) {
                id = rs3.getInt("id");
                break;
            }
            rs3.close();
        } catch (SQLException e) {

        }
        return id;
    }

    public void borrarTransaccione(int id) {
        try {
            String consulta = "DELETE FROM transacciones WHERE id = ?";
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

        dineroComprador = j.getDinero(comprador);

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

        p.getInventory().addItem(is);

        this.realizarTransferencia(comprador, vendedor, precio, objeto, TIPO.TIENDA_VENTA, true);

        p.sendMessage(ChatColor.GOLD + "Has comprado: " + objeto + " , por " + ChatColor.GREEN + formatea.format(precio) + " PC" + ChatColor.GOLD + " .Te quedan: " + ChatColor.GREEN + formatea.format(dineroComprador - precio) + " PC");
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

        Player vendedorP = Bukkit.getPlayerExact(vendedor);

        if (vendedorP != null) {
            vendedorP.sendMessage(ChatColor.GOLD + comprador + " te ha comprado: " + objeto + " por: " + ChatColor.GREEN + formatea.format(precio) + " PC " + ChatColor.AQUA + "(/estadisticas)");
            ;
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
            sp.updateScoreboard(vendedorP);
        }

        sp.updateScoreboard(p);
    }

    //Realizar pago de dos jugadores
    public void realizarTransferencia(String nombrePagador, String nombrePagado, int cantidad, String objeto, TIPO tipo, boolean editarEstadisticas) {
        Jugador j = new Jugador();

        try {
            //-------------CODIGO-------------
            int dineroPagador = 0;
            dineroPagador = j.getDinero(nombrePagador);

            boolean pagadoRe = j.estaRegistrado(nombrePagado);
            //Si se editan estadisticas se edita los ingresos, gastos,bene, nventas si no no
            if (!pagadoRe) {

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

        } catch (Exception e) {

        }
        this.nuevaTransaccion(nombrePagador, nombrePagado, cantidad, objeto, tipo);
    }

    //Realizar pago manual
    public void realizarPagoManual(String nombrePagador, String nombrePagado, int cantidad, Player p, String objeto, TIPO tipo) {
        int dineroPagador = 0;
        Jugador j = new Jugador();
        Player tp = null;
        dineroPagador = j.getDinero(nombrePagador);

        if (dineroPagador >= cantidad) {
            this.realizarTransferencia(nombrePagador, nombrePagado, cantidad, objeto, tipo, true);
            tp = Bukkit.getServer().getPlayer(nombrePagado);

            p.sendMessage(ChatColor.GOLD + "Has pagado: " + ChatColor.GREEN + formatea.format(cantidad) + " PC " + ChatColor.GOLD + "a " + nombrePagado);

            tp.playSound(tp.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
            tp.sendMessage(ChatColor.GOLD + nombrePagador + " te ha pagado: " + ChatColor.GREEN + "+" + formatea.format(cantidad) + " PC " + ChatColor.AQUA + "(/estadisticas)");
        } else {
            p.sendMessage(ChatColor.DARK_RED + "No puedes pagar a un jugador por encima de tu dinero :v");
        }
        sp.updateScoreboard(p);
        sp.updateScoreboard(tp);
    }

    //ingresarItem
    public void ingresarItem(ItemStack im, Player p, int slot) {
        int pixelcoinsAnadir = 0;
        String tipoIm = im.getType().toString();
        Jugador j = new Jugador();

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

        if (registrado) {
            j.setPixelcoin(p.getName(), pixelcoinsAnadir + dineroActual);
        } else {
            j.nuevoJugador(p.getName(), pixelcoinsAnadir, 0, 0, 0, 0, 0, 0, 0);
        }

        p.getInventory().clear(slot);

        p.sendMessage(ChatColor.GOLD + "Se ha a?adido: " + ChatColor.GREEN + formatea.format(pixelcoinsAnadir) + " PC " + ChatColor.GOLD + "Ahora tienes: " + ChatColor.GREEN + formatea.format(pixelcoinsAnadir + dineroActual) + "PC");
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

        this.nuevaTransaccion(p.getName(), "", pixelcoinsAnadir, tipoIm, TIPO.WITHERS_INGRESAR);
        sp.updateScoreboard(p);
    }

    //SacarItem
    public void sacarItem(Player p, String tipo) {
        Jugador j = new Jugador();
        Inventory in = p.getInventory();
        try {
            j.conectar();
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
            this.nuevaTransaccion(p.getName(), "", cantidad, tipo, TIPO.WITHERS_SACAR);

            j.desconectar();
        } catch (Exception e) {

        }
        sp.updateScoreboard(p);
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


        actuales = j.getDinero(p.getName());
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
        this.nuevaTransaccion(p.getName(), "", coste, tipo, TIPO.WITHERS_SACARMAX);
        sp.updateScoreboard(p);
    }

    //DEPOSITAR
    public void depositarPixelcoins(Player p, int pixelcoins, String nombreEmpresa) {
        Jugador j = new Jugador();
        Empresas em = new Empresas();

        String nombreJugador = p.getName();
        int pixelcoinsJugador = j.getDinero(nombreJugador);

        if (pixelcoinsJugador < pixelcoins) {
            p.sendMessage(ChatColor.DARK_RED + "No puedes meter mas dinero en la empresa del que tienes");
            return;
        }

        boolean reg = em.estaRegistradoNombre(nombreEmpresa);

        if (!reg) {
            p.sendMessage(ChatColor.DARK_RED + "Esa empresa no existe");
            return;
        }
        boolean es = em.esOwner(nombreJugador, nombreEmpresa);
        if (!es) {
            p.sendMessage(ChatColor.DARK_RED + "No eres due?o de esa empresa");
            return;
        }

        int pixelcoinsEmpresa = em.getPixelcoins(nombreEmpresa);
        em.setPixelcoins(nombreEmpresa, pixelcoinsEmpresa + pixelcoins);
        j.setEstadisticas(nombreJugador, pixelcoinsJugador - pixelcoins, j.getNventas(nombreJugador), j.getIngresos(nombreJugador), j.getGastos(nombreJugador));

        this.nuevaTransaccion(nombreJugador, nombreEmpresa, pixelcoins, "", TIPO.EMPRESA_DEPOSITAR);
        sp.updateScoreboard(p);
        p.sendMessage(ChatColor.GOLD + "Has metido " + ChatColor.GREEN + formatea.format(pixelcoins) + " PC" + ChatColor.GOLD + " en tu empresa: " + ChatColor.DARK_AQUA + nombreEmpresa + ChatColor.GOLD + " ahora tiene: " + ChatColor.GREEN + formatea.format(pixelcoinsEmpresa + pixelcoins) + " PC");
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
    }

    //SACAR
    public void sacarPixelcoins(Player p, int pixelcoins, String nombreEmpresa) {
        Empresas em = new Empresas();
        ;
        String nombreJugador = p.getName();

        boolean reg = em.estaRegistradoNombre(nombreEmpresa);

        if (!reg) {
            p.sendMessage(ChatColor.DARK_RED + "Esa empresa no existe");
            return;
        }
        boolean es = em.esOwner(nombreJugador, nombreEmpresa);
        if (!es) {
            p.sendMessage(ChatColor.DARK_RED + "No eres due?o de esa empresa");
            return;
        }
        int pixelcoinsEmpresa = em.getPixelcoins(nombreEmpresa);
        if (pixelcoinsEmpresa < pixelcoins) {
            p.sendMessage(ChatColor.DARK_RED + "No puedes sacar mas dinero" +
                    "+ del que la empresa tiene");
            return;
        }
        Jugador j = new Jugador();

        int pixelcoinsJugador = j.getDinero(nombreJugador);
        em.setPixelcoins(nombreEmpresa, pixelcoinsEmpresa - pixelcoins);
        j.setEstadisticas(nombreJugador, pixelcoinsJugador + pixelcoins, j.getNventas(nombreJugador), j.getIngresos(nombreJugador), j.getGastos(nombreJugador));

        this.nuevaTransaccion(nombreEmpresa, nombreJugador, pixelcoins, "", TIPO.EMPRESA_SACAR);
        sp.updateScoreboard(p);

        p.sendMessage(ChatColor.GOLD + "Has sacado " + ChatColor.GREEN + formatea.format(pixelcoins) + " PC" + ChatColor.GOLD + " de tu empresa: " + ChatColor.DARK_AQUA + nombreEmpresa + ChatColor.GOLD + " ahora tiene: " + ChatColor.GREEN + formatea.format(pixelcoinsEmpresa - pixelcoins) + " PC");
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
    }

    public void comprarEmpresa(String vendedor, String comprador, String empresa, int precio, Player p) {
        Empresas em = new Empresas();
        em.setOwner(empresa, comprador);

        Jugador j = new Jugador();
        j.setEstadisticas(vendedor, j.getDinero(vendedor) + precio, j.getNventas(vendedor) + 1, j.getIngresos(vendedor) + precio, j.getGastos(vendedor));
        j.setEstadisticas(comprador, j.getDinero(comprador) - precio, j.getNventas(comprador), j.getIngresos(comprador), j.getGastos(comprador) + precio);

        Player tp = Bukkit.getPlayer(comprador);

        if (tp != null) {
            sp.updateScoreboard(tp);
        }
        sp.updateScoreboard(p);
    }

    public boolean pagarSalario(String jugador, String empresa, int salario) {
        Empresas em = new Empresas();
        if (em.getPixelcoins(empresa) < salario) {
            return false;
        }
        em.setPixelcoins(empresa, em.getPixelcoins(empresa) - salario);
        em.setGastos(empresa, em.getGastos(empresa) + salario);

        Jugador j = new Jugador();
        j.setEstadisticas(jugador, j.getDinero(jugador) + salario, j.getNventas(jugador), j.getIngresos(jugador) + salario, j.getGastos(jugador));
        this.nuevaTransaccion(jugador, empresa, salario, "", TIPO.EMPRESA_PAGAR_SALARIO);

        return true;
    }

    public void comprarServivio(String empresa, int precio, Player p) {
        String comprador = p.getName();
        String owner = "";
        Empresas em = new Empresas();

        boolean reg = em.estaRegistradoNombre(empresa);
        if (!reg) {
            p.sendMessage(ChatColor.DARK_RED + "Esa empresa no existe");
            return;
        }
        boolean ow = em.esOwner(comprador, empresa);
        if (ow) {
            p.sendMessage(ChatColor.DARK_RED + "No puedes comprar un servivio de tu propia empresa siendo el propio owner");
            return;
        }

        owner = em.getOwner(empresa);
        Jugador j = new Jugador();

        if (j.getDinero(comprador) < precio) {
            p.sendMessage(ChatColor.DARK_RED + "No puedes ir por encima de tus posibilidades");
            return;
        }
        j.setEstadisticas(comprador, j.getDinero(comprador) - precio, j.getNventas(comprador), j.getIngresos(comprador), j.getGastos(comprador) + precio);

        em.setPixelcoins(empresa, em.getPixelcoins(empresa) + precio);
        em.setIngresos(empresa, em.getIngresos(empresa) + precio);

        Empleados empl = new Empleados();

        ArrayList<String> empleados = new ArrayList<String>();
        empleados.add(owner);
        Player tp = null;

        for (int i = 0; i < empleados.size(); i++) {
            tp = p.getServer().getPlayer(empleados.get(i));

            if (tp != null) {
                tp.sendMessage(ChatColor.GOLD + comprador + " ha comprado vuestro servicio de la empresa: " + empresa + " por " + ChatColor.GREEN + precio + " PC");
            } else {
                if (empleados.get(i).equalsIgnoreCase(owner)) {
                    Mensajes men = new Mensajes();
                    men.nuevoMensaje(owner, " el jugador: " + comprador + " ha comprado un servicio de tu empresa: " + empresa + " por " + precio + " PC");
                }
                sp.updateScoreboard(tp);
            }
        }

        p.sendMessage(ChatColor.GOLD + "Has pagado " + ChatColor.GREEN + precio + " PC " + ChatColor.GOLD + " a la empresa: " + empresa + " por su servicio");


        this.nuevaTransaccion(comprador, empresa, precio, "", TIPO.EMPRESA_COMPRAR_SERVICIO);
    }

    public enum TIPO {
        TIENDA_VENTA,
        JUGADOR_PAGO_MANUAL,
        WITHERS_INGRESAR,
        WITHERS_SACAR,
        WITHERS_SACARMAX,
        EMPRESA_BORRAR,
        EMPRESA_DEPOSITAR,
        EMPRESA_SACAR,
        EMPRESA_VENTA,
        EMPRESA_PAGAR_SALARIO,
        EMPRESA_COMPRAR_SERVICIO,
        DEUDAS_PAGAR_DEUDAS,
        DEUDAS_PAGAR_TODADEUDA,
        DEUDAS_PRIMERPAGO,
    }

}