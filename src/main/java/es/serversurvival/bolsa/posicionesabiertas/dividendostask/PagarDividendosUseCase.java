package es.serversurvival.bolsa.posicionesabiertas.dividendostask;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.serversurvival.Pixelcoin;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.TipoActivo;
import es.serversurvival.bolsa.activosinfo._shared.infrastructure.tipoactivos.AccionesApiServiceIEXCloud;
import es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesAbiertasSerivce;
import es.serversurvival.bolsa.posicionesabiertas._shared.domain.PosicionAbierta;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import lombok.AllArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import static es.serversurvival._shared.utils.Funciones.*;

@UseCase
@AllArgsConstructor
public final class PagarDividendosUseCase {
    private final PosicionesAbiertasSerivce posicionesAbiertasSerivce;
    private final JugadoresService jugadoresService;

    public void pagarDividendos() {
        Date hoy = new Date();
        List<PosicionAbierta> posicionAbiertas = posicionesAbiertasSerivce.findAll().stream()
                .filter(PosicionAbierta::esLargo)
                .filter(PosicionAbierta::esTipoAccion)
                .toList();

        for(PosicionAbierta posicionAbierta : posicionAbiertas) {
            double dividendo;
            Date fechaPagoDividendos;

            try {
                JSONObject jsonDeLosDividendos = this.getJSONDividendos(posicionAbierta.getNombreActivo());
                dividendo = getCantidadDePagoDeDividendoDesdeJSON(jsonDeLosDividendos);
                fechaPagoDividendos = getFechaPagoDividendosJSON(jsonDeLosDividendos);
            } catch (Exception e) {
                continue;
            }

            if (diferenciaDias(hoy, fechaPagoDividendos) == 0) {
                pagarDividendo(posicionAbierta, dividendo);
            }
        }
    }

    private void pagarDividendo(PosicionAbierta posicionAbierta, double dividendo) {
        var totalDividendoInflow = dividendo * posicionAbierta.getCantidad();
        var jugadorTenedorAccion = this.jugadoresService.getByNombre(posicionAbierta.getJugador());

        this.jugadoresService.save(jugadorTenedorAccion
                .incrementPixelcoinsBy(totalDividendoInflow)
                .incrementIngresosBy(totalDividendoInflow));

        Pixelcoin.publish(new DividendoPagadoEvento(posicionAbierta.getPosicionAbiertaId(), posicionAbierta.getJugador(),
                posicionAbierta.getNombreActivo(), totalDividendoInflow));
    }

    private double getCantidadDePagoDeDividendoDesdeJSON(JSONObject json) {
        return (double) json.get("amount");
    }

    private Date getFechaPagoDividendosJSON (JSONObject json) throws ParseException {
        return DATE_FORMATER_LEGACY.parse((String) json.get("paymentDate"));
    }

    private JSONObject getJSONDividendos (String ticker) throws Exception {
        JSONArray jsonArray = (JSONArray) ((AccionesApiServiceIEXCloud) TipoActivo.ACCIONES.getTipoActivoService())
                .getDividendosData(ticker);

        return (JSONObject) jsonArray.get(0);
    }
}
