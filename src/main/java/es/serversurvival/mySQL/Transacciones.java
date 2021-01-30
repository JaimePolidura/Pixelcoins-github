package es.serversurvival.mySQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import es.serversurvival.mySQL.enums.CambioPixelcoins;
import es.serversurvival.mySQL.enums.TipoActivo;
import es.serversurvival.mySQL.enums.TipoTransaccion;
import es.serversurvival.mySQL.tablasObjetos.*;
import es.serversurvival.mySQL.enums.TipoPosicion;
import es.serversurvival.task.ScoreBoardManager;
import es.serversurvival.util.MinecraftUtils;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import static es.serversurvival.mySQL.enums.CambioPixelcoins.*;
import static es.serversurvival.util.Funciones.*;

/**
 * 792 -> 600 -> 497
 */
public final class Transacciones extends MySQL {
    public static final Transacciones INSTANCE = new Transacciones();
    private Transacciones () {}

    public void nuevaTransaccion(String comprador, String vendedor, double cantidad, String objeto, TipoTransaccion tipoTransaccion) {
        String fecha = dateFormater.format(new Date());

        executeUpdate("INSERT INTO transacciones (fecha, comprador, vendedor, cantidad, objeto, tipo) VALUES ('" + fecha + "','" + comprador + "','" + vendedor + "','" + cantidad + "','" + objeto + "','" + tipoTransaccion.toString() + "')");
    }

    public List<Transaccion> getTransaccionesPagaEmpresa (String jugador) {
        return buildListFromQuery("SELECT * FROM transacciones WHERE comprador = '"+jugador+"' AND tipo = '"+ TipoTransaccion.EMPRESA_PAGAR_SALARIO.toString()+"'");
    }

    public void setCompradorVendedor (String jugador, String nuevoJugador) {
        executeUpdate("UPDATE transacciones SET comprador = '"+nuevoJugador+"' WHERE comprador = '"+jugador+"'");
        executeUpdate("UPDATE transacciones SET vendedor = '"+nuevoJugador+"' WHERE vendedor = '"+jugador+"'");
    }

    public void realizarVenta(String comprador, int id, Player player) {
        Oferta ofertaAComprar = ofertasMySQL.getOferta(id);
        Jugador jugadorComprador = jugadoresMySQL.getJugador(comprador);

        if (jugadorComprador.getPixelcoins() < ofertaAComprar.getPrecio()) {
            player.sendMessage(ChatColor.DARK_RED + "No puedes comprar por encima de tu dinero");
            return;
        }

        int cantidad = ofertaAComprar.getCantidad();
        String vendedor = ofertaAComprar.getJugador();
        String objeto = ofertaAComprar.getObjeto();
        double precio = ofertaAComprar.getPrecio();

        ItemStack itemAComprar = ofertasMySQL.getItemOferta(ofertasMySQL.getOferta(id));
        itemAComprar.setAmount(1);

        if (cantidad == 1)
            ofertasMySQL.borrarOferta(id);
         else
            ofertasMySQL.setCantidad(id, cantidad - 1);

        realizarTransferenciaConEstadisticas(comprador, vendedor, precio, objeto, TipoTransaccion.TIENDA_VENTA);

        MinecraftUtils.setLore(itemAComprar, Arrays.asList("Comprado en la tienda"));
        player.getInventory().addItem(itemAComprar);

        enviarMensajeYSonido(player, ChatColor.GOLD + "Has comprado: " + objeto + " , por " + ChatColor.GREEN + formatea.format(precio) + " PC" + ChatColor.GOLD + " .Te quedan: " +
                ChatColor.GREEN + formatea.format(jugadorComprador.getPixelcoins() - precio) + " PC", Sound.ENTITY_PLAYER_LEVELUP);

        enviarMensajeYSonidoSiOnline(vendedor, ChatColor.GOLD + comprador + " te ha comprado: " + objeto + " por: " + ChatColor.GREEN + formatea.format(precio) + " PC ", Sound.ENTITY_PLAYER_LEVELUP);

        ScoreBoardManager.getInstance().updateScoreboard(player, Bukkit.getPlayer(vendedor));
    }

    public void realizarTransferencia (String nombrePagador, String nombrePagado, double cantidad, String objeto, TipoTransaccion tipo) {
        Jugador pagador = jugadoresMySQL.getJugador(nombrePagador);
        Jugador pagado = jugadoresMySQL.getJugador(nombrePagado);

        jugadoresMySQL.setPixelcoin(nombrePagado, pagado.getPixelcoins() + cantidad);
        jugadoresMySQL.setPixelcoin(nombrePagador, pagador.getPixelcoins() - cantidad);
        nuevaTransaccion(nombrePagador, nombrePagado, cantidad, objeto, tipo);
    }

    public void realizarTransferenciaConEstadisticas (String nombrePagador, String nombrePagado, double cantidad, String objeto, TipoTransaccion tipo) {
        Jugador pagador = jugadoresMySQL.getJugador(nombrePagador);
        Jugador pagado = jugadoresMySQL.getJugador(nombrePagado);

        jugadoresMySQL.setEstadisticas(nombrePagado, pagado.getPixelcoins() + cantidad, pagado.getNventas() + 1, pagado.getIngresos() + cantidad, pagado.getGastos());
        jugadoresMySQL.setEstadisticas(nombrePagador, pagador.getPixelcoins() - cantidad, pagador.getNventas(), pagador.getIngresos(), pagador.getGastos() + cantidad);
        nuevaTransaccion(nombrePagador, nombrePagado, cantidad, objeto, tipo);
    }

    public void realizarPagoManual(String nombrePagador, String nombrePagado, double cantidad, Player player, String objeto) {
        realizarTransferenciaConEstadisticas(nombrePagador, nombrePagado, cantidad, objeto, TipoTransaccion.JUGADOR_PAGO_MANUAL);

        player.sendMessage(ChatColor.GOLD + "Has pagado: " + ChatColor.GREEN + formatea.format(cantidad) + " PC " + ChatColor.GOLD + "a " + nombrePagado);

        String mensajeSiEstaOnline = ChatColor.GOLD + nombrePagador + " te ha pagado: " + ChatColor.GREEN + "+" + formatea.format(cantidad) + " PC " + ChatColor.AQUA + "(/estadisticas)";
        String mensajeSiEstaOffline = nombrePagador + " te ha pagado " + formatea.format(cantidad) + " PC con el comando /pagar";
        enviarMensaje(nombrePagado, mensajeSiEstaOnline, mensajeSiEstaOnline, Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

        ScoreBoardManager.getInstance().updateScoreboard(player, Bukkit.getPlayer(nombrePagado));
    }

    public void ingresarItem(ItemStack itemAIngresar, Player player) {
        Jugador jugadorQueIngresaElItem = jugadoresMySQL.getJugador(player.getName());

        int cantidad = itemAIngresar.getAmount();
        String nombreItem = itemAIngresar.getType().toString();
        double pixelcoinsAnadir = getCambioTotal(nombreItem, cantidad);
        double dineroActual = jugadorQueIngresaElItem.getPixelcoins();

        jugadoresMySQL.setPixelcoin(player.getName(), pixelcoinsAnadir + dineroActual);
        nuevaTransaccion(player.getName(), "", pixelcoinsAnadir, nombreItem, TipoTransaccion.WITHERS_INGRESAR);

        player.getInventory().clear(player.getInventory().getHeldItemSlot());

        enviarMensajeYSonido(player, ChatColor.GOLD + "Se ha a?adido: " + ChatColor.GREEN + formatea.format(pixelcoinsAnadir) + " PC " + ChatColor.GOLD + "Ahora tienes: " +
                ChatColor.GREEN + formatea.format(pixelcoinsAnadir + dineroActual) + "PC", Sound.ENTITY_PLAYER_LEVELUP);

        ScoreBoardManager.getInstance().updateScoreboard(player);
    }

    public double sacarObjeto (Jugador jugador, String material, int pixelcoinsPorItem) {
        Player player= Bukkit.getPlayer(jugador.getNombre());
        MySQL.conectar();

        if(jugador.getPixelcoins() >= pixelcoinsPorItem){
            jugadoresMySQL.setPixelcoin(jugador.getNombre(), jugador.getPixelcoins() - pixelcoinsPorItem);
            player.getInventory().addItem(new ItemStack(Material.getMaterial(material), 1));

            enviarMensajeYSonido(player, ChatColor.GOLD + "Has convertido las pixelcoins" + ChatColor.RED + "-" + pixelcoinsPorItem + " PC " + ChatColor.GOLD +
                    "Quedan " + ChatColor.GREEN + formatea.format(jugador.getPixelcoins() - pixelcoinsPorItem) + " PC", Sound.ENTITY_PLAYER_LEVELUP);

            return pixelcoinsPorItem;
        }else{
            enviarMensajeYSonido(player, ChatColor.DARK_RED + "Necesitas tener minimo " + pixelcoinsPorItem + " pixelcoins para convertirlo", Sound.ENTITY_VILLAGER_NO);

            return 0;
        }
    }

    public void sacarMaxItem(String tipo, Player player) {
        Jugador jugadorASacar = jugadoresMySQL.getJugador(player.getName());
        int pixelcoinsJugador = (int) jugadoresMySQL.getJugador(player.getName()).getPixelcoins();

        if (!suficientesPixelcoins(tipo, 1, pixelcoinsJugador)) {
            enviarMensajeYSonido(player, ChatColor.DARK_RED + "No tienes las suficientes pixelcoins", Sound.ENTITY_VILLAGER_NO);
            return;
        }

        CambioPixelcoins.sacarMaxItem(tipo, jugadorASacar, player);
        ScoreBoardManager.getInstance().updateScoreboard(player);
    }

    public void sacarMaxItemDiamond (Jugador jugador, Player player) {
        int dineroJugador = (int) jugador.getPixelcoins();

        int convertibles = dineroJugador - (dineroJugador % DIAMANTE);
        int items = (convertibles / DIAMANTE) % 9;
        int bloques = ((convertibles / DIAMANTE) - items) / 9;

        int bloquesAnadidos = 0;
        int[] slotsBloques = slotsItem(bloques, 36 - getEspaciosOcupados(player.getInventory()));
        Inventory inventoryJugador = player.getInventory();

        for (int i = 0; i < slotsBloques.length; i++) {
            bloquesAnadidos = bloquesAnadidos + slotsBloques[i];
            inventoryJugador.addItem(new ItemStack(Material.getMaterial("DIAMOND_BLOCK"), slotsBloques[i]));
        }
        int[] slotsDiamantes = slotsItem(items, 36 - getEspaciosOcupados(inventoryJugador));
        int diamantesAnadidos = 0;
        for (int i = 0; i < slotsDiamantes.length; i++) {
            diamantesAnadidos = diamantesAnadidos + slotsDiamantes[i];
            inventoryJugador.addItem(new ItemStack(Material.getMaterial("DIAMOND"), slotsDiamantes[i]));
        }

        int coste = (DIAMANTE * bloquesAnadidos * 9) + (DIAMANTE * diamantesAnadidos);
        jugadoresMySQL.setPixelcoin(player.getName(), dineroJugador - coste);
        nuevaTransaccion(player.getName(), "", coste, "DIAMOND", TipoTransaccion.WITHERS_SACARMAX);

        enviarMensajeYSonido(player ,ChatColor.GOLD + "Se ha a?adio: " + ChatColor.AQUA + "+" + bloquesAnadidos + " bloques " + "+" + diamantesAnadidos + " diamantes. " + ChatColor.RED + "-" + formatea.format(coste)
                + ChatColor.GOLD + " Quedan: " + ChatColor.GREEN + formatea.format(dineroJugador - coste) + " PC", Sound.ENTITY_PLAYER_LEVELUP);
    }

    public void sacarMaxItemLapisLazuli (Jugador jugador, Player player) {
        int dineroJugador = (int) jugador.getPixelcoins();

        int convertibles = dineroJugador - (dineroJugador % LAPISLAZULI);
        int items = (convertibles / LAPISLAZULI) % 9;
        int bloques = ((convertibles / LAPISLAZULI) - items) / 9;

        int bloquesAnadidos = 0;
        int[] slotsBloques = slotsItem(bloques, 36 - getEspaciosOcupados(player.getInventory()));
        Inventory inventoryJugador = player.getInventory();

        for (int i = 0; i < slotsBloques.length; i++) {
            bloquesAnadidos = bloquesAnadidos + slotsBloques[i];
            inventoryJugador.addItem(new ItemStack(Material.getMaterial("LAPIS_BLOCK"), slotsBloques[i]));
        }
        int[] slotsDiamantes = slotsItem(items, 36 - getEspaciosOcupados(inventoryJugador));
        int diamantesAnadidos = 0;
        for (int i = 0; i < slotsDiamantes.length; i++) {
            diamantesAnadidos = diamantesAnadidos + slotsDiamantes[i];
            inventoryJugador.addItem(new ItemStack(Material.getMaterial("LAPIS_LAZULI"), slotsDiamantes[i]));
        }

        int coste = (LAPISLAZULI * bloquesAnadidos * 9) + (LAPISLAZULI * diamantesAnadidos);
        jugadoresMySQL.setPixelcoin(player.getName(), dineroJugador - coste);
        nuevaTransaccion(player.getName(), "", coste, "LAPIS_LAZULI", TipoTransaccion.WITHERS_SACARMAX);

        enviarMensajeYSonido(player, ChatColor.GOLD + "Se ha a?adio: " + ChatColor.BLUE + "+" + bloquesAnadidos + " bloques " + "+" + diamantesAnadidos + " Lapislazuli. " + ChatColor.RED + "-" + formatea.format(coste)
                + ChatColor.GOLD + " Quedan: " + ChatColor.GREEN + formatea.format(dineroJugador - coste) + " PC", Sound.ENTITY_PLAYER_LEVELUP);
    }

    public void sacarMaxItemQuartzBlock (Jugador jugador, Player player) {
        int pixelcoinsJugador = (int) jugador.getPixelcoins();

        int bloques = (pixelcoinsJugador - (pixelcoinsJugador % CUARZO)) / CUARZO;

        int[] slotsBloques = slotsItem(bloques, 36 - getEspaciosOcupados(player.getInventory()));
        int bloquesAnadidos = 0;
        Inventory jugadorInventory = player.getInventory();
        for (int i = 0; i < slotsBloques.length; i++) {
            bloquesAnadidos = bloquesAnadidos + slotsBloques[i];
            jugadorInventory.addItem(new ItemStack(Material.getMaterial("QUARTZ_BLOCK"), slotsBloques[i]));
        }

        int coste = (CUARZO * bloquesAnadidos);
        jugadoresMySQL.setPixelcoin(player.getName(), pixelcoinsJugador - coste);
        nuevaTransaccion(player.getName(), "", coste, "QUARTZ_BLOCK", TipoTransaccion.WITHERS_SACARMAX);

        enviarMensajeYSonido(player, ChatColor.GOLD + "Se ha a?adio: " + ChatColor.GRAY + "+" + bloquesAnadidos + " bloques de cuarzo " + ChatColor.RED + "-" + formatea.format(coste)
                + ChatColor.GOLD + " Quedan: " + ChatColor.GREEN + formatea.format(pixelcoinsJugador - coste) + " PC", Sound.ENTITY_PLAYER_LEVELUP);
    }

    public void depositarPixelcoinsEmpresa(Player player, double pixelcoins, String nombreEmpresa) {
        String nombreJugador = player.getName();
        Jugador jugador = jugadoresMySQL.getJugador(nombreJugador);
        double pixelcoinsJugador = jugador.getPixelcoins();
        Empresa empresaADepositar = empresasMySQL.getEmpresa(nombreEmpresa);
        double pixelcoinsEmpresa = empresaADepositar.getPixelcoins();

        empresasMySQL.setPixelcoins(nombreEmpresa, pixelcoinsEmpresa + pixelcoins);
        jugadoresMySQL.setEstadisticas(nombreJugador, pixelcoinsJugador - pixelcoins, jugador.getNventas(), jugador.getIngresos(), jugador.getGastos());
        nuevaTransaccion(nombreJugador, nombreEmpresa, pixelcoins, "", TipoTransaccion.EMPRESA_DEPOSITAR);

        enviarMensajeYSonido(player, ChatColor.GOLD + "Has metido " + ChatColor.GREEN + formatea.format(pixelcoins) + " PC" + ChatColor.GOLD + " en tu empresa: " + ChatColor.DARK_AQUA + nombreEmpresa +
                ChatColor.GOLD + " ahora tiene: " + ChatColor.GREEN + formatea.format(pixelcoinsEmpresa + pixelcoins) + " PC", Sound.ENTITY_PLAYER_LEVELUP);

        ScoreBoardManager.getInstance().updateScoreboard(player);
    }

    public void sacarPixelcoinsEmpresa(Player player, double pixelcoins, String nombreEmpresa) {
        Empresa empresaASacar = empresasMySQL.getEmpresa(nombreEmpresa);
        Jugador jugadorQueSaca = jugadoresMySQL.getJugador(player.getName());
        String nombreJugador = player.getName();
        double pixelcoinsJugador = jugadorQueSaca.getPixelcoins();
        double pixelcoinsEmpresa = empresaASacar.getPixelcoins();

        empresasMySQL.setPixelcoins(nombreEmpresa, pixelcoinsEmpresa - pixelcoins);
        jugadoresMySQL.setEstadisticas(nombreJugador, pixelcoinsJugador + pixelcoins, jugadorQueSaca.getNventas(), jugadorQueSaca.getIngresos(), jugadorQueSaca.getGastos());
        nuevaTransaccion(nombreEmpresa, nombreJugador, pixelcoins, "", TipoTransaccion.EMPRESA_SACAR);

        enviarMensajeYSonido(player, ChatColor.GOLD + "Has sacado " + ChatColor.GREEN + formatea.format(pixelcoins) + " PC" + ChatColor.GOLD + " de tu empresa: " + ChatColor.DARK_AQUA + nombreEmpresa +
                ChatColor.GOLD + " ahora tiene: " + ChatColor.GREEN + formatea.format(pixelcoinsEmpresa - pixelcoins) + " PC", Sound.ENTITY_PLAYER_LEVELUP);
        ScoreBoardManager.getInstance().updateScoreboard(player);
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

    public void comprarServivio(String empresa, double precio, Player player) {
        Empresa empresaAComprar = empresasMySQL.getEmpresa(empresa);
        if (empresaAComprar == null) {
            player.sendMessage(ChatColor.DARK_RED + "Esa empresa no existe");
            return;
        }
        if (empresaAComprar.getOwner().equalsIgnoreCase(player.getName())) {
            player.sendMessage(ChatColor.DARK_RED + "No puedes comprar un servivio de tu propia empresa");
            return;
        }

        Jugador comprador = jugadoresMySQL.getJugador(player.getName());

        jugadoresMySQL.setEstadisticas(comprador.getNombre(), comprador.getPixelcoins() - precio, comprador.getNventas(), comprador.getIngresos(), comprador.getGastos() + precio);
        empresasMySQL.setPixelcoins(empresa, empresaAComprar.getPixelcoins() + precio);
        empresasMySQL.setIngresos(empresa, empresaAComprar.getIngresos() + precio);
        nuevaTransaccion(comprador.getNombre(), empresa, precio, "", TipoTransaccion.EMPRESA_COMPRAR_SERVICIO);

        String mensajeOnline = ChatColor.GOLD + comprador.getNombre() + " ha comprado vuestro servicio de la empresa: " + empresa + " por " + ChatColor.GREEN + formatea.format(precio) + " PC";
        String mensajeOffline = comprador.getNombre() + " ha comprado vuestro servicio de la empresa: " + empresa + " por " + formatea.format(precio) + " PC";
        enviarMensaje(empresaAComprar.getOwner(), mensajeOnline, mensajeOnline);

        player.sendMessage(ChatColor.GOLD + "Has pagado " + ChatColor.GREEN + precio + " PC " + ChatColor.GOLD + " a la empresa: " + empresa + " por su servicio");
    }

    public void comprarUnidadBolsa (TipoActivo tipo, String ticker, String nombreValor, String alias, double precioUnidad, int cantidad, String nombrePlayer) {
        Player player = Bukkit.getPlayer(nombrePlayer);
        Jugador comprador = jugadoresMySQL.getJugador(nombrePlayer);
        double precioTotal = precioUnidad * cantidad;

        jugadoresMySQL.setPixelcoin(nombrePlayer, comprador.getPixelcoins() - precioTotal);
        posicionesAbiertasMySQL.nuevaPosicion(nombrePlayer, tipo, ticker, cantidad, precioUnidad, TipoPosicion.LARGO);
        nuevaTransaccion(nombrePlayer, ticker, precioTotal, tipo +  " " + precioUnidad, TipoTransaccion.BOLSA_COMPRA);
        llamadasApiMySQL.nuevaLlamadaSiNoEstaReg(ticker, precioUnidad, tipo, nombreValor);
        llamadasApiMySQL.actualizar(ticker);

        enviarMensajeYSonido(player, ChatColor.GOLD + "Has comprado " + formatea.format(cantidad)  + " " + alias + " a " + ChatColor.GREEN + formatea.format(precioUnidad) + " PC" + ChatColor.GOLD + " que es un total de " +
                ChatColor.GREEN + formatea.format(precioTotal) + " PC " + ChatColor.GOLD + " comandos: " + ChatColor.AQUA + "/bolsa vender /bolsa cartera", Sound.ENTITY_PLAYER_LEVELUP);
        Bukkit.broadcastMessage(ChatColor.GOLD + player.getName() + " ha comprado " + cantidad + " " + alias +  " de " + nombreValor + " a " + ChatColor.GREEN + precioUnidad + "PC");
    }

    public void venderEnCortoBolsa (String playerName, String ticker, String nombreValor, int cantidad, double precioPorAccion) {
        Player player = Bukkit.getPlayer(playerName);
        Jugador jugador = jugadoresMySQL.getJugador(playerName);
        double dineroJugador = jugador.getPixelcoins();
        double valorTotal = precioPorAccion * cantidad;
        double comision = redondeoDecimales(reducirPorcentaje(valorTotal, 100 - PosicionesAbiertas.PORCENTAJE_CORTO), 2);
        
        if(comision > dineroJugador){
            player.sendMessage(ChatColor.DARK_RED + "No tienes el dinero suficiente para esa operacion");
            return;
        }

        jugadoresMySQL.setEstadisticas(player.getName(), jugador.getPixelcoins() - comision, jugador.getNventas(), jugador.getIngresos(), jugador.getGastos() + comision);
        posicionesAbiertasMySQL.nuevaPosicion(player.getName(), TipoActivo.ACCIONES, ticker, cantidad, precioPorAccion, TipoPosicion.CORTO);
        nuevaTransaccion(player.getName(), ticker, comision, "ACCIONES" +  " " + precioPorAccion, TipoTransaccion.BOLSA_CORTO_VENTA);
        llamadasApiMySQL.nuevaLlamadaSiNoEstaReg(ticker, precioPorAccion, TipoActivo.ACCIONES, nombreValor);

        enviarMensajeYSonido( player,ChatColor.GOLD + "Te has puesto corto en " + nombreValor + " en " + cantidad + " cada una a " + ChatColor.GREEN + formatea.format(precioPorAccion) + " PC " + ChatColor.GOLD +
                "Para recomprar las acciones: /bolsa comprarcorto <id>. /bolsa cartera" + ChatColor.GOLD + "Ademas se te ha cobrado un 5% del valor total de la venta (" + ChatColor.GREEN  + formatea.format(valorTotal) + " PC"
                + ChatColor.GOLD + ") por lo cual: " + ChatColor.RED + "-" + formatea.format(comision) + " PC", Sound.ENTITY_PLAYER_LEVELUP);
        Bukkit.broadcastMessage(ChatColor.GOLD + player.getName() + " se ha puesto en corto en " + nombreValor);
    }

    public void venderPosicion(PosicionAbierta posicionAVender, int cantidad, String nombreJugador) {
        Player player = Bukkit.getPlayer(nombreJugador);
        int idPosiconAbierta = posicionAVender.getId();
        double precioPorAccion = llamadasApiMySQL.getLlamadaAPI(posicionAVender.getNombre_activo()).getPrecio();

        String ticker = posicionAVender.getNombre_activo();
        int nAccionesTotlaesEnCartera = posicionAVender.getCantidad();
        double precioApertura = posicionAVender.getPrecio_apertura();
        String fechaApertura = posicionAVender.getFecha_apertura();
        double revalorizacionTotal = cantidad * precioPorAccion;
        double rentabilidad = redondeoDecimales(diferenciaPorcntual(precioApertura, precioPorAccion), 3);

        Jugador vendedor = jugadoresMySQL.getJugador(nombreJugador);
        double beneficiosPerdidas = revalorizacionTotal - (cantidad * precioApertura);

        if(beneficiosPerdidas >= 0)
            jugadoresMySQL.setEstadisticas(nombreJugador, vendedor.getPixelcoins() + revalorizacionTotal, vendedor.getNventas(), vendedor.getIngresos() + beneficiosPerdidas, vendedor.getGastos());
        else
            jugadoresMySQL.setEstadisticas(nombreJugador, vendedor.getPixelcoins() + revalorizacionTotal, vendedor.getNventas(), vendedor.getIngresos(), vendedor.getGastos() + beneficiosPerdidas);

        if (cantidad == nAccionesTotlaesEnCartera)
            posicionesAbiertasMySQL.borrarPosicionAbierta(idPosiconAbierta);
        else
            posicionesAbiertasMySQL.setCantidad(idPosiconAbierta, nAccionesTotlaesEnCartera - cantidad);

        String nombreValor = llamadasApiMySQL.getLlamadaAPI(ticker).getNombre_activo();

        llamadasApiMySQL.borrarLlamadaSiNoEsUsada(ticker);
        posicionesCerradasMySQL.nuevaPosicion(nombreJugador, posicionAVender.getTipo_activo(), ticker, cantidad, precioApertura, fechaApertura, precioPorAccion, nombreValor, rentabilidad, TipoPosicion.LARGO);
        nuevaTransaccion(ticker, nombreJugador, cantidad * precioPorAccion, "", TipoTransaccion.BOLSA_VENTA);

        String mensajeAEnviarAlJugador;
        if (rentabilidad <= 0) {
            mensajeAEnviarAlJugador = ChatColor.GOLD + "Has vendido " + formatea.format(cantidad) + " de " + ticker + " a " + ChatColor.GREEN + formatea.format(precioPorAccion)
                    + " PC/Accion " + ChatColor.GOLD + " cuando la compraste a " + ChatColor.GREEN + formatea.format(precioApertura) + " PC/Unidad " + ChatColor.GOLD + " -> " +
                    ChatColor.RED + formatea.format(rentabilidad) + "% : " + formatea.format(redondeoDecimales(beneficiosPerdidas, 3)) + " Perdidas PC " + ChatColor.GOLD + " de " + ChatColor.GREEN + formatea.format(revalorizacionTotal) + " PC";

            Bukkit.broadcastMessage(ChatColor.GOLD + nombreJugador + " ha alacanzado una rentabilidad del " + ChatColor.RED + formatea.format(redondeoDecimales(rentabilidad, 3)) + "% "
                    + ChatColor.GOLD + "de las acciones de " + nombreValor + " (" + ticker + ")");
        } else {
            mensajeAEnviarAlJugador = ChatColor.GOLD + "Has vendido " + formatea.format(cantidad) + " de " + ticker + " a " + ChatColor.GREEN + formatea.format(precioPorAccion)
                    + " PC/Accion " + ChatColor.GOLD + " cuando la compraste a " + ChatColor.GREEN + formatea.format(precioApertura) + " PC/Unidad " + ChatColor.GOLD + " -> " +
                    ChatColor.GREEN + formatea.format(rentabilidad) + "% : " + formatea.format(redondeoDecimales(beneficiosPerdidas, 3)) + " Beneficios PC " + ChatColor.GOLD + " de " + ChatColor.GREEN + formatea.format(revalorizacionTotal) + " PC";

            Bukkit.broadcastMessage(ChatColor.GOLD + nombreJugador + " ha alacanzado una rentabilidad del " + ChatColor.GREEN + "+" + formatea.format(redondeoDecimales(rentabilidad, 3)) + "% "
                    + ChatColor.GOLD + "de las acciones de " + nombreValor + " (" + ticker + ")");
        }

        enviarMensajeYSonido(player, mensajeAEnviarAlJugador, Sound.ENTITY_PLAYER_LEVELUP);
    }

    public void comprarPosicionCorto (PosicionAbierta posicionAComprar, int cantidad, String playername) {
        Player player = Bukkit.getPlayer(playername);
        int idPosiconAbierta = posicionAComprar.getId();
        double precioPorAccion = llamadasApiMySQL.getLlamadaAPI(posicionAComprar.getNombre_activo()).getPrecio();

        String ticker = posicionAComprar.getNombre_activo();
        int nAccionesTotlaesEnCartera = posicionAComprar.getCantidad();
        double precioApertura = posicionAComprar.getPrecio_apertura();
        String fechaApertura = posicionAComprar.getFecha_apertura();
        double revalorizacionTotal = cantidad * (precioApertura - precioPorAccion);
        double rentabilidad = redondeoDecimales(diferenciaPorcntual(precioPorAccion, precioApertura), 3);
        Jugador compradorJugador = jugadoresMySQL.getJugador(playername);

        double pixelcoinsJugador = compradorJugador.getPixelcoins();
        if(0 > pixelcoinsJugador + revalorizacionTotal)
            jugadoresMySQL.setEstadisticas(playername, pixelcoinsJugador + revalorizacionTotal, compradorJugador.getNventas(), compradorJugador.getIngresos(), compradorJugador.getGastos() + revalorizacionTotal);
        else
            jugadoresMySQL.setEstadisticas(playername, pixelcoinsJugador + revalorizacionTotal, compradorJugador.getNventas(), compradorJugador.getIngresos() + revalorizacionTotal, compradorJugador.getGastos());

        if (cantidad == nAccionesTotlaesEnCartera)
            posicionesAbiertasMySQL.borrarPosicionAbierta(idPosiconAbierta);
        else
            posicionesAbiertasMySQL.setCantidad(idPosiconAbierta, nAccionesTotlaesEnCartera - cantidad);

        String nombreValor = llamadasApiMySQL.getLlamadaAPI(ticker).getNombre_activo();
        llamadasApiMySQL.borrarLlamadaSiNoEsUsada(ticker);
        posicionesCerradasMySQL.nuevaPosicion(playername, posicionAComprar.getTipo_activo(), ticker, cantidad, precioApertura, fechaApertura, precioPorAccion, nombreValor, rentabilidad, TipoPosicion.CORTO);
        nuevaTransaccion(ticker, playername, cantidad * precioPorAccion, "", TipoTransaccion.BOLSA_CORTO_COMPRA);

        String mensaje;
        if (rentabilidad <= 0)
            mensaje = ChatColor.GOLD + "Has comprado en corto" + formatea.format(cantidad) + " de " + ticker + " a " + ChatColor.GREEN + formatea.format(precioPorAccion)
                    + " PC/Accion " + ChatColor.GOLD + " cuando la vendiste a " + ChatColor.GREEN + formatea.format(precioApertura) + " PC/Unidad " + ChatColor.GOLD + " -> " +
                    ChatColor.RED + formatea.format(rentabilidad) + "% : " + formatea.format(redondeoDecimales(revalorizacionTotal, 3)) + " Perdidas PC ";
        else
            mensaje = ChatColor.GOLD + "Has comprado en corto" + formatea.format(cantidad) + " de " + ticker + " a " + ChatColor.GREEN + formatea.format(precioPorAccion)
                    + " PC/Accion " + ChatColor.GOLD + " cuando la vendiste a " + ChatColor.GREEN + formatea.format(precioApertura) + " PC/Unidad " + ChatColor.GOLD + " -> " +
                    ChatColor.GREEN + formatea.format(rentabilidad) + "% : " + formatea.format(redondeoDecimales(revalorizacionTotal, 3)) + " Beneficios PC ";

        enviarMensajeYSonido(player, mensaje, Sound.ENTITY_PLAYER_LEVELUP);

        Bukkit.broadcastMessage(ChatColor.GOLD + playername + " ha alacanzado una rentabilidad del " + ChatColor.GREEN + "+" + formatea.format(redondeoDecimales(rentabilidad, 3)) + "% "
                + ChatColor.GOLD + "de las acciones de " + nombreValor + " (" + ticker + "), poniendose en " + ChatColor.BOLD  + "CORTO");
    }

    public void pagaDividendo(String ticker, String nombre, double precioDividendo, int nAcciones) {
        double aPagar = precioDividendo * nAcciones;

        jugadoresMySQL.setPixelcoin(nombre, jugadoresMySQL.getJugador(nombre).getPixelcoins() + aPagar);
        nuevaTransaccion(ticker, nombre, aPagar, "", TipoTransaccion.BOLSA_DIVIDENDO);

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
