package es.serversurvival.bolsa.posicionesabiertas.pagardividendos;

import es.serversurvival.Pixelcoin;
import es.serversurvival.bolsa.posicionesabiertas.mysql.PosicionAbierta;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.utils.apiHttp.IEXCloud_API;
import es.serversurvival.utils.Funciones;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public final class PagarDividendosUseCase implements AllMySQLTablesInstances {
    public static final PagarDividendosUseCase INSTANCE = new PagarDividendosUseCase();

    private PagarDividendosUseCase () {}

    public void pagarDividendos() {
        Date hoy = new Date();
        List<PosicionAbierta> posicionAbiertas = posicionesAbiertasMySQL.getTodasPosicionesAbiertas().stream()
                .filter(PosicionAbierta::esLargo)
                .filter(PosicionAbierta::esTipoAccion)
                .collect(Collectors.toList());

        for(PosicionAbierta posicionAbierta : posicionAbiertas) {
            double dividendo;
            Date fechaPagoDividendos;

            try {
                JSONObject jsonDeLosDividendos = this.getJSONDividendos(posicionAbierta.getNombre_activo());
                dividendo = getCantidadDePagoDeDividendoDesdeJSON(jsonDeLosDividendos);
                fechaPagoDividendos = getFechaPagoDividendosJSON(jsonDeLosDividendos);
            } catch (Exception e) {
                continue;
            }

            if (Funciones.diferenciaDias(hoy, fechaPagoDividendos) == 0) {
                Pixelcoin.publish(new DividendoPagadoEvento(posicionAbierta.getJugador(), posicionAbierta.getNombre_activo(),
                        dividendo + posicionAbierta.getCantidad()));
            }
        }
    }

    private double getCantidadDePagoDeDividendoDesdeJSON(JSONObject json) {
        return (double) json.get("amount");
    }

    private Date getFechaPagoDividendosJSON (JSONObject json) throws ParseException {
        return dateFormater.parse((String) json.get("paymentDate"));
    }

    private JSONObject getJSONDividendos (String ticker) throws Exception {
        JSONArray jsonArray = IEXCloud_API.getDividendo(ticker, "week");
        return (JSONObject) jsonArray.get(0);
    }
}
