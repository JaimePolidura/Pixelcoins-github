package es.serversurvival.tienda._shared.application;

import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.cache.Cache;
import es.serversurvival._shared.cache.LRUCache;
import es.serversurvival.tienda._shared.domain.EncantamientoObjecto;
import es.serversurvival.tienda._shared.domain.TiendaObjeto;
import es.serversurvival.tienda._shared.domain.TiendaRepository;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public final class TiendaService {
    private final TiendaRepository repositoryDb;
    private final Cache<UUID, TiendaObjeto> cache;

    public TiendaService(){
        this.repositoryDb = DependecyContainer.get(TiendaRepository.class);
        this.cache = new LRUCache<>(150);
    }

    public void save(TiendaObjeto tiendaObjeto) {
        this.repositoryDb.save(tiendaObjeto);
        this.cache.put(tiendaObjeto.getTiendaObjetoId(), tiendaObjeto);
    }

    public TiendaObjeto save(String jugador, String objetoNombre, int cantidad, double precio,
                             short durabilidad, List<EncantamientoObjecto> encantamientos){
        var objetoTienda = new TiendaObjeto(UUID.randomUUID(), jugador, objetoNombre,
                cantidad, precio, durabilidad, encantamientos);

        this.save(objetoTienda);

        return objetoTienda;
    }

    public TiendaObjeto getById(UUID id) {
        return this.cache.find(id).orElseGet(() -> this.repositoryDb.findById(id)
                .map(saveItemToCache())
                .orElseThrow(() -> new ResourceNotFound("Objeto en la tienda no encontrado")));
    }

    public List<TiendaObjeto> findByJugador(String jugador) {
        return this.cache.isNotFull() ?
                this.cache.findValues(tiendaObjeto -> tiendaObjeto.getJugador().equals(jugador)) :
                this.repositoryDb.findByJugador(jugador).stream().peek(tiendaObjeto -> {
                    this.cache.put(tiendaObjeto.getTiendaObjetoId(), tiendaObjeto);
                }).toList();
    }

    public List<TiendaObjeto> findAll() {
        return this.cache.isNotFull() ?
                this.cache.all() :
                this.repositoryDb.findAll().stream().peek(tiendaObjeto -> {
                    this.cache.put(tiendaObjeto.getTiendaObjetoId(), tiendaObjeto);
                }).toList();
    }

    public void deleteById(UUID id) {
        this.repositoryDb.deleteById(id);
        this.cache.delete(id);
    }

    private Function<TiendaObjeto, TiendaObjeto> saveItemToCache(){
        return tiendaObjeto -> {
            this.cache.put(tiendaObjeto.getTiendaObjetoId(), tiendaObjeto);

            return tiendaObjeto;
        };
    }
}
