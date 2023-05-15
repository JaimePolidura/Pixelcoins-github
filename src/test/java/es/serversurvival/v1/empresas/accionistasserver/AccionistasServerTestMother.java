package es.serversurvival.v1.empresas.accionistasserver;

import es.serversurvival.v1.empresas.accionistasserver._shared.domain.AccionistaServer;
import es.serversurvival.v1.empresas.accionistasserver._shared.domain.TipoAccionista;

import java.util.UUID;

public final class AccionistasServerTestMother {
    public static AccionistaServer createAccionnistaTipoJugaodor(String username, String empresa){
        return new AccionistaServer(UUID.randomUUID(), username, TipoAccionista.JUGADOR, empresa, 1, 1, null);
    }

    public static AccionistaServer createAccionnistaTipoJugaodor(String username, String empresa, int cantidad){
        return new AccionistaServer(UUID.randomUUID(), username, TipoAccionista.JUGADOR, empresa, cantidad, 1, null);
    }

    public static AccionistaServer createAccionistaTipoEmpresa(String username, String empresa){
        return new AccionistaServer(UUID.randomUUID(), username, TipoAccionista.EMPRESA, empresa, 1, 1, null);
    }

    public static AccionistaServer createAccionistaTipoEmpresa(String username, String empresa, int cantidad){
        return new AccionistaServer(UUID.randomUUID(), username, TipoAccionista.EMPRESA, empresa, cantidad, 1, null);
    }
}
