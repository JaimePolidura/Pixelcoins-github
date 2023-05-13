package es.serversurvival.bolsa.activosinfo.actualizar;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.serversurvival.bolsa.activosinfo._shared.application.ActivosInfoService;
import es.serversurvival.bolsa.activosinfo._shared.domain.ActivoInfo;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@UseCase
@RequiredArgsConstructor
public final class ActualizarActivosInfoUseCase {
    private final ActivosInfoService activoInfoService;
    private AtomicBoolean isLoading = new AtomicBoolean(false);

    public synchronized void actualizar(){
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
