package es.serversurvival.pixelcoins.bolsa._shared.activos.aplicacion;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.ActivoBolsa;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.ActivoBolsaRepository;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.TipoActivoBolsa;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public final class ActivosBolsaService {
    private final ActivoBolsaRepository repository;

    public void save(ActivoBolsa activoBolsa) {
        repository.save(activoBolsa);
    }

    public List<ActivoBolsa> findByTipo(TipoActivoBolsa tipo) {
        return repository.findByTipo(tipo);
    }

    public Optional<ActivoBolsa> findById(UUID activoInfoId) {
        return repository.findById(activoInfoId);
    }

    public ActivoBolsa getById(UUID activoInfoId) {
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
