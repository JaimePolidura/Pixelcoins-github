package es.serversurvival.tienda._shared.application;

import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.cache.Cache;
import es.serversurvival._shared.cache.LRUCache;
import es.serversurvival.tienda._shared.domain.TiendaRepository;
import es.serversurvival.tienda._shared.domain.TiendaObjeto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class TiendaRepositoryService {
    private final TiendaRepository repository;
    private final Cache<UUID, TiendaObjeto> cache;

    public TiendaRepositoryService(){
        this.repository = DependecyContainer.get(TiendaRepository.class);
        this.cache = new LRUCache<>(150);
    }

    public void save(TiendaObjeto tiendaObjeto) {
        this.repository.save(tiendaObjeto);
        this.cache.add(tiendaObjeto.getTiendaObjetoId(), tiendaObjeto);
    }

    public Optional<TiendaObjeto> findById(UUID id) {
        var tiendaObjetoFromCache = this.cache.find(id);

        return tiendaObjetoFromCache.isPresent() ?
                tiendaObjetoFromCache :
                this.repository.findById(id).map(itemTienda -> {
                    this.cache.add(itemTienda.getTiendaObjetoId(), itemTienda);

                    return itemTienda;
                });
    }

    public List<TiendaObjeto> findByJugador(String jugador) {
        return this.cache.notFull() ?
                this.cache.findValues(tiendaObjeto -> tiendaObjeto.getJugador().equals(jugador)) :
                this.repository.findByJugador(jugador).stream()
                        .peek(itemTienda -> {
                            this.cache.add(itemTienda.getTiendaObjetoId(), itemTienda);
                        })
                        .toList();
    }

    public List<TiendaObjeto> findAll() {
        return this.cache.isFull() ? this.repository.findAll() : this.cache.all();
    }

    public void deleteById(UUID id) {
        this.repository.deleteById(id);
        this.cache.delete(id);
    }
}
