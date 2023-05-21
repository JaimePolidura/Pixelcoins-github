package es.serversurvival.v2.pixelcoins.empresas._shared.votaciones.votaciones;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public final class VotacionesService {
    private final VotacionesRepository repository;

    public void save(Votacion votacion) {
        repository.save(votacion);
    }

    public List<Votacion> findByEmpresaId(UUID empresaId) {
        return repository.findByEmpresaId(empresaId);
    }

    public Votacion getById(UUID votacionId) {
        return repository.findById(votacionId).orElseThrow(() -> new ResourceNotFound("Votacion no encontrada"));
    }

    public List<Votacion> findAll() {
        return repository.findAll();
    }
}
