package es.serversurvival.pixelcoins.empresas._shared.empresas.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmpresasRepository {
    void save(Empresa empresa);

    Optional<Empresa> findById(UUID empresaId);

    Optional<Empresa> findByNombre(String nombre);

    List<Empresa> findByDirectorJugadorId(UUID directorJugadorId);

    List<Empresa> findAll();
}
