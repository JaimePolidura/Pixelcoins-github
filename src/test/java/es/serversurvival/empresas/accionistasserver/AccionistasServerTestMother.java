package es.serversurvival.empresas.accionistasserver;

import es.serversurvival.empresas.accionistasserver._shared.domain.AccionistaServer;

import java.util.UUID;

import static es.serversurvival.empresas.accionistasserver._shared.domain.TipoAccionista.*;

public final class AccionistasServerTestMother {
    public static AccionistaServer createAccionnistaTipoJugaodor(String username, String empresa){
        return new AccionistaServer(UUID.randomUUID(), username, JUGADOR, empresa, 1, 1, null);
    }

    public static AccionistaServer createAccionnistaTipoJugaodor(String username, String empresa, int cantidad){
        return new AccionistaServer(UUID.randomUUID(), username, JUGADOR, empresa, cantidad, 1, null);
    }

    public static AccionistaServer createAccionistaTipoEmpresa(String username, String empresa){
        return new AccionistaServer(UUID.randomUUID(), username, EMPRESA, empresa, 1, 1, null);
    }

    public static AccionistaServer createAccionistaTipoEmpresa(String username, String empresa, int cantidad){
        return new AccionistaServer(UUID.randomUUID(), username, EMPRESA, empresa, cantidad, 1, null);
    }
}
