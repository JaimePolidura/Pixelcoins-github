package es.serversurvival.nfs.transacciones.mySQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import es.jaime.EventListener;
import es.serversurvival.legacy.mySQL.MySQL;
import es.serversurvival.nfs.bolsa.posicionesabiertas.mysql.PosicionAbierta;
import es.serversurvival.nfs.bolsa.posicionesabiertas.mysql.PosicionesAbiertas;
import es.serversurvival.legacy.mySQL.enums.*;
import es.serversurvival.legacy.mySQL.eventos.EventoTipoTransaccion;
import es.serversurvival.legacy.mySQL.eventos.PixelcoinsEvento;
import es.serversurvival.legacy.mySQL.eventos.bolsa.*;
import es.serversurvival.legacy.mySQL.eventos.empresas.*;
import es.serversurvival.legacy.mySQL.eventos.jugadores.JugadorPagoManualEvento;
import es.serversurvival.legacy.mySQL.eventos.tienda.ItemCompradoEvento;
import es.serversurvival.legacy.mySQL.eventos.withers.ItemIngresadoEvento;
import es.serversurvival.legacy.mySQL.eventos.withers.ItemSacadoEvento;
import es.serversurvival.legacy.mySQL.eventos.withers.ItemSacadoMaxEvento;
import es.serversurvival.legacy.mySQL.tablasObjetos.*;
import es.serversurvival.nfs.Pixelcoin;
import es.serversurvival.nfs.jugadores.mySQL.Jugador;
import es.serversurvival.nfs.empresas.comprarservicio.EmpresaServicioCompradoEvento;
import es.serversurvival.nfs.empresas.depositar.PixelcoinsDepositadasEvento;
import es.serversurvival.nfs.empresas.mysql.Empresa;
import es.serversurvival.nfs.empresas.sacar.PixelcoinsSacadasEvento;
import es.serversurvival.nfs.empresas.tasks.SueldoPagadoEvento;
import es.serversurvival.nfs.bolsa.llamadasapi.TipoActivo;
import es.serversurvival.nfs.tienda.mySQL.ofertas.Oferta;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import static es.serversurvival.legacy.mySQL.enums.CambioPixelcoins.*;
import static es.serversurvival.nfs.utils.Funciones.*;

/**
 * 792 -> 600 -> 497
 */
public final class Transacciones extends MySQL {
    public static final Transacciones INSTANCE = new Transacciones();

    @EventListener({EventoTipoTransaccion.class})
    public void onTransaction (PixelcoinsEvento event) {
        Transaccion transaccion = (((EventoTipoTransaccion) event).buildTransaccion());

        nuevaTransaccion(transaccion);
    }

    private void nuevaTransaccion(Transaccion transaccion) {
        String fecha = transaccion.getFecha();

        executeUpdate("INSERT INTO transacciones (fecha, comprador, vendedor, cantidad, objeto, tipo) VALUES ('" + fecha + "','" + transaccion.getComprador() + "','" + transaccion.getVendedor() + "','" + transaccion.getCantidad() + "','" + transaccion.getObjeto() + "','" + transaccion.getTipo().toString() + "')");
    }

    public List<Transaccion> getTransaccionesPagaEmpresa (String jugador) {
        return buildListFromQuery("SELECT * FROM transacciones WHERE comprador = '"+jugador+"' AND tipo = '"+ TipoTransaccion.EMPRESA_PAGAR_SALARIO.toString()+"'");
    }

    public void setCompradorVendedor (String jugador, String nuevoJugador) {
        executeUpdate("UPDATE transacciones SET comprador = '"+nuevoJugador+"' WHERE comprador = '"+jugador+"'");
        executeUpdate("UPDATE transacciones SET vendedor = '"+nuevoJugador+"' WHERE vendedor = '"+jugador+"'");
    }

    public void realizarVenta(String comprador, int id) {
        Oferta ofertaAComprar = ofertasMySQL.getOferta(id);
        Jugador jugadorComprador = jugadoresMySQL.getJugador(comprador);

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

        Pixelcoin.publish(new ItemCompradoEvento(vendedor, comprador, objeto, cantidad, precio));
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
        realizarTransferenciaConEstadisticas(nombrePagador, nombrePagado, cantidad, objeto, TipoTransaccion.JUGADOR_PAGO_MANUAL);

        Pixelcoin.publish(new JugadorPagoManualEvento(nombrePagador, nombrePagado, cantidad));
    }

    public void ingresarItem(ItemStack itemAIngresar, Player player) {
        Jugador jugador = jugadoresMySQL.getJugador(player.getName());

        int cantidad = itemAIngresar.getAmount();
        String nombreItem = itemAIngresar.getType().toString();
        double pixelcoinsAnadir = getCambioTotal(nombreItem, cantidad);
        double dineroActual = jugador.getPixelcoins();

        jugadoresMySQL.setPixelcoin(player.getName(), pixelcoinsAnadir + dineroActual);

        player.getInventory().clear(player.getInventory().getHeldItemSlot());

        Pixelcoin.publish(new ItemIngresadoEvento(jugador, pixelcoinsAnadir, nombreItem));
    }

    public void sacarObjeto (Jugador jugador, String itemNombre, int pixelcoinsPorItem) {
        jugadoresMySQL.setPixelcoin(jugador.getNombre(), jugador.getPixelcoins() - pixelcoinsPorItem);

        Pixelcoin.publish(new ItemSacadoEvento(jugador, itemNombre, pixelcoinsPorItem));
    }

    public void sacarMaxItem(String tipo, Jugador jugador) {
        int pixelcoinsJugador = (int) jugadoresMySQL.getJugador(jugador.getNombre()).getPixelcoins();

        CambioPixelcoins.sacarMaxItem(tipo, jugador, jugador.getNombre());
    }

    public void sacarMaxItemDiamond (Jugador jugador, String playerName) {
        int dineroJugador = (int) jugador.getPixelcoins();
        Player player = Bukkit.getPlayer(playerName);

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

        Pixelcoin.publish(new ItemSacadoMaxEvento(jugador, "DIAMOND", coste));
    }

    public void sacarMaxItemLapisLazuli (Jugador jugador, String playerName) {
        int dineroJugador = (int) jugador.getPixelcoins();
        Player player = Bukkit.getPlayer(playerName);

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

        Pixelcoin.publish(new ItemSacadoMaxEvento(jugador, "LAPIS_LAZULI", coste));
    }

    public void sacarMaxItemQuartzBlock (Jugador jugador, String playerName) {
        int pixelcoinsJugador = (int) jugador.getPixelcoins();
        Player player = Bukkit.getPlayer(playerName);

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

        Pixelcoin.publish(new ItemSacadoMaxEvento(jugador, "QUARTZ_BLOCK", coste));
    }

    public void depositarPixelcoinsEmpresa(String nombreJugador, double pixelcoins, String nombreEmpresa) {
        Jugador jugador = jugadoresMySQL.getJugador(nombreJugador);
        double pixelcoinsJugador = jugador.getPixelcoins();
        Empresa empresaADepositar = empresasMySQL.getEmpresa(nombreEmpresa);
        double pixelcoinsEmpresa = empresaADepositar.getPixelcoins();

        empresasMySQL.setPixelcoins(nombreEmpresa, pixelcoinsEmpresa + pixelcoins);
        jugadoresMySQL.setEstadisticas(nombreJugador, pixelcoinsJugador - pixelcoins, jugador.getNventas(), jugador.getIngresos(), jugador.getGastos());

        Pixelcoin.publish(new PixelcoinsDepositadasEvento(jugador, empresaADepositar, pixelcoins));
    }

    public void sacarPixelcoinsEmpresa(String playeranme, double pixelcoins, String nombreEmpresa) {
        Empresa empresaASacar = empresasMySQL.getEmpresa(nombreEmpresa);
        Jugador jugadorQueSaca = jugadoresMySQL.getJugador(playeranme);
        double pixelcoinsJugador = jugadorQueSaca.getPixelcoins();
        double pixelcoinsEmpresa = empresaASacar.getPixelcoins();

        empresasMySQL.setPixelcoins(nombreEmpresa, pixelcoinsEmpresa - pixelcoins);
        jugadoresMySQL.setEstadisticas(playeranme, pixelcoinsJugador + pixelcoins, jugadorQueSaca.getNventas(), jugadorQueSaca.getIngresos(), jugadorQueSaca.getGastos());

        Pixelcoin.publish(new PixelcoinsSacadasEvento(jugadorQueSaca, empresaASacar, pixelcoins));
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

        Pixelcoin.publish(new SueldoPagadoEvento(jugador, empresa, salario));

        return true;
    }

    public void comprarServivio(String empresa, double precio, Player player) {
        Empresa empresaAComprar = empresasMySQL.getEmpresa(empresa);
        Jugador comprador = jugadoresMySQL.getJugador(player.getName());

        jugadoresMySQL.setEstadisticas(comprador.getNombre(), comprador.getPixelcoins() - precio, comprador.getNventas(), comprador.getIngresos(), comprador.getGastos() + precio);
        empresasMySQL.setPixelcoins(empresa, empresaAComprar.getPixelcoins() + precio);
        empresasMySQL.setIngresos(empresa, empresaAComprar.getIngresos() + precio);

        Pixelcoin.publish(new EmpresaServicioCompradoEvento(comprador.getNombre(), empresaAComprar, precio));
    }

    public void comprarUnidadBolsa (TipoActivo tipoActivo, String ticker, String nombreValor, String alias, double precioUnidad, int cantidad, String nombrePlayer) {
        Player player = Bukkit.getPlayer(nombrePlayer);
        Jugador comprador = jugadoresMySQL.getJugador(nombrePlayer);
        double precioTotal = precioUnidad * cantidad;

        jugadoresMySQL.setPixelcoin(nombrePlayer, comprador.getPixelcoins() - precioTotal);
        posicionesAbiertasMySQL.nuevaPosicion(nombrePlayer, tipoActivo, ticker, cantidad, precioUnidad, TipoPosicion.LARGO);

        Pixelcoin.publish(new PosicionCompraLargoEvento(comprador.getNombre(), precioUnidad, cantidad, cantidad*precioUnidad, ticker, tipoActivo, nombreValor, alias));
    }

    public void venderEnCortoBolsa (String playerName, String ticker, String nombreValor, int cantidad, double precioPorAccion) {
        Jugador jugador = jugadoresMySQL.getJugador(playerName);
        double valorTotal = precioPorAccion * cantidad;
        double comision = redondeoDecimales(reducirPorcentaje(valorTotal, 100 - PosicionesAbiertas.PORCENTAJE_CORTO), 2);

        jugadoresMySQL.setEstadisticas(playerName, jugador.getPixelcoins() - comision, jugador.getNventas(), jugador.getIngresos(), jugador.getGastos() + comision);
        posicionesAbiertasMySQL.nuevaPosicion(playerName, TipoActivo.ACCIONES, ticker, cantidad, precioPorAccion, TipoPosicion.CORTO);

        Pixelcoin.publish(new PosicionVentaCortoEvento(playerName, precioPorAccion, cantidad, comision, ticker, TipoActivo.ACCIONES, nombreValor));
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

        Pixelcoin.publish(new PosicionVentaLargoEvento(nombreJugador, ticker, nombreValor, precioApertura, fechaApertura, precioPorAccion, cantidad, rentabilidad, posicionAVender.getTipo_activo()));
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

        Pixelcoin.publish(new PosicionCompraCortoEvento(playername, ticker, nombreValor, precioApertura, fechaApertura, precioPorAccion, cantidad, rentabilidad, TipoActivo.ACCIONES));
    }

    public void pagaDividendo(String ticker, String nombre, double precioDividendo, int nAcciones) {
        double aPagar = precioDividendo * nAcciones;

        jugadoresMySQL.setPixelcoin(nombre, jugadoresMySQL.getJugador(nombre).getPixelcoins() + aPagar);

        Pixelcoin.publish(new DividendoPagadoEvento(nombre, ticker, aPagar));
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
        Empresa empresaAComprar = empresasMySQL.getEmpresa(oferta.getEmpresa());
        Jugador jugadorComprador = jugadoresMySQL.getJugador(player.getName());
        double precioTotalAPagar = oferta.getPrecio() * cantidadAComprar;

        jugadoresMySQL.setPixelcoin(jugadorComprador.getNombre(), jugadorComprador.getPixelcoins() - precioTotalAPagar);
        posicionesAbiertasMySQL.nuevaPosicion(jugadorComprador.getNombre(), TipoActivo.ACCIONES_SERVER, oferta.getEmpresa(), cantidadAComprar, oferta.getPrecio(), TipoPosicion.LARGO);
        ofertasMercadoServerMySQL.setCantidadOBorrar(idOfeta, oferta.getCantidad() - cantidadAComprar);

        if(oferta.getTipo_ofertante() == TipoOfertante.EMPRESA){
            comprarAccionServerAEmpresa(player, oferta, cantidadAComprar, precioTotalAPagar);
        }else{
            comprarAccionServerAJugador(player, oferta, cantidadAComprar, precioTotalAPagar);
        }

        Pixelcoin.publish(new EmpresaServerAccionCompradaEvento(player.getName(), precioTotalAPagar, cantidadAComprar, oferta, empresaAComprar));
    }
    
    private void comprarAccionServerAEmpresa (Player player, OfertaMercadoServer oferta, int cantidadAComprar, double precioTotalAPagar) {
        Empresa empresa = empresasMySQL.getEmpresa(oferta.getEmpresa());

        empresasMySQL.setPixelcoins(empresa.getNombre(), empresa.getPixelcoins() + precioTotalAPagar);
        empresasMySQL.setIngresos(empresa.getNombre(), empresa.getIngresos() + precioTotalAPagar);
    }

    private void comprarAccionServerAJugador (Player player, OfertaMercadoServer oferta, int cantidadAComprar, double precioTotalAPagar) {
        Jugador jugadorVendedor = jugadoresMySQL.getJugador(oferta.getJugador());
        double beneficiosPerdidas = (oferta.getPrecio() - oferta.getPrecio_apertura()) * cantidadAComprar;
        double rentabilidad = redondeoDecimales(diferenciaPorcntual(oferta.getPrecio_apertura(), oferta.getPrecio()), 3);

        if(beneficiosPerdidas >= 0)
            jugadoresMySQL.setEstadisticas(jugadorVendedor.getNombre(), jugadorVendedor.getPixelcoins() + precioTotalAPagar, jugadorVendedor.getNventas(), jugadorVendedor.getIngresos(), jugadorVendedor.getGastos() + beneficiosPerdidas);
        else
            jugadoresMySQL.setEstadisticas(jugadorVendedor.getNombre(), jugadorVendedor.getPixelcoins() + precioTotalAPagar, jugadorVendedor.getNventas(), jugadorVendedor.getIngresos() + beneficiosPerdidas, jugadorVendedor.getGastos());
    }

    public void pagarDividendoAccionServer (Player owner, String nombreEmpresa, double dividendoPorAccion, double totalAPagar) {
        List<PosicionAbierta> posicionesAccion = posicionesAbiertasMySQL.getPosicionesAccionesServer(nombreEmpresa);
        List<OfertaMercadoServer> ofertasAccion =  ofertasMercadoServerMySQL.getOfertasEmpresa(nombreEmpresa, OfertaMercadoServer::esTipoOfertanteJugador);

        Map<String, Jugador> allJugadoresMap = jugadoresMySQL.getMapAllJugadores();

        posicionesAccion.forEach(posicion -> {
            pagarDividendoAccionAJugador(allJugadoresMap.get(posicion.getJugador()), posicion.getCantidad(), dividendoPorAccion, nombreEmpresa);
        });
        ofertasAccion.forEach(oferta -> {
            pagarDividendoAccionAJugador(allJugadoresMap.get(oferta.getJugador()), oferta.getCantidad(), dividendoPorAccion, nombreEmpresa);
        });

        empresasMySQL.setPixelcoins(nombreEmpresa, empresasMySQL.getEmpresa(nombreEmpresa).getPixelcoins() - totalAPagar);
    }

    private void pagarDividendoAccionAJugador (Jugador jugador, int cantidad, double dividendoPorAccion, String nombreEmpresa) {
        double dividendo = cantidad * dividendoPorAccion;

        jugadoresMySQL.setEstadisticas(jugador.getNombre(), jugador.getPixelcoins() + dividendo, jugador.getNventas(), jugador.getIngresos() + dividendo, jugador.getGastos());

        Pixelcoin.publish(new EmpresaServerDividendoPagadoEvento(jugador.getNombre(), nombreEmpresa, dividendo));
    }

    @Override
    protected TablaObjeto buildObjectFromResultSet(ResultSet rs) throws SQLException {
        return null;
    }
}
