package es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VotacionesRepository {
    void save(Votacion votacion);

    List<Votacion> findByEmpresaId(UUID empresaId);

    Optional<Votacion> findById(UUID votacionId);

    List<Votacion> findAll();
}
