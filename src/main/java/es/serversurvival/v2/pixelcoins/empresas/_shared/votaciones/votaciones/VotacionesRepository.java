package es.serversurvival.v2.pixelcoins.empresas._shared.votaciones.votaciones;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VotacionesRepository {
    void save(Votacion votacion);

    List<Votacion> findByEmpresaId(UUID empresaId);

    Optional<Votacion> findById(UUID votacionId);

    List<Votacion> findAll();
}
