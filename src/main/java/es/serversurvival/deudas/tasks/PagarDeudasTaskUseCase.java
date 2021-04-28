package es.serversurvival.deudas.tasks;

import es.serversurvival.deudas.mysql.Deuda;
import es.serversurvival.jugadores.mySQL.Jugador;
import es.serversurvival.jugadores.mySQL.Jugadores;
import es.serversurvival.Pixelcoin;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.transacciones.mySQL.TipoTransaccion;
import lombok.SneakyThrows;

import java.util.Date;
import java.util.List;
import java.util.Map;

public final class PagarDeudasTaskUseCase implements AllMySQLTablesInstances {
    public void pagarDeudas () {
        List<Deuda> todasLasDeudas = deudasMySQL.getAllDeudas();
        Map<String, Jugador> allJugadores = Jugadores.INSTANCE.getMapAllJugadores();

        todasLasDeudas.forEach( (deuda) -> {
            Date fechaHoy = formatFehcaDeHoyException();
            Date fechaUltimaPagaBaseDatos = formatFechaDeLaBaseDatosException(deuda.getFecha_ultimapaga());
            Jugador deudor = allJugadores.get(deuda.getDeudor());
            Jugador acredor = allJugadores.get(deuda.getAcredor());

            boolean esElDiaDeLaPaga = fechaHoy.compareTo(fechaUltimaPagaBaseDatos) != 0;
            if(esElDiaDeLaPaga){
                if(deudor.getPixelcoins() >= deuda.getCouta()){
                    pagarDeudaYBorrarSiEsNecesario(deuda, acredor, deudor);
                }else{
                    sumarUnNinpagoYEnviarMensajeAlAcredor(acredor, deudor, deuda.getId());
                }
            }
        });
    }

    private void pagarDeudaYBorrarSiEsNecesario (Deuda deuda, Jugador acredor, Jugador deudor) {
        String deudorNombre = deudor.getNombre();
        String acredorNombre = acredor.getNombre();
        int cuota = deuda.getCouta();
        int id = deuda.getId();
        int tiempo = deuda.getTiempo_restante();
        int pixelcoinsDeuda = deuda.getPixelcoins_restantes();

        transaccionesMySQL.realizarTransferenciaConEstadisticas(deudorNombre, acredorNombre, cuota, "", TipoTransaccion.DEUDAS_PAGAR_CUOTA);

        if(tiempo == 1){
            deudasMySQL.borrarDeuda(id);
        }else{
            deudasMySQL.setPagoDeuda(id, pixelcoinsDeuda - cuota, tiempo - 1, dateFormater.format(formatFehcaDeHoyException()));
        }

        Pixelcoin.publish(new DeudaCuotaPagadaEvento(deuda.getId(), acredorNombre, deudorNombre, cuota, tiempo - 1));
    }

    private void sumarUnNinpagoYEnviarMensajeAlAcredor (Jugador acredor, Jugador deudor, int id) {
        Pixelcoin.publish(new DeudaCuotaNoPagadaEvento(acredor.getNombre(), deudor.getNombre(), id));
    }

    @SneakyThrows
    private Date formatFehcaDeHoyException () {
        return dateFormater.parse(dateFormater.format(new Date()));
    }

    @SneakyThrows
    private Date formatFechaDeLaBaseDatosException (String fecha) {
        return dateFormater.parse(fecha);
    }
}
