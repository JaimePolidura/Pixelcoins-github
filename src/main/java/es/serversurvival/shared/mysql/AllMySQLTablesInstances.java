package es.serversurvival.shared.mysql;

import es.serversurvival.bolsa.llamadasapi.mysql.LlamadasApi;
import es.serversurvival.bolsa.ofertasmercadoserver.mysql.OfertasMercadoServer;
import es.serversurvival.bolsa.ordenespremarket.mysql.OrdenesPreMarket;
import es.serversurvival.bolsa.posicionesabiertas.mysql.PosicionesAbiertas;
import es.serversurvival.bolsa.posicionescerradas.mysql.PosicionesCerradas;
import es.serversurvival.deudas.mysql.Deudas;
import es.serversurvival.empleados.mysql.Empleados;
import es.serversurvival.empresas.mysql.Empresas;
import es.serversurvival.jugadores.mySQL.Jugadores;
import es.serversurvival.mensajes.mysql.Mensajes;
import es.serversurvival.utils.Funciones;
import es.serversurvival.tienda.mySQL.encantamientos.Encantamientos;
import es.serversurvival.tienda.mySQL.ofertas.Ofertas;
import es.serversurvival.transacciones.mySQL.Transacciones;
import es.serversurvival.webconnection.conversacionesweb.mysql.ConversacionesWeb;
import es.serversurvival.cuentaweb.Cuentas;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public interface AllMySQLTablesInstances {
    SimpleDateFormat dateFormater = Funciones.DATE_FORMATER_LEGACY;
    DecimalFormat formatea = Funciones.FORMATEA;

    Cuentas cuentasMySQL = Cuentas.INSTANCE;
    Empleados empleadosMySQL = Empleados.INSTANCE;
    Empresas empresasMySQL = Empresas.INSTANCE;
    Encantamientos encantamientosMySQL = Encantamientos.INSTANCE;
    Jugadores jugadoresMySQL = Jugadores.INSTANCE;
    LlamadasApi llamadasApiMySQL = LlamadasApi.INSTANCE;
    Mensajes mensajesMySQL = Mensajes.INSTANCE;
    Ofertas ofertasMySQL = Ofertas.INSTANCE;
    PosicionesAbiertas posicionesAbiertasMySQL = PosicionesAbiertas.INSTANCE;
    PosicionesCerradas posicionesCerradasMySQL = PosicionesCerradas.INSTANCE;
    Transacciones transaccionesMySQL = Transacciones.INSTANCE;
    Deudas deudasMySQL = Deudas.INSTANCE;
    OrdenesPreMarket ordenesMySQL = OrdenesPreMarket.INSTANCE;
    OfertasMercadoServer ofertasMercadoServerMySQL = OfertasMercadoServer.INSTANCE;
    ConversacionesWeb conversacionesWebMySQL = ConversacionesWeb.INSTANCE;
}
