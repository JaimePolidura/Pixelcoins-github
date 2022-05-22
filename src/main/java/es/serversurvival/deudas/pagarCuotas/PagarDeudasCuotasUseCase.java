package es.serversurvival.deudas.pagarCuotas;

import es.jaime.EventBus;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.deudas._shared.application.DeudasService;
import es.serversurvival.deudas._shared.domain.Deuda;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static es.serversurvival._shared.utils.Funciones.DATE_FORMATER_LEGACY;

@AllArgsConstructor
public final class PagarDeudasCuotasUseCase {
    private final DeudasService deudasService;
    private final JugadoresService jugadoresService;
    private final EventBus eventBus;

    public PagarDeudasCuotasUseCase () {
        this.deudasService = DependecyContainer.get(DeudasService.class);
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
        this.eventBus = DependecyContainer.get(EventBus.class);
    }

    public void pagarDeudas () {
        for (Deuda deuda : this.deudasService.findAll()) {
            Date fechaHoy = formatFehcaDeHoyException();
            Date fechaUltimaPagaBaseDatos = formatFechaDeLaBaseDatosException(deuda.getFechaUltimapaga());
            Jugador deudor = this.jugadoresService.getByNombre(deuda.getDeudor());
            Jugador acredor = this.jugadoresService.getByNombre(deuda.getAcredor());

            boolean esElDiaDeLaPaga = fechaHoy.compareTo(fechaUltimaPagaBaseDatos) != 0;
            if(esElDiaDeLaPaga){
                if(deudor.getPixelcoins() >= deuda.getCouta()){
                    pagarDeudaYBorrarSiEsNecesario(deuda, acredor, deudor);
                }else{
                    sumarUnNinpagoAlDeudor(acredor, deudor, deuda.getDeudaId());
                }
            }
        }
    }

    private void pagarDeudaYBorrarSiEsNecesario(Deuda deuda, Jugador acredor, Jugador deudor) {
        String deudorNombre = deudor.getNombre();
        String acredorNombre = acredor.getNombre();
        double cuota = deuda.getCouta();
        UUID deudaId = deuda.getDeudaId();
        int tiempo = deuda.getTiempoRestante();

        if(tiempo == 1){
            this.deudasService.deleteById(deudaId);
        }else{
            deudasService.save(deuda
                    .decrementPixelcoinsRestantes(cuota)
                    .decrementTiempoRestanteByOne()
                    .withFechaUltimoPago(Funciones.hoy()));
        }

        this.jugadoresService.realizarTransferencia(
                deudor, acredor, cuota
        );
        this.jugadoresService.save(deudor.incrementNPagos());

        this.eventBus.publish(new DeudaCuotaPagadaEvento(deuda.getDeudaId(), acredorNombre, deudorNombre, cuota, tiempo - 1));
    }

    private void sumarUnNinpagoAlDeudor(Jugador acredor, Jugador deudor, UUID deudaId) {
        this.jugadoresService.save(deudor.incrementNInpago());

        this.eventBus.publish(new DeudaCuotaNoPagadaEvento(acredor.getNombre(), deudor.getNombre(), deudaId));
    }

    @SneakyThrows
    private Date formatFehcaDeHoyException () {
        DATE_FORMATER_LEGACY.parse(Funciones.hoy());

        return DATE_FORMATER_LEGACY.parse(Funciones.hoy());
    }

    @SneakyThrows
    private Date formatFechaDeLaBaseDatosException (String fecha) {
        return DATE_FORMATER_LEGACY.parse(fecha);
    }
}
