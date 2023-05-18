package es.serversurvival.v2.pixelcoins.mercado._shared;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public final class OfertasService {
    private final MercadoOfertasRepository repository;

    public void save(Oferta oferta) {
        this.repository.save(oferta);
    }

    public Oferta getById(UUID ofertaId) {
        return this.repository.findById(ofertaId).orElseThrow(() -> new ResourceNotFound("Oferta no encontrada"));
    }

    public List<Oferta> findByTipo(TipoOferta tipoOferta) {
        return this.repository.findByTipo(tipoOferta);
    }

    public void deleteById(UUID ofertaId) {
        this.repository.deleteById(ofertaId);
    }
}
