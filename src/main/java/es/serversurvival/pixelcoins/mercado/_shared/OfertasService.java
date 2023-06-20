package es.serversurvival.pixelcoins.mercado._shared;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OfertasService {
    private final MercadoOfertasRepository repository;

    public void save(Oferta oferta) {
        this.repository.save(oferta);
    }

    public <T extends Oferta> T getById(UUID ofertaId, Class<? extends T> ofertaTypeClazz) {
        Oferta oferta = this.repository.findById(ofertaId).orElseThrow(() -> new ResourceNotFound("Oferta no encontrada"));
        return ofertaTypeClazz.cast(oferta);
    }

    public List<Oferta> findByTipo(TipoOferta... tipoOfertas) {
        return repository.findByTipo(tipoOfertas);
    }

    public <T extends Oferta> List<T> findByTipo(Class<T> classOferta, TipoOferta... tipoOfertas) {
        return repository.findByTipo(tipoOfertas).stream()
                .map(classOferta::cast)
                .collect(Collectors.toList());
    }

    public Oferta getById(UUID ofertaId) {
        return this.repository.findById(ofertaId).orElseThrow(() -> new ResourceNotFound("Oferta no encontrada"));
    }

    public Optional<Oferta> findByObjetoAndTipo(String objeto, TipoOferta tipo) {
        return this.repository.findByObjetoAndTipo(objeto, tipo);
    }

    public void deleteById(UUID ofertaId) {
        this.repository.deleteById(ofertaId);
    }

    public void deleteByObjetoYTipo(String objeto, TipoOferta tipo) {
        this.repository.deleteByObjetoYTipo(objeto, tipo);
    }
}
