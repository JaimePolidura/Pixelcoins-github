package es.serversurvival.bolsa.activosinfo.vervalores;

import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.TipoActivo;
import es.serversurvival.jugadores._shared.domain.Jugador;

public record ComprarBolsaConfirmacionMenuState(
        Jugador jugador,
        String nombreActivo,
        double precioUnidad,
        TipoActivo tipoActivo) {
}
