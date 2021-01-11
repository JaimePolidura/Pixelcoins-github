package es.serversurvival.mySQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import es.serversurvival.mySQL.enums.TipoTransaccion;
import es.serversurvival.mySQL.tablasObjetos.*;
import es.serversurvival.mySQL.enums.TipoPosicion;
import es.serversurvival.task.ScoreBoardManager;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import es.serversurvival.util.Funciones;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * 792 -> 600
 */
public final class Transacciones extends MySQL {
    public static final Transacciones INSTANCE = new Transacciones();
    private Transacciones () {}

    public static final int DIAMANTE = 290;
    public static final int CUARZO = 12;
    public static final int LAPISLAZULI = 5;

    public void nuevaTransaccion(String comprador, String vendedor, double cantidad, String objeto, TipoTransaccion tipoTransaccion) {
        String fecha = dateFormater.format(new Date());
        String tipo = tipoTransaccion.toString();

        executeUpdate("INSERT INTO transacciones (fecha, comprador, vendedor, cantidad, objeto, tipo) VALUES ('" + fecha + "','" + comprador + "','" + vendedor + "','" + cantidad + "','" + objeto + "','" + tipo + "')");
    }

    public List<Transaccion> getTransaccionesPagaEmpresa (String jugador) {
        return buildListFromResultSet(executeQuery("SELECT * FROM transacciones WHERE comprador = '"+jugador+"' AND tipo = '"+ TipoTransaccion.EMPRESA_PAGAR_SALARIO.toString()+"'"));
    }

    public void borrarTransaccione(int id) {
        executeUpdate(String.format("DELETE FROM transacciones WHERE id = '%s'", id));
    }

    public void setCompradorVendedor (String jugador, String nuevoJugador) {
        executeUpdate("UPDATE transacciones SET comprador = '"+nuevoJugador+"' WHERE comprador = '"+jugador+"'");
        executeUpdate("UPDATE transacciones SET vendedor = '"+nuevoJugador+"' WHERE vendedor = '"+jugador+"'");
    }

    public List<Transaccion> getTopVentasJugador(String nombreJugador, int limite){
        ResultSet rs = executeQuery(String.format("SELECT * FROM transacciones WHERE vendedor = '%s' AND tipo = '%s' ORDER BY cantidad DESC LIMIT %d", nombreJugador, "TIENDA", limite));

        return buildListFromResultSet(rs);
    }

    public void realizarVenta(String comprador, int id, Player p) {
        Oferta ofertaAComprar = ofertasMySQL.getOferta(id);
        Jugador jugadorComprador = jugadoresMySQL.getJugador(comprador);

        if (jugadorComprador.getPixelcoins() < ofertaAComprar.getPrecio()) {
            p.sendMessage(ChatColor.DARK_RED + "No puedes comprar por encima de tu dinero");
            return;
        }

        int cantidad = ofertaAComprar.getCantidad();
        String vendedor = ofertaAComprar.getJugador();
        String objeto = ofertaAComprar.getObjeto();
        double precio = ofertaAComprar.getPrecio();

        ItemStack itemAComprar = ofertasMySQL.getItemOferta(ofertasMySQL.getOferta(id));
        itemAComprar.setAmount(1);

        if (cantidad == 1) {
            ofertasMySQL.borrarOferta(id);
        } else {
            ofertasMySQL.setCantidad(id, cantidad - 1);
        }
        this.realizarTransferencia(comprador, vendedor, precio, objeto, TipoTransaccion.TIENDA_VENTA, true);

        ItemMeta itemMeta = itemAComprar.getItemMeta();
        List<String> lore = Arrays.asList("Comprado en la tienda");
        itemMeta.setLore(lore);
        itemAComprar.setItemMeta(itemMeta);

        p.getInventory().addItem(itemAComprar);
        p.sendMessage(ChatColor.GOLD + "Has comprado: " + objeto + " , por " + ChatColor.GREEN + formatea.format(precio) + " PC" + ChatColor.GOLD + " .Te quedan: " +
                ChatColor.GREEN + formatea.format(jugadorComprador.getPixelcoins() - precio) + " PC");
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

        Player vendedorP = Bukkit.getPlayerExact(vendedor);
        if (vendedorP != null) {
            vendedorP.sendMessage(ChatColor.GOLD + comprador + " te ha comprado: " + objeto + " por: " + ChatColor.GREEN + formatea.format(precio) + " PC ");
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
            ScoreBoardManager.updateScoreboard(vendedorP);
        }
        ScoreBoardManager.updateScoreboard(p);
    }

    public void realizarTransferencia(String nombrePagador, String nombrePagado, double cantidad, String objeto, TipoTransaccion tipo, boolean editarEstadisticas) {
        if(editarEstadisticas){
            realizarTransferenciaConEstadisticas(nombrePagador, nombrePagado, cantidad, objeto);
        }else{
            realizarTransferenciaSinEstadisticas(nombrePagador, nombrePagado, cantidad, objeto);
        }
        nuevaTransaccion(nombrePagador, nombrePagado, cantidad, objeto, tipo);
    }

    private void realizarTransferenciaSinEstadisticas (String nombrePagador, String nombrePagado, double cantidad, String objeto) {
        Jugador pagador = jugadoresMySQL.getJugador(nombrePagador);
        Jugador pagado = jugadoresMySQL.getJugador(nombrePagado);

        if(pagado == null){
            jugadoresMySQL.nuevoJugador(nombrePagado, cantidad, 0, 0, 0, 0, 0, Bukkit.getPlayer(nombrePagado).getUniqueId().toString());
        }else{
            jugadoresMySQL.setPixelcoin(nombrePagado, pagado.getPixelcoins() + cantidad);
        }
        jugadoresMySQL.setPixelcoin(nombrePagador, pagador.getPixelcoins() - cantidad);
    }

    private void realizarTransferenciaConEstadisticas (String nombrePagador, String nombrePagado, double cantidad, String objeto) {
        Jugador pagador = jugadoresMySQL.getJugador(nombrePagador);
        Jugador pagado = jugadoresMySQL.getJugador(nombrePagado);

        if(pagado == null){
            jugadoresMySQL.nuevoJugador(nombrePagado, cantidad, 0, cantidad, 0, 0, 0, Bukkit.getPlayer(nombrePagado).getUniqueId().toString());
        }else{
            jugadoresMySQL.setEstadisticas(nombrePagado, pagado.getPixelcoins() + cantidad, pagado.getNventas() + 1, pagado.getIngresos() + cantidad, pagado.getGastos());
        }
        jugadoresMySQL.setEstadisticas(nombrePagador, pagador.getPixelcoins() - cantidad, pagador.getNventas(), pagador.getIngresos(), pagador.getGastos() + cantidad);
    }

    public void realizarPagoManual(String nombrePagador, String nombrePagado, double cantidad, Player player, String objeto, TipoTransaccion tipo) {
        Jugador jugador = jugadoresMySQL.getJugador(nombrePagado);

        if(jugador == null){
            player.sendMessage(ChatColor.DARK_RED + "Ese jugador no esta registrado");
            return;
        }

        realizarTransferencia(nombrePagador, nombrePagado, cantidad, objeto, tipo, true);

        player.sendMessage(ChatColor.GOLD + "Has pagado: " + ChatColor.GREEN + formatea.format(cantidad) + " PC " + ChatColor.GOLD + "a " + nombrePagado);

        Player tp = Bukkit.getServer().getPlayer(nombrePagado);
        if(tp != null){
            tp.playSound(tp.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
            tp.sendMessage(ChatColor.GOLD + nombrePagador + " te ha pagado: " + ChatColor.GREEN + "+" + formatea.format(cantidad) + " PC " + ChatColor.AQUA + "(/estadisticas)");
        }else{
            mensajesMySQL.nuevoMensaje("", nombrePagado, nombrePagador + " te ha pagado " + formatea.format(cantidad) + " PC con el comando /pagar");
        }

        ScoreBoardManager.updateScoreboard(player);

        if(tp != null) ScoreBoardManager.updateScoreboard(tp);
    }

    public void ingresarItem(ItemStack itemAIngresar, Player jugadorPlayer, int slot) {
        Jugador jugadorQueIngresaElItem = jugadoresMySQL.getJugador(jugadorPlayer.getName());

        int cantidad = itemAIngresar.getAmount();
        String nombreItem = itemAIngresar.getType().toString();
        double pixelcoinsAnadir = getCantidadARecibirTranIngresarItem(cantidad, nombreItem);

        boolean registrado = jugadorQueIngresaElItem != null;
        double dineroActual = jugadorQueIngresaElItem.getPixelcoins();
        if (registrado) {
            jugadoresMySQL.setPixelcoin(jugadorPlayer.getName(), pixelcoinsAnadir + dineroActual);
        } else {
            jugadoresMySQL.nuevoJugador(jugadorPlayer.getName(), pixelcoinsAnadir, 0, 0, 0, 0, 0, jugadorPlayer.getUniqueId().toString());
        }
        this.nuevaTransaccion(jugadorPlayer.getName(), "", pixelcoinsAnadir, nombreItem, TipoTransaccion.WITHERS_INGRESAR);

        jugadorPlayer.getInventory().clear(slot);

        jugadorPlayer.sendMessage(ChatColor.GOLD + "Se ha a?adido: " + ChatColor.GREEN + formatea.format(pixelcoinsAnadir) + " PC " + ChatColor.GOLD + "Ahora tienes: " + ChatColor.GREEN + formatea.format(pixelcoinsAnadir + dineroActual) + "PC");
        jugadorPlayer.playSound(jugadorPlayer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

        ScoreBoardManager.updateScoreboard(jugadorPlayer);
    }

    private double getCantidadARecibirTranIngresarItem (int cantidadItems, String tipoItem) {
        double pixelcoinsAnadir = 0;

        switch (tipoItem) {
            case "DIAMOND":
                pixelcoinsAnadir = cantidadItems * DIAMANTE;
                break;
            case "DIAMOND_BLOCK":
                pixelcoinsAnadir = cantidadItems * DIAMANTE * 9;
                break;
            case "LAPIS_LAZULI":
                pixelcoinsAnadir = cantidadItems * LAPISLAZULI;
                break;
            case "LAPIS_BLOCK":
                pixelcoinsAnadir = cantidadItems * LAPISLAZULI * 9;
                break;
            case "QUARTZ_BLOCK":
                pixelcoinsAnadir = cantidadItems * CUARZO;
                break;
            default:
                break;
        }

        return pixelcoinsAnadir;
    }

    public double sacarItem(Player jugadorPlayer, String tipoItem) {
        Jugador jugadorSacarItem = jugadoresMySQL.getJugador(jugadorPlayer.getName());
        double pixelcoinsSacadas = 0;

        switch (tipoItem){
            case "DIAMOND":
                pixelcoinsSacadas = sacarItem(jugadorSacarItem, tipoItem, DIAMANTE);
                break;
            case "DIAMOND_BLOCK":
                pixelcoinsSacadas = sacarItem(jugadorSacarItem, tipoItem, DIAMANTE * 9);
                break;
            case "QUARTZ_BLOCK":
                pixelcoinsSacadas = sacarItem(jugadorSacarItem, tipoItem, CUARZO);
                break;
            case "LAPIS_LAZULI":
                pixelcoinsSacadas = sacarItem(jugadorSacarItem, tipoItem, LAPISLAZULI);
                break;
            case "LAPIS_BLOCK":
                pixelcoinsSacadas = sacarItem(jugadorSacarItem, tipoItem, LAPISLAZULI * 9);
                break;
        }

        ScoreBoardManager.updateScoreboard(jugadorPlayer);

        return pixelcoinsSacadas;
    }

    private double sacarItem (Jugador jugador, String material ,int pixelcoinsPorItem) {
        Player jugadorPlayer = Bukkit.getPlayer(jugador.getNombre());

        if(jugador.getPixelcoins() >= pixelcoinsPorItem){
            jugadoresMySQL.setPixelcoin(jugador.getNombre(), jugador.getPixelcoins() - pixelcoinsPorItem);
            jugadorPlayer.getInventory().addItem(new ItemStack(Material.getMaterial(material), 1));

            jugadorPlayer.sendMessage(ChatColor.GOLD + "Has convertido las pixelcoins" + ChatColor.RED + "-" + pixelcoinsPorItem + " PC" + ChatColor.GOLD +
                    "Quedan " + ChatColor.GREEN + formatea.format(jugador.getPixelcoins() - pixelcoinsPorItem) + " PC");
            jugadorPlayer.playSound(jugadorPlayer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

            return pixelcoinsPorItem;
        }else{
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "Necesitas tener minimo " + pixelcoinsPorItem + " pixelcoins para convertirlo");
            jugadorPlayer.playSound(jugadorPlayer.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);

            return 0;
        }
    }

    public void sacarMaxItem(String tipo, Player jugadorPlayer) {
        Jugador jugadorASacar = jugadoresMySQL.getJugador(jugadorPlayer.getName());
        int pixelcoinsJugador = (int) jugadoresMySQL.getJugador(jugadorPlayer.getName()).getPixelcoins();

        if ((tipo.equalsIgnoreCase("DIAMOND") && pixelcoinsJugador < DIAMANTE) || (tipo.equalsIgnoreCase("LAPIS_LAZULI") && pixelcoinsJugador < LAPISLAZULI) ||
                (tipo.equalsIgnoreCase("QUARTZ_BLOCK") && pixelcoinsJugador < CUARZO)) {

            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "No tienes las suficientes pixelcoins");
            jugadorPlayer.playSound(jugadorPlayer.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
            return;
        }

        if (tipo.equalsIgnoreCase("DIAMOND_BLOCK")) {
            sacarMaxItemDiamond(jugadorASacar, jugadorPlayer);
        } else if (tipo.equalsIgnoreCase("LAPIS_LAZULI")) {
            sacarMaxItemLapisLazuli(jugadorASacar, jugadorPlayer);
        } else {
            sacarMaxItemQuartzBlock(jugadorASacar, jugadorPlayer);
        }

        ScoreBoardManager.updateScoreboard(jugadorPlayer);
    }

    private void sacarMaxItemDiamond (Jugador jugador, Player jugadorPlayer) {
        int dineroJugador = (int) jugador.getPixelcoins();

        int convertibles = dineroJugador - (dineroJugador % DIAMANTE);
        int items = (convertibles / DIAMANTE) % 9;
        int bloques = ((convertibles / DIAMANTE) - items) / 9;

        int bloquesAnadidos = 0;
        int[] slotsBloques = Funciones.slotsItem(bloques, 36 - Funciones.getEspaciosOcupados(jugadorPlayer.getInventory()));
        Inventory inventoryJugador = jugadorPlayer.getInventory();

        for (int i = 0; i < slotsBloques.length; i++) {
            bloquesAnadidos = bloquesAnadidos + slotsBloques[i];
            inventoryJugador.addItem(new ItemStack(Material.getMaterial("DIAMOND_BLOCK"), slotsBloques[i]));
        }
        int[] slotsDiamantes = Funciones.slotsItem(items, 36 - Funciones.getEspaciosOcupados(inventoryJugador));
        int diamantesAnadidos = 0;
        for (int i = 0; i < slotsDiamantes.length; i++) {
            diamantesAnadidos = diamantesAnadidos + slotsDiamantes[i];
            inventoryJugador.addItem(new ItemStack(Material.getMaterial("DIAMOND"), slotsDiamantes[i]));
        }

        int coste = (DIAMANTE * bloquesAnadidos * 9) + (DIAMANTE * diamantesAnadidos);
        jugadoresMySQL.setPixelcoin(jugadorPlayer.getName(), dineroJugador - coste);
        nuevaTransaccion(jugadorPlayer.getName(), "", coste, "DIAMOND", TipoTransaccion.WITHERS_SACARMAX);

        jugadorPlayer.sendMessage(ChatColor.GOLD + "Se ha a?adio: " + ChatColor.AQUA + "+" + bloquesAnadidos + " bloques " + "+" + diamantesAnadidos + " diamantes. " + ChatColor.RED + "-" + formatea.format(coste)
                + ChatColor.GOLD + " Quedan: " + ChatColor.GREEN + formatea.format(dineroJugador - coste) + " PC");
        jugadorPlayer.playSound(jugadorPlayer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
    }

    private void sacarMaxItemLapisLazuli (Jugador jugador, Player jugadorPlayer) {
        int dineroJugador = (int) jugador.getPixelcoins();

        int convertibles = dineroJugador - (dineroJugador % LAPISLAZULI);
        int items = (convertibles / LAPISLAZULI) % 9;
        int bloques = ((convertibles / LAPISLAZULI) - items) / 9;

        int bloquesAnadidos = 0;
        int[] slotsBloques = Funciones.slotsItem(bloques, 36 - Funciones.getEspaciosOcupados(jugadorPlayer.getInventory()));
        Inventory inventoryJugador = jugadorPlayer.getInventory();

        for (int i = 0; i < slotsBloques.length; i++) {
            bloquesAnadidos = bloquesAnadidos + slotsBloques[i];
            inventoryJugador.addItem(new ItemStack(Material.getMaterial("LAPIS_BLOCK"), slotsBloques[i]));
        }
        int[] slotsDiamantes = Funciones.slotsItem(items, 36 - Funciones.getEspaciosOcupados(inventoryJugador));
        int diamantesAnadidos = 0;
        for (int i = 0; i < slotsDiamantes.length; i++) {
            diamantesAnadidos = diamantesAnadidos + slotsDiamantes[i];
            inventoryJugador.addItem(new ItemStack(Material.getMaterial("LAPIS_LAZULI"), slotsDiamantes[i]));
        }

        int coste = (LAPISLAZULI * bloquesAnadidos * 9) + (LAPISLAZULI * diamantesAnadidos);
        jugadoresMySQL.setPixelcoin(jugadorPlayer.getName(), dineroJugador - coste);
        nuevaTransaccion(jugadorPlayer.getName(), "", coste, "LAPIS_LAZULI", TipoTransaccion.WITHERS_SACARMAX);

        jugadorPlayer.sendMessage(ChatColor.GOLD + "Se ha a?adio: " + ChatColor.BLUE + "+" + bloquesAnadidos + " bloques " + "+" + diamantesAnadidos + " Lapislazuli. " + ChatColor.RED + "-" + formatea.format(coste)
                + ChatColor.GOLD + " Quedan: " + ChatColor.GREEN + formatea.format(dineroJugador - coste) + " PC");
        jugadorPlayer.playSound(jugadorPlayer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
    }

    private void sacarMaxItemQuartzBlock (Jugador jugador, Player jugadorPlayer) {
        int pixelcoinsJugador = (int) jugador.getPixelcoins();

        int bloques = (pixelcoinsJugador - (pixelcoinsJugador % CUARZO)) / CUARZO;

        int[] slotsBloques = Funciones.slotsItem(bloques, 36 - Funciones.getEspaciosOcupados(jugadorPlayer.getInventory()));
        int bloquesAnadidos = 0;
        Inventory jugadorInventory = jugadorPlayer.getInventory();
        for (int i = 0; i < slotsBloques.length; i++) {
            bloquesAnadidos = bloquesAnadidos + slotsBloques[i];
            jugadorInventory.addItem(new ItemStack(Material.getMaterial("QUARTZ_BLOCK"), slotsBloques[i]));
        }

        int coste = (CUARZO * bloquesAnadidos);
        jugadoresMySQL.setPixelcoin(jugadorPlayer.getName(), pixelcoinsJugador - coste);
        nuevaTransaccion(jugadorPlayer.getName(), "", coste, "QUARTZ_BLOCK", TipoTransaccion.WITHERS_SACARMAX);

        jugadorPlayer.sendMessage(ChatColor.GOLD + "Se ha a?adio: " + ChatColor.GRAY + "+" + bloquesAnadidos + " bloques de cuarzo " + ChatColor.RED + "-" + formatea.format(coste)
                + ChatColor.GOLD + " Quedan: " + ChatColor.GREEN + formatea.format(pixelcoinsJugador - coste) + " PC");
        jugadorPlayer.playSound(jugadorPlayer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
    }

    public void depositarPixelcoinsEmpresa(Player jugadorPlayer, double pixelcoins, String nombreEmpresa) {
        String nombreJugador = jugadorPlayer.getName();
        Jugador jugador = jugadoresMySQL.getJugador(nombreJugador);
        double pixelcoinsJugador = jugador.getPixelcoins();
        Empresa empresaADepositar = empresasMySQL.getEmpresa(nombreEmpresa);
        double pixelcoinsEmpresa = empresaADepositar.getPixelcoins();

        empresasMySQL.setPixelcoins(nombreEmpresa, pixelcoinsEmpresa + pixelcoins);
        jugadoresMySQL.setEstadisticas(nombreJugador, pixelcoinsJugador - pixelcoins, jugador.getNventas(), jugador.getIngresos(), jugador.getGastos());
        nuevaTransaccion(nombreJugador, nombreEmpresa, pixelcoins, "", TipoTransaccion.EMPRESA_DEPOSITAR);

        jugadorPlayer.sendMessage(ChatColor.GOLD + "Has metido " + ChatColor.GREEN + formatea.format(pixelcoins) + " PC" + ChatColor.GOLD
                + " en tu empresa: " + ChatColor.DARK_AQUA + nombreEmpresa + ChatColor.GOLD + " ahora tiene: " + ChatColor.GREEN +
                formatea.format(pixelcoinsEmpresa + pixelcoins) + " PC");
        jugadorPlayer.playSound(jugadorPlayer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
        ScoreBoardManager.updateScoreboard(jugadorPlayer);
    }

    public void sacarPixelcoinsEmpresa(Player jugadorPlayer, double pixelcoins, String nombreEmpresa) {
        String nombreJugador = jugadorPlayer.getName();
        Empresa empresaASacar = empresasMySQL.getEmpresa(nombreEmpresa);
        double pixelcoinsEmpresa = empresaASacar.getPixelcoins();

        Jugador jugadorQueSaca = jugadoresMySQL.getJugador(jugadorPlayer.getName());
        double pixelcoinsJugador = jugadorQueSaca.getPixelcoins();

        empresasMySQL.setPixelcoins(nombreEmpresa, pixelcoinsEmpresa - pixelcoins);
        jugadoresMySQL.setEstadisticas(nombreJugador, pixelcoinsJugador + pixelcoins, jugadorQueSaca.getNventas(), jugadorQueSaca.getIngresos(), jugadorQueSaca.getGastos());
        nuevaTransaccion(nombreEmpresa, nombreJugador, pixelcoins, "", TipoTransaccion.EMPRESA_SACAR);

        jugadorPlayer.sendMessage(ChatColor.GOLD + "Has sacado " + ChatColor.GREEN + formatea.format(pixelcoins) + " PC" + ChatColor.GOLD
                + " de tu empresa: " + ChatColor.DARK_AQUA + nombreEmpresa + ChatColor.GOLD + " ahora tiene: " + ChatColor.GREEN +
                formatea.format(pixelcoinsEmpresa - pixelcoins) + " PC");
        jugadorPlayer.playSound(jugadorPlayer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
        ScoreBoardManager.updateScoreboard(jugadorPlayer);
    }

    public void comprarEmpresa(String vendedor, String comprador, String empresa, double precio, Player p) {
        Jugador jugadorVendedor = jugadoresMySQL.getJugador(vendedor);
        Jugador jugadorComprador = jugadoresMySQL.getJugador(comprador);

        empresasMySQL.setOwner(empresa, comprador);
        jugadoresMySQL.setEstadisticas(vendedor, jugadorVendedor.getPixelcoins() + precio, jugadorVendedor.getNventas() + 1, jugadorVendedor.getIngresos() + precio, jugadorVendedor.getGastos());
        jugadoresMySQL.setEstadisticas(comprador, jugadorComprador.getPixelcoins() - precio, jugadorComprador.getNventas(), jugadorComprador.getIngresos(), jugadorComprador.getGastos() + precio);
    }

    public boolean pagarSalario(String jugador, String empresa, double salario) {
        Empresa empresaAPagarSalario = empresasMySQL.getEmpresa(empresa);
        if (empresaAPagarSalario.getPixelcoins() < salario) {
            return false;
        }

        empresasMySQL.setPixelcoins(empresa, empresaAPagarSalario.getPixelcoins() - salario);
        empresasMySQL.setGastos(empresa, empresaAPagarSalario.getGastos() + salario);

        Jugador empleadoAPagar = jugadoresMySQL.getJugador(jugador);
        jugadoresMySQL.setEstadisticas(jugador, empleadoAPagar.getPixelcoins() + salario, empleadoAPagar.getNventas(), empleadoAPagar.getIngresos() + salario, empleadoAPagar.getGastos());
        nuevaTransaccion(jugador, empresa, salario, "", TipoTransaccion.EMPRESA_PAGAR_SALARIO);
        return true;
    }

    public void comprarServivio(String empresa, double precio, Player p) {
        Empresa empresaAComprar = empresasMySQL.getEmpresa(empresa);
        if (empresaAComprar == null) {
            p.sendMessage(ChatColor.DARK_RED + "Esa empresa no existe");
            return;
        }
        if (empresaAComprar.getOwner().equalsIgnoreCase(p.getName())) {
            p.sendMessage(ChatColor.DARK_RED + "No puedes comprar un servivio de tu propia empresa siendo el propio owner");
            return;
        }

        Jugador comprador = jugadoresMySQL.getJugador(p.getName());
        if (comprador.getPixelcoins() < precio) {
            p.sendMessage(ChatColor.DARK_RED + "No puedes ir por encima de tus posibilidades");
            return;
        }

        jugadoresMySQL.setEstadisticas(comprador.getNombre(), comprador.getPixelcoins() - precio, comprador.getNventas(), comprador.getIngresos(), comprador.getGastos() + precio);
        empresasMySQL.setPixelcoins(empresa, empresaAComprar.getPixelcoins() + precio);
        empresasMySQL.setIngresos(empresa, empresaAComprar.getIngresos() + precio);
        nuevaTransaccion(comprador.getNombre(), empresa, precio, "", TipoTransaccion.EMPRESA_COMPRAR_SERVICIO);

        List<Empleado> empleados = empleadosMySQL.getEmpleadosEmrpesa(empresa);
        empleados.forEach((empl) -> {
            Player tp = p.getServer().getPlayer(empl.getJugador());
            if(tp != null){
                tp.sendMessage(ChatColor.GOLD + comprador.getNombre() + " ha comprado vuestro servicio de la empresa: " + empresa + " por " + ChatColor.GREEN + formatea.format(precio) + " PC");
            }
        });
        Player ownerEmpresa = Bukkit.getPlayer(empresaAComprar.getOwner());
        if(ownerEmpresa != null){
            ownerEmpresa.sendMessage(ChatColor.GOLD + comprador.getNombre() + " ha comprado vuestro servicio de la empresa: " + empresa + " por " + ChatColor.GREEN + formatea.format(precio) + " PC");
        }

        p.sendMessage(ChatColor.GOLD + "Has pagado " + ChatColor.GREEN + precio + " PC " + ChatColor.GOLD + " a la empresa: " + empresa + " por su servicio");
    }

    public void comprarUnidadBolsa(String tipo, String ticker, String nombreValor, String alias, double precioUnidad, int cantidad, Player jugadorPlayer) {
        Jugador comprador = jugadoresMySQL.getJugador(jugadorPlayer.getName());
        double precioTotal = precioUnidad * cantidad;

        jugadoresMySQL.setPixelcoin(jugadorPlayer.getName(), comprador.getPixelcoins() - precioTotal);
        posicionesAbiertasMySQL.nuevaPosicion(jugadorPlayer.getName(), tipo, ticker, cantidad, precioUnidad, TipoPosicion.LARGO);
        nuevaTransaccion(jugadorPlayer.getName(), ticker, precioTotal, tipo +  " " + precioUnidad, TipoTransaccion.BOLSA_COMPRA);

        if(!llamadasApiMySQL.estaReg(ticker)){
            llamadasApiMySQL.nuevaLlamada(ticker, precioUnidad, tipo, nombreValor);
        }

        jugadorPlayer.sendMessage(ChatColor.GOLD + "Has comprado " + formatea.format(cantidad)  + " " + alias + " a " + ChatColor.GREEN + formatea.format(precioUnidad) + " PC" + ChatColor.GOLD + " que es un total de " +
                ChatColor.GREEN + formatea.format(precioTotal) + " PC " + ChatColor.GOLD + " comandos: " + ChatColor.AQUA + "/bolsa vender /bolsa cartera");
        jugadorPlayer.playSound(jugadorPlayer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

        Bukkit.broadcastMessage(ChatColor.GOLD + jugadorPlayer.getName() + " ha comprado " + cantidad + " " + alias +  " de " + nombreValor + " a " + ChatColor.GREEN + precioUnidad + "PC");

    }

    public void venderEnCortoBolsa (Player player, String ticker, String nombreValor, int cantidad, double precioPorAccion) {
        jugadoresMySQL.conectar();

        Jugador jugador = jugadoresMySQL.getJugador(player.getName());

        double dineroJugador = jugador.getPixelcoins();
        double valorTotal = precioPorAccion * cantidad;
        if(valorTotal > dineroJugador){
            player.sendMessage(ChatColor.DARK_RED + "No tienes el dinero suficiente para esa operacion");
            jugadoresMySQL.desconectar();
            return;
        }

        double comision = Funciones.redondeoDecimales(Funciones.reducirPorcentaje(valorTotal, 100 - PosicionesAbiertas.PORCENTAJE_CORTO), 2);
        jugadoresMySQL.setEstadisticas(player.getName(), jugador.getPixelcoins() - comision, jugador.getNventas(), jugador.getIngresos(), jugador.getGastos() + comision);

        posicionesAbiertasMySQL.nuevaPosicion(player.getName(), "ACCIONES", ticker, cantidad, precioPorAccion, TipoPosicion.CORTO);
        nuevaTransaccion(player.getName(), ticker, comision, "ACCIONES" +  " " + precioPorAccion, TipoTransaccion.BOLSA_CORTO_VENTA);

        if(!llamadasApiMySQL.estaReg(ticker)){
            llamadasApiMySQL.nuevaLlamada(ticker, precioPorAccion, "ACCIONES", nombreValor);
        }

        player.sendMessage(ChatColor.GOLD + "Te has puesto corto en " + nombreValor + " en " + cantidad + " cada una a " + ChatColor.GREEN + formatea.format(precioPorAccion) + " PC " + ChatColor.GOLD +  "Para recomprar las acciones: /bolsa comprarcorto <id>. /bolsa cartera");
        player.sendMessage(ChatColor.GOLD + "Ademas se te ha cobrado un 5% del valor total de la venta (" + ChatColor.GREEN  + formatea.format(valorTotal) + " PC"
                + ChatColor.GOLD + ") por lo cual: " + ChatColor.RED + "-" + formatea.format(comision) + " PC");
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

        Bukkit.broadcastMessage(ChatColor.GOLD + player.getName() + " se ha puesto en corto en " + nombreValor);
    }

    public void venderPosicion(PosicionAbierta posicionAVender, int cantidad, Player player) {
        int idPosiconAbierta = posicionAVender.getId();
        double precioPorAccion = llamadasApiMySQL.getLlamadaAPI(posicionAVender.getNombre_activo()).getPrecio();

        String ticker = posicionAVender.getNombre_activo();
        int nAccionesTotlaesEnCartera = posicionAVender.getCantidad();
        double precioApertura = posicionAVender.getPrecio_apertura();
        String fechaApertura = posicionAVender.getFecha_apertura();
        double revalorizacionTotal = cantidad * precioPorAccion;
        double rentabilidad = Funciones.redondeoDecimales(Funciones.diferenciaPorcntual(precioApertura, precioPorAccion), 3);
        String tipoPosicion = posicionAVender.getTipo_activo();

        Jugador vendedor = jugadoresMySQL.getJugador(player.getName());
        double beneficiosPerdidas = revalorizacionTotal - (cantidad * precioApertura);

        if(beneficiosPerdidas >= 0){
            jugadoresMySQL.setEstadisticas(player.getName(), vendedor.getPixelcoins() + revalorizacionTotal, vendedor.getNventas(), vendedor.getIngresos() + beneficiosPerdidas, vendedor.getGastos());
        }else{
            jugadoresMySQL.setEstadisticas(player.getName(), vendedor.getPixelcoins() + revalorizacionTotal, vendedor.getNventas(), vendedor.getIngresos(), vendedor.getGastos() + beneficiosPerdidas);
        }

        if (cantidad == nAccionesTotlaesEnCartera) {
            posicionesAbiertasMySQL.borrarPosicionAbierta(idPosiconAbierta);
        } else {
            posicionesAbiertasMySQL.setCantidad(idPosiconAbierta, nAccionesTotlaesEnCartera - cantidad);
        }

        String nombreValor = llamadasApiMySQL.getLlamadaAPI(ticker).getNombre_activo();
        if(!posicionesAbiertasMySQL.existeTicker(ticker)){
            llamadasApiMySQL.borrarLlamada(ticker);
        }

        posicionesCerradasMySQL.nuevaPosicion(player.getName(), tipoPosicion, ticker, cantidad, precioApertura, fechaApertura, precioPorAccion, nombreValor, TipoPosicion.LARGO.toString());
        nuevaTransaccion(ticker, player.getName(), cantidad * precioPorAccion, "", TipoTransaccion.BOLSA_VENTA);

        if (rentabilidad <= 0) {
            player.sendMessage(ChatColor.GOLD + "Has vendido " + formatea.format(cantidad) + " de " + ticker + " a " + ChatColor.GREEN + formatea.format(precioPorAccion)
                    + " PC/Accion " + ChatColor.GOLD + " cuando la compraste a " + ChatColor.GREEN + formatea.format(precioApertura) + " PC/Unidad " + ChatColor.GOLD + " -> " +
                    ChatColor.RED + formatea.format(rentabilidad) + "% : " + formatea.format(Funciones.redondeoDecimales(beneficiosPerdidas, 3)) + " Perdidas PC " + ChatColor.GOLD + " de " + ChatColor.GREEN + formatea.format(revalorizacionTotal) + " PC");
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 10, 1);

            Bukkit.broadcastMessage(ChatColor.GOLD + player.getName() + " ha alacanzado una rentabilidad del " + ChatColor.RED + formatea.format(Funciones.redondeoDecimales(rentabilidad, 3)) + "% "
                    + ChatColor.GOLD + "de las acciones de " + nombreValor + " (" + ticker + ")");
        } else {
            player.sendMessage(ChatColor.GOLD + "Has vendido " + formatea.format(cantidad) + " de " + ticker + " a " + ChatColor.GREEN + formatea.format(precioPorAccion)
                    + " PC/Accion " + ChatColor.GOLD + " cuando la compraste a " + ChatColor.GREEN + formatea.format(precioApertura) + " PC/Unidad " + ChatColor.GOLD + " -> " +
                    ChatColor.GREEN + formatea.format(rentabilidad) + "% : " + formatea.format(Funciones.redondeoDecimales(beneficiosPerdidas, 3)) + " Beneficios PC " + ChatColor.GOLD + " de " + ChatColor.GREEN + formatea.format(revalorizacionTotal) + " PC");
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

            Bukkit.broadcastMessage(ChatColor.GOLD + player.getName() + " ha alacanzado una rentabilidad del " + ChatColor.GREEN + "+" + formatea.format(Funciones.redondeoDecimales(rentabilidad, 3)) + "% "
                    + ChatColor.GOLD + "de las acciones de " + nombreValor + " (" + ticker + ")");
        }
    }

    public void comprarPosicionCorto (PosicionAbierta posicionAComprar, int cantidad, Player player) {
        int idPosiconAbierta = posicionAComprar.getId();
        double precioPorAccion = llamadasApiMySQL.getLlamadaAPI(posicionAComprar.getNombre_activo()).getPrecio();

        String ticker = posicionAComprar.getNombre_activo();
        int nAccionesTotlaesEnCartera = posicionAComprar.getCantidad();
        double precioApertura = posicionAComprar.getPrecio_apertura();
        String fechaApertura = posicionAComprar.getFecha_apertura();
        double revalorizacionTotal = cantidad * (precioApertura - precioPorAccion);
        double rentabilidad = Funciones.redondeoDecimales(Funciones.diferenciaPorcntual(precioPorAccion, precioApertura), 3);
        String tipoPosicion = posicionAComprar.getTipo_activo();
        Jugador compradorJugador = jugadoresMySQL.getJugador(player.getName());

        double pixelcoinsJugador = compradorJugador.getPixelcoins();
        if(0 > pixelcoinsJugador + revalorizacionTotal){
            jugadoresMySQL.setEstadisticas(player.getName(), pixelcoinsJugador + revalorizacionTotal, compradorJugador.getNventas(), compradorJugador.getIngresos(), compradorJugador.getGastos() + revalorizacionTotal);
        }else{
            jugadoresMySQL.setEstadisticas(player.getName(), pixelcoinsJugador + revalorizacionTotal, compradorJugador.getNventas(), compradorJugador.getIngresos() + revalorizacionTotal, compradorJugador.getGastos());
        }

        if (cantidad == nAccionesTotlaesEnCartera) {
            posicionesAbiertasMySQL.borrarPosicionAbierta(idPosiconAbierta);
        } else {
            posicionesAbiertasMySQL.setCantidad(idPosiconAbierta, nAccionesTotlaesEnCartera - cantidad);
        }

        String nombreValor = llamadasApiMySQL.getLlamadaAPI(ticker).getNombre_activo();
        if(!posicionesAbiertasMySQL.existeTicker(ticker)){
            llamadasApiMySQL.borrarLlamada(ticker);
        }

        posicionesCerradasMySQL.nuevaPosicion(player.getName(), tipoPosicion, ticker, cantidad, precioApertura, fechaApertura, precioPorAccion, nombreValor, TipoPosicion.CORTO.toString(), rentabilidad);
        nuevaTransaccion(ticker, player.getName(), cantidad * precioPorAccion, "", TipoTransaccion.BOLSA_CORTO_COMPRA);

        if (rentabilidad <= 0) {
            player.sendMessage(ChatColor.GOLD + "Has comprado en corto" + formatea.format(cantidad) + " de " + ticker + " a " + ChatColor.GREEN + formatea.format(precioPorAccion)
                    + " PC/Accion " + ChatColor.GOLD + " cuando la vendiste a " + ChatColor.GREEN + formatea.format(precioApertura) + " PC/Unidad " + ChatColor.GOLD + " -> " +
                    ChatColor.RED + formatea.format(rentabilidad) + "% : " + formatea.format(Funciones.redondeoDecimales(revalorizacionTotal, 3)) + " Perdidas PC ");
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 10, 1);
        } else {
            player.sendMessage(ChatColor.GOLD + "Has comprado en corto" + formatea.format(cantidad) + " de " + ticker + " a " + ChatColor.GREEN + formatea.format(precioPorAccion)
                    + " PC/Accion " + ChatColor.GOLD + " cuando la vendiste a " + ChatColor.GREEN + formatea.format(precioApertura) + " PC/Unidad " + ChatColor.GOLD + " -> " +
                    ChatColor.GREEN + formatea.format(rentabilidad) + "% : " + formatea.format(Funciones.redondeoDecimales(revalorizacionTotal, 3)) + " Beneficios PC ");
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
        }

        Bukkit.broadcastMessage(ChatColor.GOLD + player.getName() + " ha alacanzado una rentabilidad del " + ChatColor.GREEN + "+" + formatea.format(Funciones.redondeoDecimales(rentabilidad, 3)) + "% "
                + ChatColor.GOLD + "de las acciones de " + nombreValor + " (" + ticker + "), poniendose en " + ChatColor.BOLD  + "CORTO");

    }

    public void pagaDividendo(String ticker, String nombre, double precioDividendo, int nAcciones) {
        double aPagar = precioDividendo * nAcciones;

        jugadoresMySQL.setPixelcoin(nombre, jugadoresMySQL.getJugador(nombre).getPixelcoins() + aPagar);
        this.nuevaTransaccion(ticker, nombre, aPagar, "", TipoTransaccion.BOLSA_DIVIDENDO);

        mensajesMySQL.nuevoMensaje("",nombre, "Has cobrado " + precioDividendo + " PC en dividendos por parte de la empresa " + ticker);
    }

    public void cambiarNombreJugadorRegistros (String jugadorACambiar, String nuevoNombre) {
        cuentasMySQL.setUsername(jugadorACambiar, nuevoNombre);
        deudasMySQL.setAcredorDeudor(jugadorACambiar, nuevoNombre);
        empleadosMySQL.setEmpleado(jugadorACambiar, nuevoNombre);
        empresasMySQL.setTodosOwner(jugadorACambiar, nuevoNombre);
        jugadoresMySQL.cambiarNombreJugador(jugadorACambiar, nuevoNombre);
        mensajesMySQL.setDestinatario(jugadorACambiar, nuevoNombre);
        ofertasMySQL.setJugador(jugadorACambiar, nuevoNombre);
        posicionesAbiertasMySQL.setJugador(jugadorACambiar, nuevoNombre);
        posicionesCerradasMySQL.setJugador(jugadorACambiar, nuevoNombre);
        setCompradorVendedor(jugadorACambiar, nuevoNombre);

        nuevaTransaccion(nuevoNombre, nuevoNombre, 0, "", TipoTransaccion.BASEDATOS_CAMBIAR_NOMBRE);
    }

    @Override
    protected Transaccion buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return new Transaccion(
                rs.getInt("id"),
                rs.getString("fecha"),
                rs.getString("comprador"),
                rs.getString("vendedor"),
                rs.getInt("cantidad"),
                rs.getString("objeto"),
                rs.getString("tipo")
        );
    }
}
