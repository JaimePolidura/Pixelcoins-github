package es.serversurvival._shared.mysql;

import es.serversurvival.bolsa._shared.llamadasapi.mysql.LlamadasApi;
import es.serversurvival.bolsa._shared.ofertasmercadoserver.mysql.OfertasMercadoServer;
import es.serversurvival.bolsa._shared.ordenespremarket.mysql.OrdenesPreMarket;
import es.serversurvival.bolsa._shared.posicionesabiertas.mysql.PosicionesAbiertas;
import es.serversurvival.bolsa._shared.posicionescerradas.mysql.PosicionesCerradas;
import es.serversurvival.deudas._shared.mysql.Deudas;
import es.serversurvival.empleados._shared.mysql.Empleados;
import es.serversurvival.empresas._shared.mysql.Empresas;
import es.serversurvival.jugadores._shared.mySQL.MySQLJugadoresRepository;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.tienda._shared.mySQL.encantamientos.Encantamientos;
import es.serversurvival.tienda._shared.mySQL.ofertas.Ofertas;
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
    MySQLJugadoresRepository jugadoresMySQL = MySQLJugadoresRepository.INSTANCE;
    LlamadasApi llamadasApiMySQL = LlamadasApi.INSTANCE;
    PosicionesAbiertas posicionesAbiertasMySQL = PosicionesAbiertas.INSTANCE;
    PosicionesCerradas posicionesCerradasMySQL = PosicionesCerradas.INSTANCE;
    Deudas deudasMySQL = Deudas.INSTANCE;
    OrdenesPreMarket ordenesMySQL = OrdenesPreMarket.INSTANCE;
    OfertasMercadoServer ofertasMercadoServerMySQL = OfertasMercadoServer.INSTANCE;
    ConversacionesWeb conversacionesWebMySQL = ConversacionesWeb.INSTANCE;
}
