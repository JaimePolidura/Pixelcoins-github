package es.serversurvival.pixelcoins.deudas._shared;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.javaddd.domain.exceptions.IllegalState;
import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.serversurvival.pixelcoins._shared.Validador;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Service
@AllArgsConstructor
public final class DeudasValidador {
    private final DeudasService deudasService;
    private final Validador validador;

    public void deudorDeDeuda(UUID deudaId, UUID supuestoDeudorId) {
        if(!deudasService.getById(deudaId).getDeudorJugadorId().equals(supuestoDeudorId)){
            throw new NotTheOwner("No eres el deudor de esa deuda");
        }
    }

    public void acredorDeDeuda(UUID deudaId, UUID supuestoAcredorId) {
        if(!deudasService.getById(deudaId).getAcredorJugadorId().equals(supuestoAcredorId)){
            throw new NotTheOwner("No eres el acredor de esa deuda");
        }
    }

    public void deudaPendiente(UUID deudaId) {
        if(deudasService.getById(deudaId).getEstadoDeuda() != EstadoDeuda.PENDIENTE){
            throw new IllegalState("La deuda tiene que estar pendiente");
        }
    }

    public void numeroCuotasCorrecto(int numeroCuotas) {
        validador.numeroMayorQueCero(numeroCuotas, "Numero de cuotas");
    }

    public void interesCorreto(double interes) {
        validador.numeroMayorQueCero(interes, "Interes");
    }

    public void nominalCorrecto(double nominal) {
        validador.numeroMayorQueCero(nominal, "Nominal");
    }

    public void periodoPagoCuotasCorrecto(long periodoPagoCuotas) {
        validador.numeroMayorQueCero(periodoPagoCuotas, "Periodo pago cuotas");
    }
}
