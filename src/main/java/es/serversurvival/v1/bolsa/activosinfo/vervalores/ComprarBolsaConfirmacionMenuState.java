package es.serversurvival.v1.bolsa.activosinfo.vervalores;

import es.serversurvival.v1.bolsa.activosinfo._shared.domain.tipoactivos.TipoActivo;
import es.serversurvival.v1.jugadores._shared.domain.Jugador;

public record ComprarBolsaConfirmacionMenuState(
        Jugador jugador,
        String nombreActivo,
        double precioUnidad,
        TipoActivo tipoActivo) {
}
