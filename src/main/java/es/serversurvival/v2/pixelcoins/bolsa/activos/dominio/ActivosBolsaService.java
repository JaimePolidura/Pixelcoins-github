package es.serversurvival.v2.pixelcoins.bolsa.activos.dominio;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import lombok.AllArgsConstructor;

import java.util.Optional;

@Service
@AllArgsConstructor
public final class ActivosBolsaService {
    private final ActivoBolsaRepository repository;

    public void save(ActivoBolsa activoBolsa) {
        repository.save(activoBolsa);
    }

    public Optional<ActivoBolsa> findById(int activoInfoId) {
        return repository.findById(activoInfoId);
    }

    public ActivoBolsa getById(int activoInfoId) {
        return repository.findById(activoInfoId)
                .orElseThrow(() -> new ResourceNotFound("Activo no encontrado"));
    }

    public Optional<ActivoBolsa> findByNombreCortoAndTipoActivo(String nombreCorto, TipoActivoBolsa tipoActivo) {
        return this.repository.findByNombreCortoAndTipoActivo(nombreCorto, tipoActivo);
    }

    public ActivoBolsa getByNombreCortoAndTipoActivo(String nombreCorto, TipoActivoBolsa tipoActivo) {
        return this.repository.findByNombreCortoAndTipoActivo(nombreCorto, tipoActivo)
                .orElseThrow(() -> new ResourceNotFound("Activo no encontrado"));
    }
}
