package es.serversurvival.bolsa.posicionesabiertas._shared.newformat.application;

import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.cache.Cache;
import es.serversurvival._shared.cache.LRUCache;
import es.serversurvival.bolsa.posicionesabiertas._shared.newformat.domain.PosicionAbierta;
import es.serversurvival.bolsa.posicionesabiertas._shared.newformat.domain.PosicionesAbiertasRepository;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;

public final class PosicionesAbiertasSerivce {
    private final PosicionesAbiertasRepository repositoryDb;
    private final Cache<UUID, PosicionAbierta> cache;

    public PosicionesAbiertasSerivce(){
        this.repositoryDb = DependecyContainer.get(PosicionesAbiertasRepository.class);
        this.cache = new LRUCache<>(150);
    }

    public void save(PosicionAbierta posicionAbierta) {
        this.repositoryDb.save(posicionAbierta);
        this.cache.put(posicionAbierta.getPosicionAbiertaId(), posicionAbierta);
    }

    public PosicionAbierta getById(UUID posicionAbiertaId) {
        return this.cache.find(posicionAbiertaId).orElseGet(() -> this.repositoryDb.findById(posicionAbiertaId)
                .map(saveToCache())
                .orElseThrow(() -> new ResourceNotFound("Posicion abierta no encontrada")));
    }

    public List<PosicionAbierta> findByJugador(String jugador) {
        return this.repositoryDb.findByJugador(jugador).stream()
                .map(saveToCache())
                .toList();
    }

    public List<PosicionAbierta> findByJugador(String jugador, Predicate<PosicionAbierta> condition){
        return this.repositoryDb.findByJugador(jugador).stream()
                .filter(condition)
                .toList();
    }

    public List<PosicionAbierta> findAll() {
        return this.repositoryDb.findAll();
    }

    public List<PosicionAbierta> findAll(Predicate<PosicionAbierta> condition){
        return this.repositoryDb.findAll().stream()
                .filter(condition)
                .toList();
    }



//    public Map<String, List<PosicionAbierta>> getAllPosicionesAbiertasMap (Predicate<? super PosicionAbierta> condition) {
//        List<PosicionAbierta> posicionAbiertas = this.getTodasPosicionesAbiertas().stream()
//                .filter(condition)
//                .collect(Collectors.toList());
//
//        return Funciones.mergeMapList(posicionAbiertas, PosicionAbierta::getJugador);
//    }

    public void deleteById(UUID posicionAbiertaId) {
        this.repositoryDb.deleteById(posicionAbiertaId);
        this.cache.remove(posicionAbiertaId);
    }

    private Function<PosicionAbierta, PosicionAbierta> saveToCache(){
        return posicionAbierta -> {
            this.cache.put(posicionAbierta.getPosicionAbiertaId(), posicionAbierta);

            return posicionAbierta;
        };
    }
}
