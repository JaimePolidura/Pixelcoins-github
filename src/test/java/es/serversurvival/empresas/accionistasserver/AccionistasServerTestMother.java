package es.serversurvival.empresas.accionistasserver;

import es.serversurvival.empresas.accionistasempresasserver._shared.domain.AccionEmpresaServer;

import java.util.UUID;

import static es.serversurvival.empresas.accionistasempresasserver._shared.domain.TipoAccionista.*;

public final class AccionistasServerTestMother {
    public static AccionEmpresaServer createAccionnistaTipoJugaodor(String username, String empresa){
        return new AccionEmpresaServer(UUID.randomUUID(), username, JUGADOR, empresa, 1, 1, null);
    }

    public static AccionEmpresaServer createAccionnistaTipoJugaodor(String username, String empresa, int cantidad){
        return new AccionEmpresaServer(UUID.randomUUID(), username, JUGADOR, empresa, cantidad, 1, null);
    }

    public static AccionEmpresaServer createAccionistaTipoEmpresa(String username, String empresa){
        return new AccionEmpresaServer(UUID.randomUUID(), username, EMPRESA, empresa, 1, 1, null);
    }

    public static AccionEmpresaServer createAccionistaTipoEmpresa(String username, String empresa, int cantidad){
        return new AccionEmpresaServer(UUID.randomUUID(), username, EMPRESA, empresa, cantidad, 1, null);
    }
}
