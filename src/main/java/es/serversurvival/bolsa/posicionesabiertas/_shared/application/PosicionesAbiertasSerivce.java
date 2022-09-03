package es.serversurvival.bolsa.posicionesabiertas._shared.application;

import es.dependencyinjector.annotations.Service;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.cache.Cache;
import es.serversurvival._shared.cache.LRUCache;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.TipoActivo;
import es.serversurvival.bolsa.posicionescerradas._shared.domain.TipoPosicion;
import es.serversurvival.bolsa.posicionesabiertas._shared.domain.PosicionAbierta;
import es.serversurvival.bolsa.posicionesabiertas._shared.domain.PosicionesAbiertasRepository;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;

@AllArgsConstructor
@Service
public class PosicionesAbiertasSerivce {
    public static final double PORCENTAJE_CORTO = 5;

    private final PosicionesAbiertasRepository repositoryDb;
    private final Cache<UUID, PosicionAbierta> cache;

    public PosicionesAbiertasSerivce(){
        this.repositoryDb = DependecyContainer.get(PosicionesAbiertasRepository.class);
        this.cache = new LRUCache<>(150);
    }

    public UUID save(String jugador, TipoActivo tipoAcivo, String nombreActivo, int cantidad,
                     double precioApertura, TipoPosicion tipoPosicion) {

        UUID idPosicion = UUID.randomUUID();

        this.save(new PosicionAbierta(idPosicion, jugador, tipoAcivo, nombreActivo, cantidad, precioApertura,
                Funciones.hoy(), tipoPosicion));

        return idPosicion;
    }

    public void save(PosicionAbierta posicionAbierta) {
        this.repositoryDb.save(posicionAbierta);
        this.cache.put(posicionAbierta.getPosicionAbiertaId(), posicionAbierta);
    }

    public PosicionAbierta getById(UUID posicionAbiertaId) {
        return this.cache.find(posicionAbiertaId)
                .orElseGet(() -> this.repositoryDb.findById(posicionAbiertaId)
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

    public List<PosicionAbierta> findByNombreActivo(String nombreActivo) {
        return this.repositoryDb.findByNombreActivo(nombreActivo);
    }

    public boolean existsByNombreActivo(String nombreActivo){
        return !this.findByNombreActivo(nombreActivo).isEmpty();
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
