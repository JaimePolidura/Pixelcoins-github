package es.serversurvival.deudas.pagarCuotas;

import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.deudas._shared.application.DeudasService;
import es.serversurvival.deudas._shared.domain.Deuda;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;
import es.serversurvival.Pixelcoin;
import lombok.SneakyThrows;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static es.serversurvival._shared.utils.Funciones.DATE_FORMATER_LEGACY;


public final class PagarDeudasCuotasUseCase {
    private final DeudasService deudasService;
    private final JugadoresService jugadoresService;

    public PagarDeudasCuotasUseCase () {
        this.deudasService = DependecyContainer.get(DeudasService.class);
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
    }

    public void pagarDeudas () {
        List<Deuda> todasLasDeudas = this.deudasService.findAll();
        Map<String, Jugador> allJugadores = DependecyContainer.get(JugadoresService.class).getMapAllJugadores();

        todasLasDeudas.forEach( (deuda) -> {
            Date fechaHoy = formatFehcaDeHoyException();
            Date fechaUltimaPagaBaseDatos = formatFechaDeLaBaseDatosException(deuda.getFechaUltimapaga());
            Jugador deudor = allJugadores.get(deuda.getDeudor());
            Jugador acredor = allJugadores.get(deuda.getAcredor());

            boolean esElDiaDeLaPaga = fechaHoy.compareTo(fechaUltimaPagaBaseDatos) != 0;
            if(esElDiaDeLaPaga){
                if(deudor.getPixelcoins() >= deuda.getCouta()){
                    pagarDeudaYBorrarSiEsNecesario(deuda, acredor, deudor);
                }else{
                    sumarUnNinpagoYEnviarMensajeAlAcredor(acredor, deudor, deuda.getDeudaId());
                }
            }
        });
    }

    private void pagarDeudaYBorrarSiEsNecesario(Deuda deuda, Jugador acredor, Jugador deudor) {
        String deudorNombre = deudor.getNombre();
        String acredorNombre = acredor.getNombre();
        int cuota = deuda.getCouta();
        UUID deudaId = deuda.getDeudaId();
        int tiempo = deuda.getTiempoRestante();

        if(tiempo == 1){
            this.deudasService.deleteById(deudaId);
        }else{
            deudasService.save(deuda
                    .decrementPixelcoinsRestantes(cuota)
                    .decrementTiempoRestanteByOne()
                    .withFechaUltimoPago(formatFehcaDeHoyException().toString()));
        }

        this.jugadoresService.realizarTransferenciaConEstadisticas(
                deudor, acredor, cuota
        );
        this.jugadoresService.save(deudor.incrementNPagos());


        Pixelcoin.publish(new DeudaCuotaPagadaEvento(deuda.getDeudaId(), acredorNombre, deudorNombre, cuota, tiempo - 1));
    }

    private void sumarUnNinpagoYEnviarMensajeAlAcredor (Jugador acredor, Jugador deudor, UUID deudaId) {
        this.jugadoresService.save(deudor.incrementNInpago());

        Pixelcoin.publish(new DeudaCuotaNoPagadaEvento(acredor.getNombre(), deudor.getNombre(), deudaId));
    }

    @SneakyThrows
    private Date formatFehcaDeHoyException () {
        return DATE_FORMATER_LEGACY.parse(DATE_FORMATER_LEGACY.format(new Date()));
    }

    @SneakyThrows
    private Date formatFechaDeLaBaseDatosException (String fecha) {
        return DATE_FORMATER_LEGACY.parse(fecha);
    }
}
