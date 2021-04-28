package es.serversurvival.transacciones.mySQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import es.serversurvival.bolsa.llamadasapi.mysql.TipoActivo;
import es.serversurvival.bolsa.ofertasmercadoserver.comprar.EmpresaServerAccionCompradaEvento;
import es.serversurvival.bolsa.ofertasmercadoserver.mysql.OfertaMercadoServer;
import es.serversurvival.bolsa.ofertasmercadoserver.mysql.TipoOfertante;
import es.serversurvival.bolsa.posicionesabiertas.comprarlargo.PosicionCompraLargoEvento;
import es.serversurvival.bolsa.posicionesabiertas.mysql.PosicionAbierta;
import es.serversurvival.bolsa.posicionesabiertas.vendercorto.PosicionVentaCortoEvento;
import es.serversurvival.bolsa.posicionesabiertas.venderlargo.PosicionVentaLargoEvento;
import es.serversurvival.bolsa.posicionescerradas.mysql.TipoPosicion;
import es.serversurvival.empresas.comprarservicio.EmpresaServicioCompradoEvento;
import es.serversurvival.empresas.depositar.PixelcoinsDepositadasEvento;
import es.serversurvival.empresas.mysql.Empresa;
import es.serversurvival.empresas.sacar.PixelcoinsSacadasEvento;
import es.serversurvival.jugadores.mySQL.Jugador;
import es.serversurvival.jugadores.pagar.JugadorPagoManualEvento;
import es.serversurvival.jugadores.withers.ingresarItem.ItemIngresadoEvento;
import es.serversurvival.jugadores.withers.sacarItem.ItemSacadoEvento;
import es.serversurvival.jugadores.withers.sacarMaxItem.ItemSacadoMaxEvento;
import es.serversurvival.shared.mysql.MySQL;
import es.serversurvival.bolsa.posicionesabiertas.mysql.PosicionesAbiertas;
import es.serversurvival.tienda.comprar.ItemCompradoEvento;
import es.serversurvival.Pixelcoin;
import es.serversurvival.jugadores.withers.CambioPixelcoins;
import es.serversurvival.shared.mysql.TablaObjeto;
import es.serversurvival.tienda.mySQL.ofertas.Oferta;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import static es.serversurvival.jugadores.withers.CambioPixelcoins.*;
import static es.serversurvival.utils.Funciones.*;

/**
 * 792 -> 600 -> 497
 */
public final class Transacciones extends MySQL {
    public static final Transacciones INSTANCE = new Transacciones();

    public void nuevaTransaccion(Transaccion transaccion) {
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

    public void sacarObjeto (Jugador jugador, String itemNombre, int pixelcoinsPorItem) {
        jugadoresMySQL.setPixelcoin(jugador.getNombre(), jugador.getPixelcoins() - pixelcoinsPorItem);

        Pixelcoin.publish(new ItemSacadoEvento(jugador, itemNombre, pixelcoinsPorItem));
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
