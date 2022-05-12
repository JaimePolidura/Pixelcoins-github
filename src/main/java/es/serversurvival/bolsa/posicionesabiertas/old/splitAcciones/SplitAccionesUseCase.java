package es.serversurvival.bolsa.posicionesabiertas.old.splitAcciones;

import es.serversurvival.bolsa.other._shared.llamadasapi.mysql.LlamadaApi;
import es.serversurvival.bolsa.posicionesabiertas._shared.newformat.domain.PosicionAbierta;
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
    public static final SplitAccionesUseCase INSTANCE = new SplitAccionesUseCase();

    private SplitAccionesUseCase () {}

    public void actualizarSplits () {
        Map<String, JSONObject> infoSplitsPorAccion = new HashMap<>();
        List<LlamadaApi> todasLlamadasApi = llamadasApiMySQL.getTodasLlamadasApiCondicion(LlamadaApi::esTipoAccion);

        todasLlamadasApi.forEach( (llamada) -> {
            try {
                JSONObject infoSplit = IEXCloud_API.getSplitInfoEmpresa(llamada.getSimbolo());
                infoSplitsPorAccion.put(llamada.getNombre_activo(), infoSplit);
            } catch (Exception ignored) {
                //IGNORED
            }
        });

        List<PosicionAbierta> posicionAbiertas = posicionesAbiertasMySQL.getTodasPosicionesAbiertasCondicion(PosicionAbierta::esTipoAccion);
        posicionAbiertas.forEach( (posicionAbierta) -> {
            JSONObject infoSplit = infoSplitsPorAccion.get(posicionAbierta.getNombreActivo());

            if(infoSplit != null){
                realizarSplit(posicionAbierta, infoSplit);
            }
        });

    }

    @SneakyThrows
    private void realizarSplit(PosicionAbierta pos, JSONObject infoSplit) {
        Date fechaHoy = new Date();
        Date dateSplit = dateFormater.parse((String) infoSplit.get("date"));

        int denominador = (int) infoSplit.get("fromFactor");
        int numerador = (int) infoSplit.get("toFactor");

        if (diferenciaDias(fechaHoy, dateSplit) == 0) {
            int cantidadDeAccionesConvertibles = pos.getCantidad() - (pos.getCantidad() % denominador);
            int accionesSobrantes = pos.getCantidad() % denominador;
            int accionesConvertidas = (cantidadDeAccionesConvertibles / denominador) * numerador;

            double precioAperturaConvertido = pos.getPrecioApertura() / (numerador / denominador);

            posicionesAbiertasMySQL.setCantidad(pos.getId(), accionesConvertidas + accionesSobrantes);
            posicionesAbiertasMySQL.setPrecioApertura(pos.getId(), precioAperturaConvertido);
        }
    }
}
