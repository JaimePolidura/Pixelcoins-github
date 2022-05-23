package es.serversurvival.bolsa;

import es.serversurvival.bolsa.posicionesabiertas._shared.domain.PosicionAbierta;

import java.util.UUID;

import static es.serversurvival._shared.utils.Funciones.*;
import static es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.SupportedTipoActivo.ACCIONES;
import static es.serversurvival.bolsa.posicionescerradas._shared.domain.TipoPosicion.*;

public final class PosicionesAbiertasTestMother {
    public static PosicionAbierta createPosicionAbierta(String jugaodr, String nombreActivo){
        return new PosicionAbierta(UUID.randomUUID(), jugaodr, ACCIONES, nombreActivo, 1, 1, hoy(), LARGO);
    }
}
