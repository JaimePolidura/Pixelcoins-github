package es.serversurvival._shared.mysql;

import es.serversurvival.bolsa.other._shared.llamadasapi.mysql.LlamadasApi;
import es.serversurvival.bolsa.other._shared.ofertasmercadoserver.mysql.OfertasMercadoServer;
import es.serversurvival._shared.utils.Funciones;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public interface AllMySQLTablesInstances {
    SimpleDateFormat dateFormater = Funciones.DATE_FORMATER_LEGACY;
    DecimalFormat formatea = Funciones.FORMATEA;

    LlamadasApi llamadasApiMySQL = LlamadasApi.INSTANCE;
    OfertasMercadoServer ofertasMercadoServerMySQL = OfertasMercadoServer.INSTANCE;
}
