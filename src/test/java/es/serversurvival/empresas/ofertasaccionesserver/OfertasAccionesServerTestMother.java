package es.serversurvival.empresas.ofertasaccionesserer;

import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.empresas.accionistasempresasserver._shared.domain.TipoAccionista;
import es.serversurvival.empresas.ofertasaccionesserver._shared.domain.OfertaAccionServer;

import java.util.UUID;

public final class OfertasAccionesServerTestMother {
    public static OfertaAccionServer createOfertAccionServerJugador(String nombre, String empresa){
        return new OfertaAccionServer(UUID.randomUUID(), nombre, empresa, 1, 1, Funciones.hoy(), TipoAccionista.JUGADOR, 1, null);
    }
}
