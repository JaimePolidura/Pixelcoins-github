package es.serversurvival.pixelcoins._shared;

import es.jaime.Event;

import java.util.UUID;

public interface EventoTipoTransaccion extends Event {
    UUID pagadoId();
    UUID pagadorId();
}
