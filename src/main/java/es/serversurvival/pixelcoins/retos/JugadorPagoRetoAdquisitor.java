package es.serversurvival.pixelcoins.retos;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.pixelcoins.retos._shared.IntentadorAdquirirRetos;
import es.serversurvival.pixelcoins.retos._shared.ResultadorIntentarAdquirirRetos;
import es.serversurvival.pixelcoins.retos._shared.retos.Reto;

import java.util.UUID;

@Service
public final class JugadorPagoRetoAdquisitor implements IntentadorAdquirirRetos {
    @Override
    public ResultadorIntentarAdquirirRetos intentarAdquirir(UUID jugadorId, Reto reto) {
        return null;
    }

    @Override
    public double getCantidad(UUID jugadorId) {
        return 0;
    }
}
