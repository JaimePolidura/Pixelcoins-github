package es.serversurvival.nfs.shared.mysql;

import es.serversurvival.nfs.bolsa.ofertasmercadoserver.mysql.OfertasMercadoServer;
import es.serversurvival.nfs.bolsa.ordenespremarket.mysql.OrdenesPreMarket;
import es.serversurvival.nfs.bolsa.llamadasapi.mysql.LlamadasApi;
import es.serversurvival.nfs.bolsa.posicionescerradas.mysql.PosicionesCerradas;
import es.serversurvival.nfs.utils.Funciones;
import es.serversurvival.nfs.deudas.mysql.Deudas;
import es.serversurvival.nfs.empleados.mysql.Empleados;
import es.serversurvival.nfs.empresas.mysql.Empresas;
import es.serversurvival.nfs.jugadores.mySQL.Jugadores;
import es.serversurvival.nfs.bolsa.posicionesabiertas.mysql.PosicionesAbiertas;
import es.serversurvival.nfs.tienda.mySQL.encantamientos.Encantamientos;
import es.serversurvival.nfs.tienda.mySQL.ofertas.Ofertas;
import es.serversurvival.nfs.transacciones.mySQL.Transacciones;
import es.serversurvival.nfs.webconnection.conversacionesweb.mysql.ConversacionesWeb;
import es.serversurvival.nfs.cuentaweb.Cuentas;
import es.serversurvival.nfs.mensajes.mysql.Mensajes;

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
