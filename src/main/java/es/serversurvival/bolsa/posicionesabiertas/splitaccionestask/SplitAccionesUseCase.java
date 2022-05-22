package es.serversurvival.bolsa.posicionesabiertas.splitaccionestask;

import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.bolsa.activosinfo._shared.application.ActivosInfoService;
import es.serversurvival.bolsa.activosinfo._shared.domain.ActivoInfo;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.SupportedTipoActivo;
import es.serversurvival.bolsa.activosinfo._shared.infrastructure.tipoactivos.AccionesApiServiceIEXCloud;
import es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesAbiertasSerivce;
import es.serversurvival.bolsa.posicionesabiertas._shared.domain.PosicionAbierta;
import lombok.SneakyThrows;
import org.json.simple.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static es.serversurvival._shared.utils.Funciones.diferenciaDias;

public final class SplitAccionesUseCase {
    private final PosicionesAbiertasSerivce posicionesAbiertasSerivce;
    private final ActivosInfoService activoInfoService;

    public SplitAccionesUseCase() {
        this.posicionesAbiertasSerivce = DependecyContainer.get(PosicionesAbiertasSerivce.class);
        this.activoInfoService = DependecyContainer.get(ActivosInfoService.class);
    }

    public void actualizarSplits () {
        Map<String, JSONObject> infoSplitsPorAccion = new HashMap<>();
        List<ActivoInfo> todasLlamadasApi = activoInfoService.findAll(ActivoInfo::esTipoAccion);

        todasLlamadasApi.forEach( (llamada) -> {
            try {
                JSONObject infoSplit = getSplitData(llamada);
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

    private JSONObject getSplitData(ActivoInfo activoInfo) throws Exception {
        return (JSONObject) ((AccionesApiServiceIEXCloud) SupportedTipoActivo.ACCIONES.getTipoActivoService())
                .getSplitData(activoInfo.getNombreActivo());
    }

    @SneakyThrows
    private void realizarSplit(PosicionAbierta posicionAbiertaToSplit, JSONObject infoSplit) {
        Date fechaHoy = new Date();
        Date dateSplit = Funciones.DATE_FORMATER_LEGACY.parse((String) infoSplit.get("date"));

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
