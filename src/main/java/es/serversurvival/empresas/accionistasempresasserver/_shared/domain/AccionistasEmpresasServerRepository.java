package es.serversurvival.empresas.accionistasempresasserver._shared.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccionistasEmpresasServerRepository {
    void save(AccionEmpresaServer accionistaEmpresaServer);

    Optional<AccionEmpresaServer> findById(UUID id);

    List<AccionEmpresaServer> findByEmpresa(String empresa);

    List<AccionEmpresaServer> findByNombreAccionista(String nombreAccionista);

    void deleteById(UUID id);
}
