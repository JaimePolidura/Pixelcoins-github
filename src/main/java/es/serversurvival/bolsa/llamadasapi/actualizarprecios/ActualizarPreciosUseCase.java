package es.serversurvival.bolsa.llamadasapi.actualizarprecios;

import es.serversurvival.bolsa.llamadasapi.mysql.LlamadaApi;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;

import java.util.List;

public final class ActualizarPreciosUseCase implements AllMySQLTablesInstances {
    public static final ActualizarPreciosUseCase INSTANCE = new ActualizarPreciosUseCase();

    private ActualizarPreciosUseCase () {}

    public synchronized void actualizarPrecios (){
        List<LlamadaApi> llamadaApis = llamadasApiMySQL.getTodasLlamadasApi();

        for (LlamadaApi llamadaApi : llamadaApis) {
            double precio = llamadaApi.getTipo_activo().getPrecio(llamadaApi.getSimbolo());

            llamadasApiMySQL.setPrecio(llamadaApi.getSimbolo(), precio);
        }
    }
}
