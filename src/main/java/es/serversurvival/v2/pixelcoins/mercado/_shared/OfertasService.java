package es.serversurvival.v2.pixelcoins.mercado._shared;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;
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

    public Optional<Oferta> findByObjetoAndTipo(String objeto, TipoOferta tipo) {
        return this.repository.findByObjetoAndTipo(objeto, tipo);
    }

    public Optional<Oferta> findByObjetoAndTipo(UUID objetoId, TipoOferta tipo) {
        return this.repository.findByObjetoAndTipo(objetoId.toString(), tipo);
    }

    public void deleteById(UUID ofertaId) {
        this.repository.deleteById(ofertaId);
    }

    public void deleteByObjetoYTipo(String objeto, TipoOferta tipo) {
        this.repository.deleteByObjetoYTipo(objeto, tipo);
    }
}
