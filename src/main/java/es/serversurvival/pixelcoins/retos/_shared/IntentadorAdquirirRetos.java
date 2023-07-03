package es.serversurvival.pixelcoins.retos._shared;

import es.serversurvival.pixelcoins.retos._shared.retos.Reto;

import java.util.UUID;

public interface IntentadorAdquirirRetos {
    ResultadorIntentarAdquirirRetos intentarAdquirir(UUID jugadorId, Reto retoBase);

    double getCantidad(UUID jugadorId);
}

