package es.serversurvival.bolsa.other._shared.llamadasapi.actualizarprecios;

import es.serversurvival.bolsa.activosinfo._shared.domain.ActivoInfo;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;

import java.util.List;

public final class ActualizarPreciosUseCase implements AllMySQLTablesInstances {
    public static final ActualizarPreciosUseCase INSTANCE = new ActualizarPreciosUseCase();

    private ActualizarPreciosUseCase () {}

    public synchronized void actualizarPrecios (){
        List<ActivoInfo> llamadaApis = llamadasApiMySQL.getTodasLlamadasApi();

        for (ActivoInfo llamadaApi : llamadaApis) {
            double precio = llamadaApi.getTipoActivo().getPrecio(llamadaApi.getNombreActivo());

            llamadasApiMySQL.setPrecio(llamadaApi.getNombreActivo(), precio);
        }
    }
}
