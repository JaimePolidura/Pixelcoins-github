package es.serversurvival.v1.bolsa.posicionesabiertas;

import es.serversurvival.v1.bolsa.posicionesabiertas._shared.domain.PosicionAbierta;
import es.serversurvival.v1.bolsa.posicionescerradas._shared.domain.TipoPosicion;
import es.serversurvival.v1._shared.utils.Funciones;
import es.serversurvival.v1.bolsa.activosinfo._shared.domain.tipoactivos.TipoActivo;

import java.util.UUID;

public final class PosicionesAbiertasTestMother {
    public static PosicionAbierta createPosicionAbierta(String jugaodr, String nombreActivo){
        return new PosicionAbierta(UUID.randomUUID(), jugaodr, TipoActivo.ACCIONES, nombreActivo, 1, 1, Funciones.hoy(), TipoPosicion.LARGO);
    }

    public static PosicionAbierta createPosicionAbierta(String jugaodr, String nombreActivo, TipoPosicion tipoPosicion, double precioApertura, int cantidad){
        return new PosicionAbierta(UUID.randomUUID(), jugaodr, TipoActivo.ACCIONES, nombreActivo, cantidad, precioApertura, Funciones.hoy(), tipoPosicion);
    }
}
