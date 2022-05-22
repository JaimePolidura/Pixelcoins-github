package es.serversurvival.bolsa.activosinfo.actualizar;

import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.activosinfo._shared.application.ActivosInfoService;
import es.serversurvival.bolsa.activosinfo._shared.domain.ActivoInfo;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@AllArgsConstructor
public final class ActualizarActivosInfoUseCase {
    private final ActivosInfoService activoInfoService;
    private final AtomicBoolean isLoading;

    public ActualizarActivosInfoUseCase() {
        this.activoInfoService = DependecyContainer.get(ActivosInfoService.class);
        this.isLoading = new AtomicBoolean(false);
    }

    public synchronized void actualizrar(){
        List<ActivoInfo> allActivosInfo = activoInfoService.findAll();
        this.isLoading.set(true);

        for (ActivoInfo activoInfo : allActivosInfo) {
            try {
                String nombreAcitvo = activoInfo.getNombreActivo();
                double precio = activoInfo.getTipoActivo().getTipoActivoService().getPrecio(nombreAcitvo);
                String nombreActivoLargo = activoInfo.getTipoActivo().getTipoActivoService().getNombreActivoLargo(nombreAcitvo);

                this.activoInfoService.save(activoInfo
                        .withPrecio(precio)
                        .withNombreActivoLargo(nombreActivoLargo));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.isLoading.set(false);
    }

    public boolean isLoading(){
        return this.isLoading.get();
    }
}
