package es.serversurvival.bolsa.posicionesabiertas._shared.newformat.application;

import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.cache.Cache;
import es.serversurvival._shared.cache.LRUCache;
import es.serversurvival.bolsa.other._shared.llamadasapi.mysql.TipoActivo;
import es.serversurvival.bolsa.other._shared.posicionescerradas.mysql.TipoPosicion;
import es.serversurvival.bolsa.posicionesabiertas._shared.newformat.domain.PosicionAbierta;
import es.serversurvival.bolsa.posicionesabiertas._shared.newformat.domain.PosicionesAbiertasRepository;
import org.junit.internal.builders.JUnit3Builder;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;

import static es.serversurvival._shared.mysql.AllMySQLTablesInstances.dateFormater;

public final class PosicionesAbiertasSerivce {
    public static final double PORCENTAJE_CORTO = 5;

    private final PosicionesAbiertasRepository repositoryDb;
    private final Cache<UUID, PosicionAbierta> cache;

    public PosicionesAbiertasSerivce(){
        this.repositoryDb = DependecyContainer.get(PosicionesAbiertasRepository.class);
        this.cache = new LRUCache<>(150);
    }

    public void save(String jugador, TipoActivo tipoAcivo, String nombreActivo, int cantidad,
                              double precioApertura, TipoPosicion tipoPosicion) {
        String fecha = dateFormater.format(new Date());

        this.save(new PosicionAbierta(UUID.randomUUID(), jugador, tipoAcivo, nombreActivo, cantidad, precioApertura,
                fecha, tipoPosicion));
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

    public boolean existsNombreActivo(String nombreActivo){
        return this.repositoryDb.findAll().stream()
                .anyMatch(posicionAbierta -> posicionAbierta.getNombreActivo().equalsIgnoreCase(nombreActivo));
    }

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
