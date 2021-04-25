package es.serversurvival.legacy.mySQL;

import es.serversurvival.legacy.util.Funciones;
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
