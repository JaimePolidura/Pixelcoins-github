package es.serversurvival.mySQL;

import es.serversurvival.util.Funciones;

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
