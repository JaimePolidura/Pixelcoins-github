package es.serversurvival.objetos.mySQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.serversurvival.objetos.menus.Menu;
import es.serversurvival.objetos.menus.OfertasMenu;
import es.serversurvival.objetos.mySQL.tablasObjetos.*;
import es.serversurvival.objetos.task.ScoreboardTaskManager;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


import es.serversurvival.main.Funciones;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

@SuppressWarnings("SpellCheckingInspection")
/**
 * 792 -> 624
 */
public class Transacciones extends MySQL {
    public DecimalFormat formatea = new DecimalFormat("###,###.##");
    private ScoreboardTaskManager sp = new ScoreboardTaskManager();
    private SimpleDateFormat dateFormater = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final int DIAMANTE = 750;
    public static final int CUARZO = 20;
    public static final int LAPISLAZULI = 10;

    public void nuevaTransaccion(String comprador, String vendedor, double cantidad, String objeto, TIPO tipos) {
        Date fechaHoy = new Date();
        String fecha = dateFormater.format(fechaHoy);
        String tipo = tipos.toString();

        executeUpdate("INSERT INTO transacciones (fecha, comprador, vendedor, cantidad, objeto, tipo) VALUES ('" + fecha + "','" + comprador + "','" + vendedor + "','" + cantidad + "','" + objeto + "','" + tipo + "')");
    }

    public void borrarTransaccione(int id) {
        executeUpdate(String.format("DELETE FROM transacciones WHERE id = '%s'", id));
    }

    public List<Transaccion> getTopVentasJugador(String nombreJugador, int limite){
        List<Transaccion> transacciones = new ArrayList<>();
        try{
            ResultSet rs = executeQuery(String.format("SELECT * FROM transacciones WHERE vendedor = '%s' AND tipo = '%s' ORDER BY cantidad DESC LIMIT %d", nombreJugador, "TIENDA", limite));
            while (rs.next()){
                transacciones.add(buildTransaccionByResultset(rs));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return transacciones;
    }

    public void realizarVenta(String comprador, int id, Player p) {
        Jugadores jugadoresMySQL = new Jugadores();
        Ofertas ofertasMySQL = new Ofertas();

        Oferta ofertaAComprar = ofertasMySQL.getOferta(id);
        Jugador jugadorComprador = jugadoresMySQL.getJugador(comprador);

        if (jugadorComprador.getPixelcoin() < ofertaAComprar.getPrecio()) {
            p.sendMessage(ChatColor.DARK_RED + "No puedes comprar por encima de tu dinero");
            return;
        }

        int cantidad = ofertaAComprar.getCantidad();
        String vendedor = ofertaAComprar.getNombre();
        String objeto = ofertaAComprar.getObjeto();
        double precio = ofertaAComprar.getPrecio();

        ItemStack itemAComprar = ofertasMySQL.getItemOferta(id);
        itemAComprar.setAmount(1);

        if (cantidad == 1) {
            ofertasMySQL.borrarOferta(id, vendedor);
        } else {
            ofertasMySQL.setCantidad(id, cantidad - 1);
        }
        this.realizarTransferencia(comprador, vendedor, precio, objeto, TIPO.TIENDA_VENTA, true);

        ((OfertasMenu) Menu.getByPlayer(p)).refresh();

        p.getInventory().addItem(itemAComprar);
        p.sendMessage(ChatColor.GOLD + "Has comprado: " + objeto + " , por " + ChatColor.GREEN + formatea.format(precio) + " PC" + ChatColor.GOLD + " .Te quedan: " +
                ChatColor.GREEN + formatea.format(jugadorComprador.getPixelcoin() - precio) + " PC");
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

        Player vendedorP = Bukkit.getPlayerExact(vendedor);
        if (vendedorP != null) {
            vendedorP.sendMessage(ChatColor.GOLD + comprador + " te ha comprado: " + objeto + " por: " + ChatColor.GREEN + formatea.format(precio) + " PC " + ChatColor.AQUA + "(/estadisticas)");
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
            sp.updateScoreboard(vendedorP);
        }
        sp.updateScoreboard(p);
    }

    public void realizarTransferencia(String nombrePagador, String nombrePagado, double cantidad, String objeto, TIPO tipo, boolean editarEstadisticas) {
        if(editarEstadisticas){
            realizarTransferenciaConEstadisticas(nombrePagador, nombrePagado, cantidad, objeto);
        }else{
            realizarTransferenciaSinEstadisticas(nombrePagador, nombrePagado, cantidad, objeto);
        }
        nuevaTransaccion(nombrePagador, nombrePagado, cantidad, objeto, tipo);
    }

    private void realizarTransferenciaSinEstadisticas (String nombrePagador, String nombrePagado, double cantidad, String objeto) {
        Jugadores jugadoresMySQL = new Jugadores();
        Jugador pagador = jugadoresMySQL.getJugador(nombrePagador);
        Jugador pagado = jugadoresMySQL.getJugador(nombrePagado);

        if(pagado == null){
            jugadoresMySQL.nuevoJugador(nombrePagado, cantidad, 0, 0, 0, 0, 0, 0, 0);
        }else{
            jugadoresMySQL.setPixelcoin(nombrePagado, pagado.getPixelcoin() + cantidad);
        }
        jugadoresMySQL.setPixelcoin(nombrePagador, pagador.getPixelcoin() - cantidad);
    }

    private void realizarTransferenciaConEstadisticas (String nombrePagador, String nombrePagado, double cantidad, String objeto) {
        Jugadores jugadoresMySQL = new Jugadores();
        Jugador pagador = jugadoresMySQL.getJugador(nombrePagador);
        Jugador pagado = jugadoresMySQL.getJugador(nombrePagado);

        if(pagado == null){
            jugadoresMySQL.nuevoJugador(nombrePagado, cantidad, 0, 1, cantidad, 0, cantidad, 0, 0);
        }else{
            jugadoresMySQL.setEstadisticas(nombrePagado, pagado.getPixelcoin() + cantidad, pagado.getNventas() + 1, pagado.getIngresos() + cantidad, pagado.getGastos());
        }
        jugadoresMySQL.setEstadisticas(nombrePagador, pagador.getPixelcoin() - cantidad, pagador.getNventas(), pagador.getIngresos(), pagador.getGastos() + cantidad);
    }

    public void realizarPagoManual(String nombrePagador, String nombrePagado, double cantidad, Player p, String objeto, TIPO tipo) {
        realizarTransferencia(nombrePagador, nombrePagado, cantidad, objeto, tipo, true);

        p.sendMessage(ChatColor.GOLD + "Has pagado: " + ChatColor.GREEN + formatea.format(cantidad) + " PC " + ChatColor.GOLD + "a " + nombrePagado);

        Player tp = Bukkit.getServer().getPlayer(nombrePagado);
        tp.playSound(tp.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
        tp.sendMessage(ChatColor.GOLD + nombrePagador + " te ha pagado: " + ChatColor.GREEN + "+" + formatea.format(cantidad) + " PC " + ChatColor.AQUA + "(/estadisticas)");

        sp.updateScoreboard(p);
        sp.updateScoreboard(tp);
    }

    public void ingresarItem(ItemStack itemAIngresar, Player jugadorPlayer, int slot) {
        Jugadores jugadoresMySQL = new Jugadores();
        Jugador jugadorQueIngresaElItem = jugadoresMySQL.getJugador(jugadorPlayer.getName());

        int cantidad = itemAIngresar.getAmount();
        String nombreItem = itemAIngresar.getType().toString();
        double pixelcoinsAnadir = getCantidadARecibirTranIngresarItem(cantidad, nombreItem);

        boolean registrado = jugadorQueIngresaElItem != null;
        double dineroActual = jugadorQueIngresaElItem.getPixelcoin();
        if (registrado) {
            jugadoresMySQL.setPixelcoin(jugadorPlayer.getName(), pixelcoinsAnadir + dineroActual);
        } else {
            jugadoresMySQL.nuevoJugador(jugadorPlayer.getName(), pixelcoinsAnadir, 0, 0, 0, 0, 0, 0, 0);
        }
        this.nuevaTransaccion(jugadorPlayer.getName(), "", pixelcoinsAnadir, nombreItem, TIPO.WITHERS_INGRESAR);

        jugadorPlayer.getInventory().clear(slot);

        jugadorPlayer.sendMessage(ChatColor.GOLD + "Se ha a?adido: " + ChatColor.GREEN + formatea.format(pixelcoinsAnadir) + " PC " + ChatColor.GOLD + "Ahora tienes: " + ChatColor.GREEN + formatea.format(pixelcoinsAnadir + dineroActual) + "PC");
        jugadorPlayer.playSound(jugadorPlayer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

        sp.updateScoreboard(jugadorPlayer);
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

    public void sacarItem(Player jugadorPlayer, String tipoItem) {
        Jugadores jugadoresMySQL = new Jugadores();

        Jugador jugadorSacarItem = jugadoresMySQL.getJugador(jugadorPlayer.getName());
        if(tipoItem.equalsIgnoreCase("DIAMOND")){
            sacarItemDiamond(jugadorSacarItem, jugadorPlayer);
        }else if(tipoItem.equalsIgnoreCase("QUARTZ_BLOCK")){
            sacarItemQuartzBlock(jugadorSacarItem, jugadorPlayer);
        }else{
            sacarItemLapislazuli(jugadorSacarItem, jugadorPlayer);
        }

        sp.updateScoreboard(jugadorPlayer);
    }

    private void sacarItemDiamond(Jugador jugador, Player jugadorPlayer) {
        Jugadores jugadoresMySQL = new Jugadores();

        if(jugador.getPixelcoin() >= DIAMANTE){
            jugadoresMySQL.setPixelcoin(jugador.getNombre(), jugador.getPixelcoin() - DIAMANTE);
            jugadorPlayer.getInventory().addItem(new ItemStack(Material.DIAMOND, 1));

            jugadorPlayer.sendMessage(ChatColor.GOLD + "Se ha a?adido un diamante. " + ChatColor.RED + "-" + DIAMANTE + " PC" + ChatColor.GOLD +
                    "Quedan " + ChatColor.GREEN + formatea.format(jugador.getPixelcoin() - DIAMANTE) + " PC");
            jugadorPlayer.playSound(jugadorPlayer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
        }else{
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "Necesitas tener minimo " + DIAMANTE + " pixelcoins para convertirlo a diamantes.");
            jugadorPlayer.playSound(jugadorPlayer.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
        }
    }

    private void sacarItemQuartzBlock(Jugador jugador, Player jugadorPlayer) {
        Jugadores jugadoresMySQL = new Jugadores();

        if (jugador.getPixelcoin() >= CUARZO) {
            jugadoresMySQL.setPixelcoin(jugadorPlayer.getName(), jugador.getPixelcoin() - CUARZO);
            jugadorPlayer.getInventory().addItem(new ItemStack(Material.QUARTZ_BLOCK, 1));

            jugadorPlayer.sendMessage(ChatColor.GOLD + "Se ha a?adido un bloque de cuarzo. " + ChatColor.RED + "-" + CUARZO + " PC" +
                    ChatColor.GOLD + "Quedan " + ChatColor.GREEN + formatea.format(jugador.getPixelcoin() - CUARZO) + " PC");
            jugadorPlayer.playSound(jugadorPlayer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
        } else {
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "Necesitas tener minimo " + CUARZO + " pixelcoins para convertirlo a bloques de cuarzo.");
            jugadorPlayer.playSound(jugadorPlayer.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
        }
    }

    private void sacarItemLapislazuli(Jugador jugador, Player jugadorPlayer) {
        Jugadores jugadoresMySQL = new Jugadores();

        if (jugador.getPixelcoin() >= LAPISLAZULI) {
            jugadoresMySQL.setPixelcoin(jugadorPlayer.getName(), jugador.getPixelcoin() - LAPISLAZULI);
            jugadorPlayer.getInventory().addItem(new ItemStack(Material.LAPIS_LAZULI, 1));

            jugadorPlayer.sendMessage(ChatColor.GOLD + "Se ha a?adido lapislazuli. " + ChatColor.RED + "-" + LAPISLAZULI + " PC" +
                    ChatColor.GOLD + "Quedan " + ChatColor.GREEN + formatea.format(jugador.getPixelcoin() - LAPISLAZULI) + " PC");
            jugadorPlayer.playSound(jugadorPlayer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
        } else {
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "Necesitas tener minimo " + LAPISLAZULI + " pixelcoins para convertirlo a lapislazuli .");
            jugadorPlayer.playSound(jugadorPlayer.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
        }
    }

    public void sacarMaxItem(String tipo, Player jugadorPlayer) {
        Jugadores jugadoresMySQL = new Jugadores();

        Jugador jugadorASacar = jugadoresMySQL.getJugador(jugadorPlayer.getName());
        int pixelcoinsJugador = (int) jugadoresMySQL.getDinero(jugadorPlayer.getName());

        if ((tipo.equalsIgnoreCase("DIAMOND") && pixelcoinsJugador < DIAMANTE) || (tipo.equalsIgnoreCase("LAPIS_LAZULI") && pixelcoinsJugador < LAPISLAZULI) ||
                (tipo.equalsIgnoreCase("QUARTZ_BLOCK") && pixelcoinsJugador < CUARZO)) {

            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "No tienes las suficientes pixelcoins");
            jugadorPlayer.playSound(jugadorPlayer.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
            return;
        }

        if (tipo.equalsIgnoreCase("DIAMOND")) {
            sacarMaxItemDiamond(jugadorASacar, jugadorPlayer);
        } else if (tipo.equalsIgnoreCase("LAPIS_LAZULI")) {
            sacarMaxItemLapisLazuli(jugadorASacar, jugadorPlayer);
        } else {
            sacarMaxItemQuartzBlock(jugadorASacar, jugadorPlayer);
        }

        sp.updateScoreboard(jugadorPlayer);
    }

    private void sacarMaxItemDiamond (Jugador jugador, Player jugadorPlayer) {
        Jugadores jugadoresMySQL = new Jugadores();
        int dineroJugador = (int) jugador.getPixelcoin();

        int convertibles = dineroJugador - (dineroJugador % DIAMANTE);
        int items = (convertibles / DIAMANTE) % 9;
        int bloques = ((convertibles / DIAMANTE) - items) / 9;

        int bloquesAnadidos = 0;
        int[] slotsBloques = Funciones.slotsItem(bloques, Funciones.espaciosLibres(jugadorPlayer.getInventory()));
        Inventory inventoryJugador = jugadorPlayer.getInventory();

        for (int i = 0; i < slotsBloques.length; i++) {
            bloquesAnadidos = bloquesAnadidos + slotsBloques[i];
            inventoryJugador.addItem(new ItemStack(Material.getMaterial("DIAMOND_BLOCK"), slotsBloques[i]));
        }
        int[] slotsDiamantes = Funciones.slotsItem(items, Funciones.espaciosLibres(inventoryJugador));
        int diamantesAnadidos = 0;
        for (int i = 0; i < slotsDiamantes.length; i++) {
            diamantesAnadidos = diamantesAnadidos + slotsDiamantes[i];
            inventoryJugador.addItem(new ItemStack(Material.getMaterial("DIAMOND"), slotsDiamantes[i]));
        }

        int coste = (DIAMANTE * bloquesAnadidos * 9) + (DIAMANTE * diamantesAnadidos);
        jugadoresMySQL.setPixelcoin(jugadorPlayer.getName(), dineroJugador - coste);
        nuevaTransaccion(jugadorPlayer.getName(), "", coste, "DIAMOND", TIPO.WITHERS_SACARMAX);

        jugadorPlayer.sendMessage(ChatColor.GOLD + "Se ha a?adio: " + ChatColor.AQUA + "+" + bloquesAnadidos + " bloques " + "+" + diamantesAnadidos + " diamantes. " + ChatColor.RED + "-" + formatea.format(coste)
                + ChatColor.GOLD + " Quedan: " + ChatColor.GREEN + formatea.format(dineroJugador - coste) + " PC");
        jugadorPlayer.playSound(jugadorPlayer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
    }

    private void sacarMaxItemLapisLazuli (Jugador jugador, Player jugadorPlayer) {
        Jugadores jugadoresMySQL = new Jugadores();
        int dineroJugador = (int) jugador.getPixelcoin();

        int convertibles = dineroJugador - (dineroJugador % LAPISLAZULI);
        int items = (convertibles / LAPISLAZULI) % 9;
        int bloques = ((convertibles / LAPISLAZULI) - items) / 9;

        int bloquesAnadidos = 0;
        int[] slotsBloques = Funciones.slotsItem(bloques, Funciones.espaciosLibres(jugadorPlayer.getInventory()));
        Inventory inventoryJugador = jugadorPlayer.getInventory();

        for (int i = 0; i < slotsBloques.length; i++) {
            bloquesAnadidos = bloquesAnadidos + slotsBloques[i];
            inventoryJugador.addItem(new ItemStack(Material.getMaterial("LAPIS_BLOCK"), slotsBloques[i]));
        }
        int[] slotsDiamantes = Funciones.slotsItem(items, Funciones.espaciosLibres(inventoryJugador));
        int diamantesAnadidos = 0;
        for (int i = 0; i < slotsDiamantes.length; i++) {
            diamantesAnadidos = diamantesAnadidos + slotsDiamantes[i];
            inventoryJugador.addItem(new ItemStack(Material.getMaterial("LAPIS_LAZULI"), slotsDiamantes[i]));
        }

        int coste = (LAPISLAZULI * bloquesAnadidos * 9) + (LAPISLAZULI * diamantesAnadidos);
        jugadoresMySQL.setPixelcoin(jugadorPlayer.getName(), dineroJugador - coste);
        nuevaTransaccion(jugadorPlayer.getName(), "", coste, "LAPIS_LAZULI", TIPO.WITHERS_SACARMAX);

        jugadorPlayer.sendMessage(ChatColor.GOLD + "Se ha a?adio: " + ChatColor.BLUE + "+" + bloquesAnadidos + " bloques " + "+" + diamantesAnadidos + " Lapislazuli. " + ChatColor.RED + "-" + formatea.format(coste)
                + ChatColor.GOLD + " Quedan: " + ChatColor.GREEN + formatea.format(dineroJugador - coste) + " PC");
        jugadorPlayer.playSound(jugadorPlayer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
    }

    private void sacarMaxItemQuartzBlock (Jugador jugador, Player jugadorPlayer) {
        Jugadores jugadoresMySQL = new Jugadores();
        int pixelcoinsJugador = (int) jugador.getPixelcoin();

        int bloques = (pixelcoinsJugador - (pixelcoinsJugador % CUARZO)) / CUARZO;

        int[] slotsBloques = Funciones.slotsItem(bloques, Funciones.espaciosLibres(jugadorPlayer.getInventory()));
        int bloquesAnadidos = 0;
        Inventory jugadorInventory = jugadorPlayer.getInventory();
        for (int i = 0; i < slotsBloques.length; i++) {
            bloquesAnadidos = bloquesAnadidos + slotsBloques[i];
            jugadorInventory.addItem(new ItemStack(Material.getMaterial("QUARTZ_BLOCK"), slotsBloques[i]));
        }

        int coste = (CUARZO * bloquesAnadidos);
        jugadoresMySQL.setPixelcoin(jugadorPlayer.getName(), pixelcoinsJugador - coste);
        nuevaTransaccion(jugadorPlayer.getName(), "", coste, "QUARTZ_BLOCK", TIPO.WITHERS_SACARMAX);

        jugadorPlayer.sendMessage(ChatColor.GOLD + "Se ha a?adio: " + ChatColor.GRAY + "+" + bloquesAnadidos + " bloques de cuarzo " + ChatColor.RED + "-" + formatea.format(coste)
                + ChatColor.GOLD + " Quedan: " + ChatColor.GREEN + formatea.format(pixelcoinsJugador - coste) + " PC");
        jugadorPlayer.playSound(jugadorPlayer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
    }

    public void depositarPixelcoinsEmpresa(Player jugadorPlayer, double pixelcoins, String nombreEmpresa) {
        Jugadores jugadoresMySQL = new Jugadores();
        Empresas empresasMySQL = new Empresas();

        String nombreJugador = jugadorPlayer.getName();
        double pixelcoinsJugador = jugadoresMySQL.getDinero(nombreJugador);
        Empresa empresaADepositar = empresasMySQL.getEmpresa(nombreEmpresa);
        double pixelcoinsEmpresa = empresaADepositar.getPixelcoins();

        empresasMySQL.setPixelcoins(nombreEmpresa, pixelcoinsEmpresa + pixelcoins);
        jugadoresMySQL.setEstadisticas(nombreJugador, pixelcoinsJugador - pixelcoins, jugadoresMySQL.getNventas(nombreJugador), jugadoresMySQL.getIngresos(nombreJugador), jugadoresMySQL.getGastos(nombreJugador));
        nuevaTransaccion(nombreJugador, nombreEmpresa, pixelcoins, "", TIPO.EMPRESA_DEPOSITAR);

        jugadorPlayer.sendMessage(ChatColor.GOLD + "Has metido " + ChatColor.GREEN + formatea.format(pixelcoins) + " PC" + ChatColor.GOLD
                + " en tu empresa: " + ChatColor.DARK_AQUA + nombreEmpresa + ChatColor.GOLD + " ahora tiene: " + ChatColor.GREEN +
                formatea.format(pixelcoinsEmpresa + pixelcoins) + " PC");
        jugadorPlayer.playSound(jugadorPlayer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
        sp.updateScoreboard(jugadorPlayer);
    }

    public void sacarPixelcoinsEmpresa(Player jugadorPlayer, double pixelcoins, String nombreEmpresa) {
        Empresas empresasMySQL = new Empresas();
        Jugadores jugadoresMySQL = new Jugadores();
        String nombreJugador = jugadorPlayer.getName();
        Empresa empresaASacar = empresasMySQL.getEmpresa(nombreEmpresa);
        double pixelcoinsEmpresa = empresaASacar.getPixelcoins();

        Jugador jugadorQueSaca = jugadoresMySQL.getJugador(jugadorPlayer.getName());
        double pixelcoinsJugador = jugadorQueSaca.getPixelcoin();

        empresasMySQL.setPixelcoins(nombreEmpresa, pixelcoinsEmpresa - pixelcoins);
        jugadoresMySQL.setEstadisticas(nombreJugador, pixelcoinsJugador + pixelcoins, jugadorQueSaca.getNventas(), jugadorQueSaca.getIngresos(), jugadorQueSaca.getGastos());
        nuevaTransaccion(nombreEmpresa, nombreJugador, pixelcoins, "", TIPO.EMPRESA_SACAR);

        jugadorPlayer.sendMessage(ChatColor.GOLD + "Has sacado " + ChatColor.GREEN + formatea.format(pixelcoins) + " PC" + ChatColor.GOLD
                + " de tu empresa: " + ChatColor.DARK_AQUA + nombreEmpresa + ChatColor.GOLD + " ahora tiene: " + ChatColor.GREEN +
                formatea.format(pixelcoinsEmpresa - pixelcoins) + " PC");
        jugadorPlayer.playSound(jugadorPlayer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
        sp.updateScoreboard(jugadorPlayer);
    }

    public void comprarEmpresa(String vendedor, String comprador, String empresa, double precio, Player p) {
        Empresas empresasMySQL = new Empresas();
        Jugadores jugadoresMySQL = new Jugadores();

        Jugador jugadorVendedor = jugadoresMySQL.getJugador(vendedor);
        Jugador jugadorComprador = jugadoresMySQL.getJugador(comprador);

        empresasMySQL.setOwner(empresa, comprador);
        jugadoresMySQL.setEstadisticas(vendedor, jugadorVendedor.getPixelcoin() + precio, jugadorVendedor.getNventas() + 1, jugadorVendedor.getIngresos() + precio, jugadorVendedor.getGastos());
        jugadoresMySQL.setEstadisticas(comprador, jugadorComprador.getPixelcoin() - precio, jugadorComprador.getNventas(), jugadorComprador.getIngresos(), jugadorComprador.getGastos() + precio);
    }

    public boolean pagarSalario(String jugador, String empresa, double salario) {
        Empresas empresasMySQL = new Empresas();
        Jugadores jugadoresMySQL = new Jugadores();

        Empresa empresaAPagarSalario = empresasMySQL.getEmpresa(empresa);
        if (empresaAPagarSalario.getPixelcoins() < salario) {
            return false;
        }

        empresasMySQL.setPixelcoins(empresa, empresaAPagarSalario.getPixelcoins() - salario);
        empresasMySQL.setGastos(empresa, empresaAPagarSalario.getGastos() + salario);

        Jugador empleadoAPagar = jugadoresMySQL.getJugador(jugador);
        jugadoresMySQL.setEstadisticas(jugador, empleadoAPagar.getPixelcoin() + salario, empleadoAPagar.getNventas(), empleadoAPagar.getIngresos() + salario, empleadoAPagar.getGastos());
        nuevaTransaccion(jugador, empresa, salario, "", TIPO.EMPRESA_PAGAR_SALARIO);
        return true;
    }

    public void comprarServivio(String empresa, double precio, Player p) {
        Empresas empresasMySQL = new Empresas();
        Jugadores jugadoresMySQL = new Jugadores();
        Empleados empleadosMySQL = new Empleados();
        Mensajes mensajesMySQL = new Mensajes();

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
        if (comprador.getPixelcoin() < precio) {
            p.sendMessage(ChatColor.DARK_RED + "No puedes ir por encima de tus posibilidades");
            return;
        }

        jugadoresMySQL.setEstadisticas(comprador.getNombre(), comprador.getPixelcoin() - precio, comprador.getNventas(), comprador.getIngresos(), comprador.getGastos() + precio);
        empresasMySQL.setPixelcoins(empresa, empresaAComprar.getPixelcoins() + precio);
        empresasMySQL.setIngresos(empresa, empresaAComprar.getIngresos() + precio);
        nuevaTransaccion(comprador.getNombre(), empresa, precio, "", TIPO.EMPRESA_COMPRAR_SERVICIO);

        List<Empleado> empleados = empleadosMySQL.getEmpleadosEmrpesa(empresa);
        empleados.forEach((empl) -> {
            Player tp = p.getServer().getPlayer(empl.getEmpleado());
            if(tp != null){
                tp.sendMessage(ChatColor.GOLD + comprador.getNombre() + " ha comprado vuestro servicio de la empresa: " + empresa + " por " + ChatColor.GREEN + formatea.format(precio) + " PC");
            }
        });
        Player ownerEmpresa = Bukkit.getPlayer(empresaAComprar.getOwner());
        if(ownerEmpresa != null){
            ownerEmpresa.sendMessage(ChatColor.GOLD + comprador.getNombre() + " ha comprado vuestro servicio de la empresa: " + empresa + " por " + ChatColor.GREEN + formatea.format(precio) + " PC");
        }

        p.sendMessage(ChatColor.GOLD + "Has pagado " + ChatColor.GREEN + precio + " PC " + ChatColor.GOLD + " a la empresa: " + empresa + " por su servicio");
        mensajesMySQL.nuevoMensaje(empresaAComprar.getOwner(), " el jugador: " + comprador + " ha comprado un servicio de tu empresa: " + empresa + " por " + precio + " PC");
    }

    public void comprarUnidadBolsa(String tipo, String ticker, String alias, double precioUnidad, int cantidad, Player jugadorPlayer) {
        PosicionesAbiertas posicionesAbiertasMySQL = new PosicionesAbiertas();
        LlamadasApi llamadasApiMySQL = new LlamadasApi();
        Jugadores jugadoresMySQL = new Jugadores();

        Jugador comprador = jugadoresMySQL.getJugador(jugadorPlayer.getName());
        double precioTotal = precioUnidad * cantidad;

        jugadoresMySQL.setPixelcoin(jugadorPlayer.getName(), comprador.getPixelcoin() - precioTotal);
        posicionesAbiertasMySQL.nuevaPosicion(jugadorPlayer.getName(), tipo, ticker, cantidad, precioUnidad);
        nuevaTransaccion(jugadorPlayer.getName(), ticker, precioTotal, tipo +  " " + precioUnidad, TIPO.BOLSA_COMPRA);

        if(llamadasApiMySQL.estaReg(ticker)){
            llamadasApiMySQL.setPrecio(ticker, precioUnidad);
        }else{
            llamadasApiMySQL.nuevaLlamada(ticker, precioTotal, PosicionesAbiertas.TIPOS.ACCIONES.toString());
        }

        if (jugadorPlayer != null) {
            jugadorPlayer.sendMessage(ChatColor.GOLD + "Has comprado " + formatea.format(cantidad)  + " " + alias + " a " + ChatColor.GREEN + formatea.format(precioUnidad) + " PC" + ChatColor.GOLD + " que es un total de " +
                    ChatColor.GREEN + formatea.format(precioTotal) + " PC " + ChatColor.GOLD + " comandos: " + ChatColor.AQUA + "/bolsa vender /bolsa cartera");
            jugadorPlayer.playSound(jugadorPlayer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

            Bukkit.broadcastMessage(ChatColor.GOLD + jugadorPlayer.getName() + " ha comprado " + cantidad + " " + alias +  " de " + ticker + " a " + ChatColor.GREEN + precioUnidad + "PC");
        }
    }

    public void venderPosicion(double precioPorAccion, int cantidad, int idPosiconAbierta, Player player) {
        PosicionesCerradas posicionesCerradasMySQL = new PosicionesCerradas();
        PosicionesAbiertas posicionesAbiertasMySQL = new PosicionesAbiertas();
        LlamadasApi llamadasApiMySQL = new LlamadasApi();
        Jugadores jugadoresMySQL = new Jugadores();

        PosicionAbierta posicionAVender = posicionesAbiertasMySQL.getPosicionAbierta(idPosiconAbierta);

        String ticker = posicionAVender.getNombre();
        int nAccionesTotlaesEnCartera = posicionAVender.getCantidad();
        double precioApertura = posicionAVender.getPrecioApertura();
        String fechaApertura = posicionAVender.getFechaApertura();
        double revalorizacionTotal = cantidad * precioPorAccion;
        double rentabilidad = Funciones.redondeoDecimales(Funciones.diferenciaPorcntual(precioApertura, precioPorAccion), 3);
        String tipoPosicion = posicionAVender.getTipo();

        jugadoresMySQL.setPixelcoin(player.getName(), jugadoresMySQL.getDinero(player.getName()) + revalorizacionTotal);
        if (cantidad == nAccionesTotlaesEnCartera) {
            posicionesAbiertasMySQL.borrarPosicionAbierta(idPosiconAbierta);
        } else {
            posicionesAbiertasMySQL.setCantidad(idPosiconAbierta, nAccionesTotlaesEnCartera - cantidad);
        }
        posicionesCerradasMySQL.nuevaPosicion(player.getName(), tipoPosicion, ticker, cantidad, precioApertura, fechaApertura, precioPorAccion);
        nuevaTransaccion(ticker, player.getName(), cantidad * precioPorAccion, "", TIPO.BOLSA_VENTA);

        if(!posicionesAbiertasMySQL.existeTicker(ticker)){
            llamadasApiMySQL.borrarLlamada(ticker);
        }

        if (player != null) {
            if (rentabilidad <= 0) {
                player.sendMessage(ChatColor.GOLD + "Has vendido " + formatea.format(cantidad) + " de " + ticker + " a " + ChatColor.GREEN + formatea.format(precioPorAccion)
                        + " PC/Accion " + ChatColor.GOLD + " cuando la compraste a " + ChatColor.GREEN + formatea.format(precioApertura) + " PC/Unidad " + ChatColor.GOLD + " -> " +
                        ChatColor.RED + formatea.format(rentabilidad) + "% : " + formatea.format(Funciones.redondeoDecimales(revalorizacionTotal - (cantidad * precioApertura), 3)) + " Perdidas PC " + ChatColor.GOLD + " de " + ChatColor.GREEN + formatea.format(revalorizacionTotal) + " PC");
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 10, 1);
                Bukkit.broadcastMessage(ChatColor.GOLD + player.getName() + " ha alacanzado una rentabilidad del " + ChatColor.RED + formatea.format(Funciones.redondeoDecimales(rentabilidad, 3)) + "% "
                        + ChatColor.GOLD + "de las acciones de " + ticker);
            } else {
                player.sendMessage(ChatColor.GOLD + "Has vendido " + formatea.format(cantidad) + " de " + ticker + " a " + ChatColor.GREEN + formatea.format(precioPorAccion)
                        + " PC/Accion " + ChatColor.GOLD + " cuando la compraste a " + ChatColor.GREEN + formatea.format(precioApertura) + " PC/Unidad " + ChatColor.GOLD + " -> " +
                        ChatColor.GREEN + formatea.format(rentabilidad) + "% : " + formatea.format(Funciones.redondeoDecimales(revalorizacionTotal - (cantidad * precioApertura), 3)) + " Beneficios PC " + ChatColor.GOLD + " de " + ChatColor.GREEN + formatea.format(revalorizacionTotal) + " PC");
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
                Bukkit.broadcastMessage(ChatColor.GOLD + player.getName() + " ha alacanzado una rentabilidad del " + ChatColor.GREEN + "+" + formatea.format(Funciones.redondeoDecimales(rentabilidad, 3)) + "% "
                        + ChatColor.GOLD + "de las acciones de " + ticker);
            }
        }
    }

    public void pagaDividendo(String ticker, String nombre, double precioDividendo, int nAcciones) {
        Jugadores j = new Jugadores();

        double aPagar = precioDividendo * nAcciones;

        j.setPixelcoin(nombre, j.getDinero(nombre) + aPagar);
        this.nuevaTransaccion(ticker, nombre, aPagar, "", TIPO.BOLSA_DIVIDENDO);

        Mensajes men = new Mensajes();
        men.nuevoMensaje(nombre, "Has cobrado " + precioDividendo + " PC en dividendos por parte de la empresa " + ticker);
    }

    private Transaccion buildTransaccionByResultset (ResultSet rs) throws SQLException {
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

    /**
     * TIPOS DE TRANSACCIONES
     */
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
        BOLSA_COMPRA,
        BOLSA_VENTA,
        BOLSA_DIVIDENDO,
        WEB_TRANSFERENCIA,
        WEB_COMPRAACCION,
        WEB_VENTA_ACCION,
        WEB_PAGAR_DEUDA,
        WEB_BORRAR_EMPRESA,
        WEB_CREAR_EMPRESA
    }
}