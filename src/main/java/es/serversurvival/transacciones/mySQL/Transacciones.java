package es.serversurvival.transacciones.mySQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import es.serversurvival.bolsa.llamadasapi.mysql.TipoActivo;
import es.serversurvival.bolsa.ofertasmercadoserver.comprar.EmpresaServerAccionCompradaEvento;
import es.serversurvival.bolsa.ofertasmercadoserver.mysql.OfertaMercadoServer;
import es.serversurvival.bolsa.ofertasmercadoserver.mysql.TipoOfertante;
import es.serversurvival.bolsa.posicionescerradas.mysql.TipoPosicion;
import es.serversurvival.empresas.mysql.Empresa;
import es.serversurvival.jugadores.mySQL.Jugador;
import es.serversurvival.shared.mysql.MySQL;
import es.serversurvival.Pixelcoin;

import org.bukkit.entity.Player;

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

        Pixelcoin.publish(new EmpresaServerAccionCompradaEvento(player.getName(), precioTotalAPagar, cantidadAComprar, oferta, empresaAComprar.getNombre()));
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
