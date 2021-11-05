package es.serversurvival.bolsa._shared.posicionesabiertas.splitAcciones;

import es.serversurvival.bolsa._shared.llamadasapi.mysql.LlamadaApi;
import es.serversurvival.bolsa._shared.posicionesabiertas.mysql.PosicionAbierta;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.shared.utils.apiHttp.IEXCloud_API;
import lombok.SneakyThrows;
import org.json.simple.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static es.serversurvival.shared.utils.Funciones.diferenciaDias;

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
            JSONObject infoSplit = infoSplitsPorAccion.get(posicionAbierta.getNombre_activo());

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

            double precioAperturaConvertido = pos.getPrecio_apertura() / (numerador / denominador);

            posicionesAbiertasMySQL.setCantidad(pos.getId(), accionesConvertidas + accionesSobrantes);
            posicionesAbiertasMySQL.setPrecioApertura(pos.getId(), precioAperturaConvertido);
        }
    }
}
