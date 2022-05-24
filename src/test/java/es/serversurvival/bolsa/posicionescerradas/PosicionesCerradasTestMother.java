package es.serversurvival.bolsa.posicionescerradas;

import es.serversurvival.bolsa.posicionescerradas._shared.domain.PosicionCerrada;

import java.util.UUID;

import static es.serversurvival._shared.utils.Funciones.*;
import static es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.SupportedTipoActivo.*;
import static es.serversurvival.bolsa.posicionescerradas._shared.domain.TipoPosicion.*;

public final class PosicionesCerradasTestMother {
    public static PosicionCerrada createPosicionCerrada(String jugador, String nombreActivo){
        return new PosicionCerrada(UUID.randomUUID(), jugador, ACCIONES, nombreActivo, 1, 1,
                hoy(), 1, hoy(), LARGO);
    }
}
