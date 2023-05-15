package es.serversurvival.v1.empresas.ofertasaccionesserver;

import es.serversurvival.v1._shared.utils.Funciones;
import es.serversurvival.v1.empresas.accionistasserver._shared.domain.TipoAccionista;
import es.serversurvival.v1.empresas.ofertasaccionesserver._shared.domain.OfertaAccionServer;

import java.util.UUID;

public final class OfertasAccionesServerTestMother {
    public static OfertaAccionServer createOfertAccionServerJugador(String nombre, String empresa){
        return new OfertaAccionServer(UUID.randomUUID(), nombre, empresa, 1, 1, Funciones.hoy(), TipoAccionista.JUGADOR, 1, null);
    }

    public static OfertaAccionServer createOfertAccionServerJugador(String nombre, String empresa, double precio, int cantidad){
        return new OfertaAccionServer(UUID.randomUUID(), nombre, empresa, precio, cantidad, Funciones.hoy(), TipoAccionista.JUGADOR, 1, null);
    }

    public static OfertaAccionServer createOfertAccionServerEmpresa(String nombre, String empresa, double precio, int cantidad){
        return new OfertaAccionServer(UUID.randomUUID(), nombre, empresa, precio, cantidad, Funciones.hoy(), TipoAccionista.EMPRESA, 1, null);
    }
}
