package es.serversurvival.v1.empresas.accionistasserver._shared.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccionistasServerRepository {
    void save(AccionistaServer accionistaEmpresaServer);

    Optional<AccionistaServer> findById(UUID id);

    List<AccionistaServer> findByEmpresa(String empresa);

    List<AccionistaServer> findByNombreAccionista(String nombreAccionista);

    void deleteById(UUID id);
}
