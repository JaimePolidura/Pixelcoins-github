package es.serversurvival.pixelcoins.empresas._shared.accionistas;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public final class AccionistasEmpresasService {
    private final AccionistaEmpresaRepository repository;

    public void save(AccionistaEmpresa empresa) {
        this.repository.save(empresa);
    }

    public AccionistaEmpresa getById(UUID accionistaId) {
        return this.repository.findById(accionistaId).orElseThrow(() -> new ResourceNotFound("Accionista no encontrado"));
    }

    public AccionistaEmpresa getByEmpresaIdAndJugadorId(UUID empresaId, UUID jugadorId) {
        return this.repository.findByEmpresaIdAndJugadorId(empresaId, jugadorId).orElseThrow(() -> new ResourceNotFound("Accionista no encontrado"));
    }

    public Optional<AccionistaEmpresa> findByEmpresaIdAndJugadorId(UUID empresaId, UUID jugadorId) {
        return this.repository.findByEmpresaIdAndJugadorId(empresaId, jugadorId);
    }

    public List<AccionistaEmpresa> findByJugadorId(UUID jugadorId) {
        return repository.findByJugadorId(jugadorId);
    }

    public List<AccionistaEmpresa> findByEmpresaId(UUID empresaId) {
        return this.repository.findByEmpresaId(empresaId);
    }

    public List<AccionistaEmpresa> findAll() {
        return this.repository.findAll();
    }

    public void deleteByEmpresaId(UUID empresaId) {
        this.repository.deleteByEmpresaId(empresaId);
    }

    public void deleteById(UUID accionistaId) {
        repository.deleteById(accionistaId);
    }

    public void incrementarPosicionAccionEnUno(UUID empresaId, UUID compradorId) {
        save(findByEmpresaIdAndJugadorId(empresaId, compradorId)
                .orElse(new AccionistaEmpresa(UUID.randomUUID(), empresaId, compradorId, 0))
                .incrementarNAccionesEnUno());
    }
}
