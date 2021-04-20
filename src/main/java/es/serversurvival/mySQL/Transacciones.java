package es.serversurvival.mySQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import es.jaime.EventListener;
import es.serversurvival.main.Pixelcoin;
import es.serversurvival.mySQL.enums.*;
import es.serversurvival.mySQL.eventos.bolsa.*;
import es.serversurvival.mySQL.eventos.TransactionEvent;
import es.serversurvival.mySQL.eventos.empresas.*;
import es.serversurvival.mySQL.eventos.jugadores.JugadorPagoManualEvento;
import es.serversurvival.mySQL.eventos.tienda.ItemCompradoEvento;
import es.serversurvival.mySQL.eventos.withers.ItemIngresadoEvento;
import es.serversurvival.mySQL.eventos.withers.ItemSacadoEvento;
import es.serversurvival.mySQL.eventos.withers.ItemSacadoMaxEvento;
import es.serversurvival.mySQL.tablasObjetos.*;
import es.serversurvival.mySQL.tablasObjetos.Jugador;
import es.serversurvival.util.MinecraftUtils;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import static es.serversurvival.mySQL.enums.CambioPixelcoins.*;
import static es.serversurvival.mySQL.enums.TipoActivo.*;
import static es.serversurvival.mySQL.enums.TipoPosicion.*;
import static es.serversurvival.mySQL.enums.TipoTransaccion.*;
import static es.serversurvival.util.Funciones.*;
import static org.bukkit.ChatColor.*;

/**
 * 792 -> 600 -> 497
 */
public final class Transacciones extends MySQL {
    public static final Transacciones INSTANCE = new Transacciones();

    @EventListener
    public void onTransaction (TransactionEvent event) {
        Transaccion transaccion = event.buildTransaccion();

        nuevaTransaccion(transaccion);
    }

    private void nuevaTransaccion(Transaccion transaccion) {
        String fecha = transaccion.getFecha();

        executeUpdate("INSERT INTO transacciones (fecha, comprador, vendedor, cantidad, objeto, tipo) VALUES ('" + fecha + "','" + transaccion.getComprador() + "','" + transaccion.getVendedor() + "','" + transaccion.getCantidad() + "','" + transaccion.getObjeto() + "','" + transaccion.getTipo().toString() + "')");
    }

    public List<Transaccion> getTransaccionesPagaEmpresa (String jugador) {
        return buildListFromQuery("SELECT * FROM transacciones WHERE comprador = '"+jugador+"' AND tipo = '"+ EMPRESA_PAGAR_SALARIO.toString()+"'");
    }

    public void setCompradorVendedor (String jugador, String nuevoJugador) {
        executeUpdate("UPDATE transacciones SET comprador = '"+nuevoJugador+"' WHERE comprador = '"+jugador+"'");
        executeUpdate("UPDATE transacciones SET vendedor = '"+nuevoJugador+"' WHERE vendedor = '"+jugador+"'");
    }

    public void realizarVenta(String comprador, int id, Player player) {
        Oferta ofertaAComprar = ofertasMySQL.getOferta(id);
        Jugador jugadorComprador = jugadoresMySQL.getJugador(comprador);

        if (jugadorComprador.getPixelcoins() < ofertaAComprar.getPrecio()) {
            player.sendMessage(DARK_RED + "No puedes comprar por encima de tu dinero");
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

        realizarTransferenciaConEstadisticas(comprador, vendedor, precio, objeto, TIENDA_VENTA);

        Pixelcoin.publish(new ItemCompradoEvento(vendedor, comprador, objeto, cantidad, precio));

        MinecraftUtils.setLore(itemAComprar, Collections.singletonList("Comprado en la tienda"));
        player.getInventory().addItem(itemAComprar);

        enviarMensajeYSonido(player, GOLD + "Has comprado: " + objeto + " , por " + GREEN + formatea.format(precio) + " PC" + GOLD + " .Te quedan: " +
                GREEN + formatea.format(jugadorComprador.getPixelcoins() - precio) + " PC", Sound.ENTITY_PLAYER_LEVELUP);

        enviarMensajeYSonidoSiOnline(vendedor, GOLD + comprador + " te ha comprado: " + objeto + " por: " + GREEN + formatea.format(precio) + " PC ", Sound.ENTITY_PLAYER_LEVELUP);
    }

    public void realizarTransferencia (String nombrePagador, String nombrePagado, double cantidad, String objeto, TipoTransaccion tipo) {
        Jugador pagador = jugadoresMySQL.getJugador(nombrePagador);
        Jugador pagado = jugadoresMySQL.getJugador(nombrePagado);

        jugadoresMySQL.setPixelcoin(nombrePagado, pagado.getPixelcoins() + cantidad);
        jugadoresMySQL.setPixelcoin(nombrePagador, pagador.getPixelcoins() - cantidad);
    }

    public void realizarTransferenciaConEstadisticas (String nombrePagador, String nombrePagado, double cantidad, String objeto, TipoTransaccion tipo) {
        Jugador pagador = jugadoresMySQL.getJugador(nombrePagador);
        Jugador pagado = jugadoresMySQL.getJugador(nombrePagado);

        jugadoresMySQL.setEstadisticas(nombrePagado, pagado.getPixelcoins() + cantidad, pagado.getNventas() + 1, pagado.getIngresos() + cantidad, pagado.getGastos());
        jugadoresMySQL.setEstadisticas(nombrePagador, pagador.getPixelcoins() - cantidad, pagador.getNventas(), pagador.getIngresos(), pagador.getGastos() + cantidad);
    }

    public void realizarPagoManual(String nombrePagador, String nombrePagado, double cantidad, Player player, String objeto) {
        realizarTransferenciaConEstadisticas(nombrePagador, nombrePagado, cantidad, objeto, JUGADOR_PAGO_MANUAL);

        Pixelcoin.publish(new JugadorPagoManualEvento(nombrePagador, nombrePagado, cantidad));

        player.sendMessage(GOLD + "Has pagado: " + GREEN + formatea.format(cantidad) + " PC " + GOLD + "a " + nombrePagado);

        String mensajeSiEstaOnline = GOLD + nombrePagador + " te ha pagado: " + GREEN + "+" + formatea.format(cantidad) + " PC " + AQUA + "(/estadisticas)";
        enviarMensaje(nombrePagado, mensajeSiEstaOnline, mensajeSiEstaOnline, Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
    }

    public void ingresarItem(ItemStack itemAIngresar, Player player) {
        Jugador jugadorQueIngresaElItem = jugadoresMySQL.getJugador(player.getName());

        int cantidad = itemAIngresar.getAmount();
        String nombreItem = itemAIngresar.getType().toString();
        double pixelcoinsAnadir = getCambioTotal(nombreItem, cantidad);
        double dineroActual = jugadorQueIngresaElItem.getPixelcoins();

        jugadoresMySQL.setPixelcoin(player.getName(), pixelcoinsAnadir + dineroActual);

        player.getInventory().clear(player.getInventory().getHeldItemSlot());

        Pixelcoin.publish(new ItemIngresadoEvento(player.getName(), pixelcoinsAnadir, nombreItem));

        enviarMensajeYSonido(player, GOLD + "Se ha a?adido: " + GREEN + formatea.format(pixelcoinsAnadir) + " PC " + GOLD + "Ahora tienes: " +
                GREEN + formatea.format(pixelcoinsAnadir + dineroActual) + "PC", Sound.ENTITY_PLAYER_LEVELUP);
    }

    public double sacarObjeto (Jugador jugador, String itemNombre, int pixelcoinsPorItem) {
        Player player= Bukkit.getPlayer(jugador.getNombre());

        if(jugador.getPixelcoins() >= pixelcoinsPorItem){
            jugadoresMySQL.setPixelcoin(jugador.getNombre(), jugador.getPixelcoins() - pixelcoinsPorItem);
            player.getInventory().addItem(new ItemStack(Material.getMaterial(itemNombre), 1));

            Pixelcoin.publish(new ItemSacadoEvento(player.getName(), itemNombre, pixelcoinsPorItem));

            enviarMensajeYSonido(player, GOLD + "Has convertido las pixelcoins" + RED + "-" + pixelcoinsPorItem + " PC " + GOLD +
                    "Quedan " + GREEN + formatea.format(jugador.getPixelcoins() - pixelcoinsPorItem) + " PC", Sound.ENTITY_PLAYER_LEVELUP);

            return pixelcoinsPorItem;
        }else{
            enviarMensajeYSonido(player, DARK_RED + "Necesitas tener minimo " + pixelcoinsPorItem + " pixelcoins para convertirlo", Sound.ENTITY_VILLAGER_NO);

            return 0;
        }
    }

    public void sacarMaxItem(String tipo, Player player) {
        Jugador jugadorASacar = jugadoresMySQL.getJugador(player.getName());
        int pixelcoinsJugador = (int) jugadoresMySQL.getJugador(player.getName()).getPixelcoins();

        if (!suficientesPixelcoins(tipo, 1, pixelcoinsJugador)) {
            enviarMensajeYSonido(player, DARK_RED + "No tienes las suficientes pixelcoins", Sound.ENTITY_VILLAGER_NO);
            return;
        }

        CambioPixelcoins.sacarMaxItem(tipo, jugadorASacar, player);
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

        Pixelcoin.publish(new ItemSacadoMaxEvento(player.getName(), "DIAMOND", coste));

        enviarMensajeYSonido(player , GOLD + "Se ha a?adio: " + AQUA + "+" + bloquesAnadidos + " bloques " + "+" + diamantesAnadidos + " diamantes. " + RED + "-" + formatea.format(coste)
                + GOLD + " Quedan: " + GREEN + formatea.format(dineroJugador - coste) + " PC", Sound.ENTITY_PLAYER_LEVELUP);
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

        Pixelcoin.publish(new ItemSacadoMaxEvento(player.getName(), "LAPIS_LAZULI", coste));

        enviarMensajeYSonido(player, GOLD + "Se ha a?adio: " + BLUE + "+" + bloquesAnadidos + " bloques " + "+" + diamantesAnadidos + " Lapislazuli. " + RED + "-" + formatea.format(coste)
                + GOLD + " Quedan: " + GREEN + formatea.format(dineroJugador - coste) + " PC", Sound.ENTITY_PLAYER_LEVELUP);
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

        Pixelcoin.publish(new ItemSacadoMaxEvento(player.getName(), "QUARTZ_BLOCK", coste));

        enviarMensajeYSonido(player, GOLD + "Se ha a?adio: " + GRAY + "+" + bloquesAnadidos + " bloques de cuarzo " + RED + "-" + formatea.format(coste)
                + GOLD + " Quedan: " + GREEN + formatea.format(pixelcoinsJugador - coste) + " PC", Sound.ENTITY_PLAYER_LEVELUP);
    }

    public void depositarPixelcoinsEmpresa(Player player, double pixelcoins, String nombreEmpresa) {
        String nombreJugador = player.getName();
        Jugador jugador = jugadoresMySQL.getJugador(nombreJugador);
        double pixelcoinsJugador = jugador.getPixelcoins();
        Empresa empresaADepositar = empresasMySQL.getEmpresa(nombreEmpresa);
        double pixelcoinsEmpresa = empresaADepositar.getPixelcoins();

        empresasMySQL.setPixelcoins(nombreEmpresa, pixelcoinsEmpresa + pixelcoins);
        jugadoresMySQL.setEstadisticas(nombreJugador, pixelcoinsJugador - pixelcoins, jugador.getNventas(), jugador.getIngresos(), jugador.getGastos());

        Pixelcoin.publish(new PixelcoinsDepositadasEvento(nombreJugador, nombreEmpresa, pixelcoins));

        enviarMensajeYSonido(player, GOLD + "Has metido " + GREEN + formatea.format(pixelcoins) + " PC" + GOLD + " en tu empresa: " + DARK_AQUA + nombreEmpresa +
                GOLD + " ahora tiene: " + GREEN + formatea.format(pixelcoinsEmpresa + pixelcoins) + " PC", Sound.ENTITY_PLAYER_LEVELUP);
    }

    public void sacarPixelcoinsEmpresa(Player player, double pixelcoins, String nombreEmpresa) {
        Empresa empresaASacar = empresasMySQL.getEmpresa(nombreEmpresa);
        Jugador jugadorQueSaca = jugadoresMySQL.getJugador(player.getName());
        String nombreJugador = player.getName();
        double pixelcoinsJugador = jugadorQueSaca.getPixelcoins();
        double pixelcoinsEmpresa = empresaASacar.getPixelcoins();

        empresasMySQL.setPixelcoins(nombreEmpresa, pixelcoinsEmpresa - pixelcoins);
        jugadoresMySQL.setEstadisticas(nombreJugador, pixelcoinsJugador + pixelcoins, jugadorQueSaca.getNventas(), jugadorQueSaca.getIngresos(), jugadorQueSaca.getGastos());

        Pixelcoin.publish(new PixelcoinsSacadasEvento(nombreJugador, nombreEmpresa, pixelcoins));

        enviarMensajeYSonido(player, GOLD + "Has sacado " + GREEN + formatea.format(pixelcoins) + " PC" + GOLD + " de tu empresa: " + DARK_AQUA + nombreEmpresa +
                GOLD + " ahora tiene: " + GREEN + formatea.format(pixelcoinsEmpresa - pixelcoins) + " PC", Sound.ENTITY_PLAYER_LEVELUP);
    }

    public void comprarEmpresa(String vendedor, String comprador, String empresa, double precio, Player p) {
        Jugador jugadorVendedor = jugadoresMySQL.getJugador(vendedor);
        Jugador jugadorComprador = jugadoresMySQL.getJugador(comprador);

        empresasMySQL.setOwner(empresa, comprador);
        jugadoresMySQL.setEstadisticas(vendedor, jugadorVendedor.getPixelcoins() + precio, jugadorVendedor.getNventas() + 1, jugadorVendedor.getIngresos() + precio, jugadorVendedor.getGastos());
        jugadoresMySQL.setEstadisticas(comprador, jugadorComprador.getPixelcoins() - precio, jugadorComprador.getNventas(), jugadorComprador.getIngresos(), jugadorComprador.getGastos() + precio);

        Pixelcoin.publish(new EmpresaVendidaEvento(jugadorComprador.getNombre(), jugadorVendedor.getNombre(), empresa, precio));
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

        Pixelcoin.publish(new SalarioPagadoEvento(jugador, empresa, salario));

        return true;
    }

    public void comprarServivio(String empresa, double precio, Player player) {
        Empresa empresaAComprar = empresasMySQL.getEmpresa(empresa);
        if (empresaAComprar == null) {
            player.sendMessage(DARK_RED + "Esa empresa no existe");
            return;
        }
        if (empresaAComprar.getOwner().equalsIgnoreCase(player.getName())) {
            player.sendMessage(DARK_RED + "No puedes comprar un servivio de tu propia empresa");
            return;
        }

        Jugador comprador = jugadoresMySQL.getJugador(player.getName());

        jugadoresMySQL.setEstadisticas(comprador.getNombre(), comprador.getPixelcoins() - precio, comprador.getNventas(), comprador.getIngresos(), comprador.getGastos() + precio);
        empresasMySQL.setPixelcoins(empresa, empresaAComprar.getPixelcoins() + precio);
        empresasMySQL.setIngresos(empresa, empresaAComprar.getIngresos() + precio);

        Pixelcoin.publish(new ServicioCompradoEvento(comprador.getNombre(), empresa, precio));

        String mensajeOnline = GOLD + comprador.getNombre() + " ha comprado vuestro servicio de la empresa: " + empresa + " por " + GREEN + formatea.format(precio) + " PC";
        enviarMensaje(empresaAComprar.getOwner(), mensajeOnline, mensajeOnline);

        player.sendMessage(GOLD + "Has pagado " + GREEN + precio + " PC " + GOLD + " a la empresa: " + empresa + " por su servicio");
    }

    public void comprarUnidadBolsa (TipoActivo tipoActivo, String ticker, String nombreValor, String alias, double precioUnidad, int cantidad, String nombrePlayer) {
        Player player = Bukkit.getPlayer(nombrePlayer);
        Jugador comprador = jugadoresMySQL.getJugador(nombrePlayer);
        double precioTotal = precioUnidad * cantidad;

        jugadoresMySQL.setPixelcoin(nombrePlayer, comprador.getPixelcoins() - precioTotal);
        posicionesAbiertasMySQL.nuevaPosicion(nombrePlayer, tipoActivo, ticker, cantidad, precioUnidad, LARGO);

        enviarMensajeYSonido(player, GOLD + "Has comprado " + formatea.format(cantidad)  + " " + alias + " a " + GREEN + formatea.format(precioUnidad) + " PC" + GOLD + " que es un total de " +
                GREEN + formatea.format(precioTotal) + " PC " + GOLD + " comandos: " + AQUA + "/bolsa vender /bolsa cartera", Sound.ENTITY_PLAYER_LEVELUP);
        Bukkit.broadcastMessage(GOLD + player.getName() + " ha comprado " + cantidad + " " + alias +  " de " + nombreValor + " a " + GREEN + precioUnidad + "PC");

        Pixelcoin.publish(new PosicionCompraLargoEvento(comprador.getNombre(), precioUnidad, cantidad, cantidad*precioUnidad, ticker, tipoActivo, nombreValor));
    }

    public void venderEnCortoBolsa (String playerName, String ticker, String nombreValor, int cantidad, double precioPorAccion) {
        Player player = Bukkit.getPlayer(playerName);
        Jugador jugador = jugadoresMySQL.getJugador(playerName);
        double dineroJugador = jugador.getPixelcoins();
        double valorTotal = precioPorAccion * cantidad;
        double comision = redondeoDecimales(reducirPorcentaje(valorTotal, 100 - PosicionesAbiertas.PORCENTAJE_CORTO), 2);
        
        if(comision > dineroJugador){
            player.sendMessage(DARK_RED + "No tienes el dinero suficiente para esa operacion");
            return;
        }

        jugadoresMySQL.setEstadisticas(player.getName(), jugador.getPixelcoins() - comision, jugador.getNventas(), jugador.getIngresos(), jugador.getGastos() + comision);
        posicionesAbiertasMySQL.nuevaPosicion(player.getName(), ACCIONES, ticker, cantidad, precioPorAccion, CORTO);
                
        Pixelcoin.publish(new PosicionVentaCortoEvento(playerName, precioPorAccion, cantidad, comision, ticker, ACCIONES, nombreValor));

        enviarMensajeYSonido( player, GOLD + "Te has puesto corto en " + nombreValor + " en " + cantidad + " cada una a " + GREEN + formatea.format(precioPorAccion) + " PC " + GOLD +
                "Para recomprar las acciones: /bolsa comprarcorto <id>. /bolsa cartera" + GOLD + "Ademas se te ha cobrado un 5% del valor total de la venta (" + GREEN  + formatea.format(valorTotal) + " PC"
                + GOLD + ") por lo cual: " + RED + "-" + formatea.format(comision) + " PC", Sound.ENTITY_PLAYER_LEVELUP);
        Bukkit.broadcastMessage(GOLD + player.getName() + " se ha puesto en corto en " + nombreValor);
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
        posicionesCerradasMySQL.nuevaPosicion(nombreJugador, posicionAVender.getTipo_activo(), ticker, cantidad, precioApertura, fechaApertura, precioPorAccion, nombreValor, rentabilidad, LARGO);

        Pixelcoin.publish(new PosicionVentaLargoEvento(nombreJugador, precioPorAccion, cantidad, precioPorAccion*cantidad, ticker, posicionAVender.getTipo_activo(), nombreValor));

        String mensajeAEnviarAlJugador;
        if (rentabilidad <= 0) {
            mensajeAEnviarAlJugador = GOLD + "Has vendido " + formatea.format(cantidad) + " de " + ticker + " a " + GREEN + formatea.format(precioPorAccion)
                    + " PC/Accion " + GOLD + " cuando la compraste a " + GREEN + formatea.format(precioApertura) + " PC/Unidad " + GOLD + " -> " +
                    RED + formatea.format(rentabilidad) + "% : " + formatea.format(redondeoDecimales(beneficiosPerdidas, 3)) + " Perdidas PC " + GOLD + " de " + GREEN + formatea.format(revalorizacionTotal) + " PC";

            Bukkit.broadcastMessage(GOLD + nombreJugador + " ha alacanzado una rentabilidad del " + RED + formatea.format(redondeoDecimales(rentabilidad, 3)) + "% "
                    + GOLD + "de las acciones de " + nombreValor + " (" + ticker + ")");
        } else {
            mensajeAEnviarAlJugador = GOLD + "Has vendido " + formatea.format(cantidad) + " de " + ticker + " a " + GREEN + formatea.format(precioPorAccion)
                    + " PC/Accion " + GOLD + " cuando la compraste a " + GREEN + formatea.format(precioApertura) + " PC/Unidad " + GOLD + " -> " +
                    GREEN + formatea.format(rentabilidad) + "% : " + formatea.format(redondeoDecimales(beneficiosPerdidas, 3)) + " Beneficios PC " + GOLD + " de " + GREEN + formatea.format(revalorizacionTotal) + " PC";

            Bukkit.broadcastMessage(GOLD + nombreJugador + " ha alacanzado una rentabilidad del " + GREEN + "+" + formatea.format(redondeoDecimales(rentabilidad, 3)) + "% "
                    + GOLD + "de las acciones de " + nombreValor + " (" + ticker + ")");
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
        posicionesCerradasMySQL.nuevaPosicion(playername, posicionAComprar.getTipo_activo(), ticker, cantidad, precioApertura, fechaApertura, precioPorAccion, nombreValor, rentabilidad, CORTO);

        Pixelcoin.publish(new PosicionCompraCortoEvento(playername, precioPorAccion, cantidad, revalorizacionTotal, ticker, ACCIONES, nombreValor));

        String mensaje;
        if (rentabilidad <= 0)
            mensaje = GOLD + "Has comprado en corto" + formatea.format(cantidad) + " de " + ticker + " a " + GREEN + formatea.format(precioPorAccion)
                    + " PC/Accion " + GOLD + " cuando la vendiste a " + GREEN + formatea.format(precioApertura) + " PC/Unidad " + GOLD + " -> " +
                    RED + formatea.format(rentabilidad) + "% : " + formatea.format(redondeoDecimales(revalorizacionTotal, 3)) + " Perdidas PC ";
        else
            mensaje = GOLD + "Has comprado en corto" + formatea.format(cantidad) + " de " + ticker + " a " + GREEN + formatea.format(precioPorAccion)
                    + " PC/Accion " + GOLD + " cuando la vendiste a " + GREEN + formatea.format(precioApertura) + " PC/Unidad " + GOLD + " -> " +
                    GREEN + formatea.format(rentabilidad) + "% : " + formatea.format(redondeoDecimales(revalorizacionTotal, 3)) + " Beneficios PC ";

        enviarMensajeYSonido(player, mensaje, Sound.ENTITY_PLAYER_LEVELUP);

        Bukkit.broadcastMessage(GOLD + playername + " ha alacanzado una rentabilidad del " + GREEN + "+" + formatea.format(redondeoDecimales(rentabilidad, 3)) + "% "
                + GOLD + "de las acciones de " + nombreValor + " (" + ticker + "), poniendose en " + BOLD  + "CORTO");
    }

    public void pagaDividendo(String ticker, String nombre, double precioDividendo, int nAcciones) {
        double aPagar = precioDividendo * nAcciones;

        jugadoresMySQL.setPixelcoin(nombre, jugadoresMySQL.getJugador(nombre).getPixelcoins() + aPagar);

        Pixelcoin.publish(new DividendoPagadoEvento(nombre, ticker, aPagar));

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
    }

    public void comprarOfertaMercadoAccionServer (Player player, int idOfeta, int cantidadAComprar) {
        OfertaMercadoServer oferta = ofertasMercadoServerMySQL.get(idOfeta);
        Jugador jugadorComprador = jugadoresMySQL.getJugador(player.getName());
        double precioTotalAPagar = oferta.getPrecio() * cantidadAComprar;

        if(oferta.getPrecio() * cantidadAComprar > jugadorComprador.getPixelcoins()){
            enviarMensajeYSonido(player, DARK_RED + "No tienes el suficiente dinero", Sound.ENTITY_VILLAGER_NO);
            return;
        }

        jugadoresMySQL.setPixelcoin(jugadorComprador.getNombre(), jugadorComprador.getPixelcoins() - precioTotalAPagar);
        posicionesAbiertasMySQL.nuevaPosicion(jugadorComprador.getNombre(), ACCIONES_SERVER, oferta.getEmpresa(), cantidadAComprar, oferta.getPrecio(), LARGO);
        ofertasMercadoServerMySQL.setCantidadOBorrar(idOfeta, oferta.getCantidad() - cantidadAComprar);

        if(oferta.getTipo_ofertante() == TipoOfertante.EMPRESA){
            comprarAccionServerAEmpresa(player, oferta, cantidadAComprar, precioTotalAPagar);
        }else{
            comprarAccionServerAJugador(player, oferta, cantidadAComprar, precioTotalAPagar);
        }

        enviarMensajeYSonido(player, GOLD + "Has comprado " + formatea.format(cantidadAComprar)  + " acciones a " + GREEN + formatea.format(oferta.getPrecio()) + " PC" + GOLD + " que es un total de " +
                GREEN + formatea.format(precioTotalAPagar) + " PC " + GOLD + " comandos: " + AQUA + "/bolsa vender /bolsa cartera", Sound.ENTITY_PLAYER_LEVELUP);
        Bukkit.broadcastMessage(GOLD + player.getName() + " ha comprado " + cantidadAComprar + " acciones de la empresa del server: " + oferta.getEmpresa() + " a " + GREEN + oferta.getPrecio() + "PC");

        Pixelcoin.publish(new EmpresaServerAccionCompradaEvento(player.getName(), oferta.getEmpresa(), precioTotalAPagar));
    }
    
    private void comprarAccionServerAEmpresa (Player player, OfertaMercadoServer oferta, int cantidadAComprar, double precioTotalAPagar) {
        Empresa empresa = empresasMySQL.getEmpresa(oferta.getEmpresa());

        empresasMySQL.setPixelcoins(empresa.getNombre(), empresa.getPixelcoins() + precioTotalAPagar);
        empresasMySQL.setIngresos(empresa.getNombre(), empresa.getIngresos() + precioTotalAPagar);

        String mensajeOnline = GOLD + player.getName() + " ha comprado " + cantidadAComprar + " acciones de " + empresa.getNombre() + "."+GREEN+" +" + formatea.format(precioTotalAPagar) + "PC";

        enviarMensaje(empresa.getOwner(), mensajeOnline, mensajeOnline);
    }

    private void comprarAccionServerAJugador (Player player, OfertaMercadoServer oferta, int cantidadAComprar, double precioTotalAPagar) {
        Jugador jugadorVendedor = jugadoresMySQL.getJugador(oferta.getJugador());
        double beneficiosPerdidas = (oferta.getPrecio() - oferta.getPrecio_apertura()) * cantidadAComprar;
        double rentabilidad = redondeoDecimales(diferenciaPorcntual(oferta.getPrecio_apertura(), oferta.getPrecio()), 3);

        if(beneficiosPerdidas >= 0)
            jugadoresMySQL.setEstadisticas(jugadorVendedor.getNombre(), jugadorVendedor.getPixelcoins() + precioTotalAPagar, jugadorVendedor.getNventas(), jugadorVendedor.getIngresos(), jugadorVendedor.getGastos() + beneficiosPerdidas);
        else
            jugadoresMySQL.setEstadisticas(jugadorVendedor.getNombre(), jugadorVendedor.getPixelcoins() + precioTotalAPagar, jugadorVendedor.getNventas(), jugadorVendedor.getIngresos() + beneficiosPerdidas, jugadorVendedor.getGastos());

        posicionesCerradasMySQL.nuevaPosicion(oferta.getJugador(), ACCIONES_SERVER, oferta.getEmpresa(), cantidadAComprar, oferta.getPrecio_apertura(), oferta.getFecha(), oferta.getPrecio(), oferta.getEmpresa(), rentabilidad, LARGO);

        String mensajeOnline = beneficiosPerdidas >= 0 ?
                GOLD + player.getName() + " te ha comprado " + cantidadAComprar + " acciones de " + oferta.getEmpresa() + " con unos beneficios de " + GREEN + "+" + formatea.format(beneficiosPerdidas) + " PC +" + formatea.format(rentabilidad):
                GOLD + player.getName() + " te ha comprado " + cantidadAComprar + " acciones de " + oferta.getEmpresa() + " con unos beneficios de " + RED + formatea.format(beneficiosPerdidas) + " PC " + formatea.format(rentabilidad) ;
        String mensajeOffline = beneficiosPerdidas >= 0 ?
                player.getName() + " te ha comprado " + cantidadAComprar + " acciones de " + oferta.getEmpresa() + " con unos beneficios de " + "+" + formatea.format(beneficiosPerdidas) + " PC +" + formatea.format(rentabilidad) :
                player.getName() + " te ha comprado " + cantidadAComprar + " acciones de " + oferta.getEmpresa() + " con unos beneficios de " + formatea.format(beneficiosPerdidas) + " PC" + formatea.format(rentabilidad);

        enviarMensaje(jugadorVendedor.getNombre(), mensajeOnline, mensajeOffline);
    }

    public void pagarDividendoAccionServer (Player owner, String nombreEmpresa, double dividendoPorAccion, double totalAPagar) {
        List<PosicionAbierta> posicionesAccion = posicionesAbiertasMySQL.getPosicionesAccionesServer(nombreEmpresa);
        List<OfertaMercadoServer> ofertasAccion =  ofertasMercadoServerMySQL.getOfertasEmpresa(nombreEmpresa, OfertaMercadoServer::esTipoOfertanteJugador);

        Map<String, Jugador> allJugadoresMap = jugadoresMySQL.getMapAllJugadores();

        String mensajeOnline = GOLD + "Has cobrado " + GREEN + "%s PC" + GOLD + " en dividendo de la empresa " + nombreEmpresa;
        String mensajeOffline = GOLD + "Has cobrado %s PC en dividendo de la empresa " + nombreEmpresa;

        posicionesAccion.forEach(posicion -> {
            pagarDividendoAccionAJugador(allJugadoresMap.get(posicion.getJugador()), posicion.getCantidad(), dividendoPorAccion, nombreEmpresa);

            enviarMensaje(posicion.getJugador(), String.format(mensajeOnline, formatea.format(posicion.getCantidad() * dividendoPorAccion)), String.format(mensajeOffline, formatea.format(posicion.getCantidad() * dividendoPorAccion)));
        });
        ofertasAccion.forEach(oferta -> {
            pagarDividendoAccionAJugador(allJugadoresMap.get(oferta.getJugador()), oferta.getCantidad(), dividendoPorAccion, nombreEmpresa);

            enviarMensaje(oferta.getJugador(), String.format(mensajeOnline, formatea.format(oferta.getCantidad() * dividendoPorAccion)), String.format(mensajeOffline, formatea.format(oferta.getCantidad() * dividendoPorAccion)));
        });

        empresasMySQL.setPixelcoins(nombreEmpresa, empresasMySQL.getEmpresa(nombreEmpresa).getPixelcoins() - totalAPagar);

        enviarMensajeYSonido(owner, GOLD + "Se han pagado todos los dividendos", Sound.ENTITY_PLAYER_LEVELUP);
    }

    private void pagarDividendoAccionAJugador (Jugador jugador, int cantidad, double dividendoPorAccion, String nombreEmpresa) {
        double dividendo = cantidad * dividendoPorAccion;

        jugadoresMySQL.setEstadisticas(jugador.getNombre(), jugador.getPixelcoins() + dividendo, jugador.getNventas(), jugador.getIngresos() + dividendo, jugador.getGastos());

        Pixelcoin.publish(new EmpresaServerDividendoPagadoEvento(jugador.getNombre(), nombreEmpresa, dividendo));
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
                TipoTransaccion.valueOf(rs.getString("tipo"))
        );
    }
}
