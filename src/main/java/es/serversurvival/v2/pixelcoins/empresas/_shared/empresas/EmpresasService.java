package es.serversurvival.v2.pixelcoins.empresas._shared.empresas;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public final class EmpresasService {
    private final EmpresasRepository empresasRepository;

    public void save(Empresa empresa) {
        this.empresasRepository.save(empresa);
    }

    public Empresa getById(UUID empresaId) {
        return this.empresasRepository.findById(empresaId).orElseThrow(() -> new ResourceNotFound("Empresa no encontrada"));
    }

    public Optional<Empresa> findByNombre(String nombre) {
        return this.empresasRepository.findByNombre(nombre);
    }

    public List<Empresa> findAll() {
        return this.empresasRepository.findAll();
    }
}
