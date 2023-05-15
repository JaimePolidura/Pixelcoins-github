package es.serversurvival.v1.bolsa.activosinfo._shared.application;

import es.dependencyinjector.dependencies.DependenciesRepository;
import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.v1.bolsa.activosinfo._shared.domain.tipoactivos.TipoActivo;
import es.serversurvival.v1.bolsa.activosinfo._shared.domain.tipoactivos.TipoActivoService;
import es.serversurvival.v1.bolsa.activosinfo._shared.domain.tipoactivos.acciones.Dividendos;
import es.serversurvival.v1.bolsa.activosinfo._shared.domain.tipoactivos.acciones.Split;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public final class ActivoInfoDataService {
    private final DependenciesRepository dependenciesRepository;

    public double getPrecio(TipoActivo tipoActivo, String nombreActivo) throws Exception {
        Class<? extends TipoActivoService> tipoActivoServiceClass = tipoActivo.getTipoActivoService();
        TipoActivoService tipoActivoService = (TipoActivoService) dependenciesRepository.get(tipoActivoServiceClass);

        return tipoActivoService.getPrecio(nombreActivo);
    }

    public String getNombreActivoLargo(TipoActivo tipoActivo, String nombreActivo) throws Exception {
        Class<? extends TipoActivoService> tipoActivoServiceClass = tipoActivo.getTipoActivoService();
        TipoActivoService tipoActivoService = (TipoActivoService) dependenciesRepository.get(tipoActivoServiceClass);

        return tipoActivoService.getNombreActivoLargo(nombreActivo);
    }

    public Object getAccionesDividendosData(String nombreActivo) throws Exception {
        Class<? extends TipoActivoService> tipoActivoServiceClass = TipoActivo.ACCIONES.getTipoActivoService();
        Dividendos dividendos = (Dividendos) dependenciesRepository.get(tipoActivoServiceClass);

        return dividendos.getDividendosData(nombreActivo);
    }

    public Object getAccionesSplitData(String nombreActivo) throws Exception {
        Class<? extends TipoActivoService> tipoActivoServiceClass = TipoActivo.ACCIONES.getTipoActivoService();
        Split dividendos = (Split) dependenciesRepository.get(tipoActivoServiceClass);

        return dividendos.getSplitData(nombreActivo);
    }
}
