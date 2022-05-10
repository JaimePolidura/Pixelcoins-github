package es.serversurvival._shared.mysql;

import es.serversurvival.bolsa._shared.llamadasapi.mysql.LlamadasApi;
import es.serversurvival.bolsa._shared.ofertasmercadoserver.mysql.OfertasMercadoServer;
import es.serversurvival.bolsa._shared.ordenespremarket.mysql.OrdenesPreMarket;
import es.serversurvival.bolsa._shared.posicionesabiertas.mysql.PosicionesAbiertas;
import es.serversurvival.bolsa._shared.posicionescerradas.mysql.PosicionesCerradas;
import es.serversurvival._shared.utils.Funciones;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public interface AllMySQLTablesInstances {
    SimpleDateFormat dateFormater = Funciones.DATE_FORMATER_LEGACY;
    DecimalFormat formatea = Funciones.FORMATEA;

    LlamadasApi llamadasApiMySQL = LlamadasApi.INSTANCE;
    PosicionesAbiertas posicionesAbiertasMySQL = PosicionesAbiertas.INSTANCE;
    PosicionesCerradas posicionesCerradasMySQL = PosicionesCerradas.INSTANCE;
    OrdenesPreMarket ordenesMySQL = OrdenesPreMarket.INSTANCE;
    OfertasMercadoServer ofertasMercadoServerMySQL = OfertasMercadoServer.INSTANCE;
}
