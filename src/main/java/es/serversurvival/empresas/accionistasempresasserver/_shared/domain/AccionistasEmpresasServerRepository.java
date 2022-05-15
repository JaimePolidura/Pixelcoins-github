package es.serversurvival.empresas.accionistasempresasserver._shared.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccionistasEmpresasServerRepository {
    void save(AccionistaEmpresaServer accionistaEmpresaServer);

    Optional<AccionistaEmpresaServer> findById(UUID id);

    List<AccionistaEmpresaServer> findByEmpresa(String empresa);

    List<AccionistaEmpresaServer> findByNombreAccionista(String nombreAccionista);

    void deleteById(UUID id);
}
