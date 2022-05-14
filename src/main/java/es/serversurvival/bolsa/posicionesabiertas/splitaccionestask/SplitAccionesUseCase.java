package es.serversurvival.bolsa.posicionesabiertas.splitaccionestask;

import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.activosinfo._shared.application.ActivoInfoService;
import es.serversurvival.bolsa.activosinfo._shared.domain.ActivoInfo;
import es.serversurvival.bolsa.activosinfo.actualizar.ActualizarActivosInfoTask;
import es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesAbiertasSerivce;
import es.serversurvival.bolsa.posicionesabiertas._shared.domain.PosicionAbierta;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import es.serversurvival._shared.utils.apiHttp.IEXCloud_API;
import lombok.SneakyThrows;
import org.json.simple.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static es.serversurvival._shared.utils.Funciones.diferenciaDias;

public final class SplitAccionesUseCase implements AllMySQLTablesInstances {
    private final PosicionesAbiertasSerivce posicionesAbiertasSerivce;
    private final ActivoInfoService activoInfoService;

    public SplitAccionesUseCase() {
        this.posicionesAbiertasSerivce = DependecyContainer.get(PosicionesAbiertasSerivce.class);
        this.activoInfoService = DependecyContainer.get(ActivoInfoService.class);
    }

    public void actualizarSplits () {
        Map<String, JSONObject> infoSplitsPorAccion = new HashMap<>();
        List<ActivoInfo> todasLlamadasApi = activoInfoService.findAll(ActivoInfo::esTipoAccion);

        todasLlamadasApi.forEach( (llamada) -> {
            try {
                JSONObject infoSplit = IEXCloud_API.getSplitInfoEmpresa(llamada.getNombreActivo());
                infoSplitsPorAccion.put(llamada.getNombreActivoLargo(), infoSplit);
            } catch (Exception ignored) {
                //IGNORED
            }
        });

        List<PosicionAbierta> posicionAbiertas = posicionesAbiertasSerivce.findAll(PosicionAbierta::esTipoAccion);
        posicionAbiertas.forEach( (posicionAbierta) -> {
            JSONObject infoSplit = infoSplitsPorAccion.get(posicionAbierta.getNombreActivo());

            if(infoSplit != null){
                realizarSplit(posicionAbierta, infoSplit);
            }
        });

    }

    @SneakyThrows
    private void realizarSplit(PosicionAbierta posicionAbiertaToSplit, JSONObject infoSplit) {
        Date fechaHoy = new Date();
        Date dateSplit = dateFormater.parse((String) infoSplit.get("date"));

        int denominador = (int) infoSplit.get("fromFactor");
        int numerador = (int) infoSplit.get("toFactor");

        if (diferenciaDias(fechaHoy, dateSplit) == 0) {
            int cantidadDeAccionesConvertibles = posicionAbiertaToSplit.getCantidad() - (posicionAbiertaToSplit.getCantidad() % denominador);
            int accionesSobrantes = posicionAbiertaToSplit.getCantidad() % denominador;
            int accionesConvertidas = (cantidadDeAccionesConvertibles / denominador) * numerador;

            double precioAperturaConvertido = posicionAbiertaToSplit.getPrecioApertura() / (numerador / denominador);

            posicionesAbiertasSerivce.save(posicionAbiertaToSplit
                    .withCantidad(accionesConvertidas + accionesSobrantes)
                    .withPrecioApertura(precioAperturaConvertido));
        }
    }
}
