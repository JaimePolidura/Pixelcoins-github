package es.serversurvival.bolsa.activosinfo.actualizar;

import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.activosinfo._shared.application.ActivoInfoService;
import es.serversurvival.bolsa.activosinfo._shared.domain.ActivoInfo;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public final class ActualizarPreciosUseCase implements AllMySQLTablesInstances {
    private final ActivoInfoService activoInfoService;
    private final AtomicBoolean isLoading;

    public ActualizarPreciosUseCase() {
        this.activoInfoService = DependecyContainer.get(ActivoInfoService.class);
        this.isLoading = new AtomicBoolean(false);
    }

    public synchronized void actualizrar(){
        List<ActivoInfo> allActivosInfo = activoInfoService.findAll();
        this.isLoading.set(true);

        for (ActivoInfo activoInfo : allActivosInfo) {
            String nombreAcitvo = activoInfo.getNombreActivo();
            double precio = activoInfo.getTipoActivo().getTipoActivoService().getPrecio(nombreAcitvo);
            String nombreActivoLargo = activoInfo.getTipoActivo().getTipoActivoService().getNombreActivoLargo(nombreAcitvo);

            this.activoInfoService.save(activoInfo
                    .withPrecio(precio)
                    .withNombreActivoLargo(nombreActivoLargo));
        }

        this.isLoading.set(false);
    }

    public boolean isLoading(){
        return this.isLoading.get();
    }
}
