package es.serversurvival.v2.pixelcoins.bolsa._shared.premarket.application;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival.v2.pixelcoins.bolsa._shared.premarket.domain.OrdenPremarket;
import es.serversurvival.v2.pixelcoins.bolsa._shared.premarket.domain.OrdenesPremarketRepository;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public final class OrdenesPremarketService {
    private final OrdenesPremarketRepository repository;

    public UUID save(OrdenPremarket orden) {
        this.repository.save(orden);
        return orden.getOrdenPremarketId();
    }

    public OrdenPremarket getById(UUID ordenPremarketId) {
        return repository.findById(ordenPremarketId)
                .orElseThrow(() -> new ResourceNotFound("Orden premarket no encontrada"));
    }

    public List<OrdenPremarket> findByPosicionAbiertaId(UUID posicionAbiertaId) {
        return repository.findByPosicionAbiertaId(posicionAbiertaId);
    }

    public List<OrdenPremarket> findAll() {
        return repository.findAll();
    }

    public void deleteById(UUID ordenPremarketId) {
        repository.deleteById(ordenPremarketId);
    }
}
