package es.serversurvival.empresas.empresas._shared.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmpresasRepostiory {
    void save(Empresa empresa);

    Optional<Empresa> findEmpresaByNombre(String nombre);

    Optional<Empresa> findByEmpresaId(UUID empresaId);

    List<Empresa> findByOwner(String owner);

    List<Empresa> findAll();

    void deleteByEmpresaId(UUID empresaId);


}
