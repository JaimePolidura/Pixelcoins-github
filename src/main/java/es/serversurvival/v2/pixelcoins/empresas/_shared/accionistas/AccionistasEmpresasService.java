package es.serversurvival.v2.pixelcoins.empresas._shared.accionistas;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import lombok.AllArgsConstructor;

import java.util.List;
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

    public List<AccionistaEmpresa> findByJugadorId(UUID jugadorId) {
        return this.repository.findByJugadorId(jugadorId);
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
}
