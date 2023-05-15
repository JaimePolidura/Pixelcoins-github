package es.serversurvival.v1.bolsa.posicionescerradas;

import es.serversurvival.v1.bolsa.posicionescerradas._shared.domain.PosicionCerrada;
import es.serversurvival.v1._shared.utils.Funciones;
import es.serversurvival.v1.bolsa.activosinfo._shared.domain.tipoactivos.TipoActivo;
import es.serversurvival.v1.bolsa.posicionescerradas._shared.domain.TipoPosicion;

import java.util.UUID;

public final class PosicionesCerradasTestMother {
    public static PosicionCerrada createPosicionCerrada(String jugador, String nombreActivo){
        return new PosicionCerrada(UUID.randomUUID(), jugador, TipoActivo.ACCIONES, nombreActivo, 1, 1,
                Funciones.hoy(), 1, Funciones.hoy(), TipoPosicion.LARGO);
    }
}
