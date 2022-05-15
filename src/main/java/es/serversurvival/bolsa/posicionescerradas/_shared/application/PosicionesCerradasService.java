package es.serversurvival.bolsa.posicionescerradas._shared.application;

import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.SupportedTipoActivo;
import es.serversurvival.bolsa.posicionescerradas._shared.domain.PosicionCerrada;
import es.serversurvival.bolsa.posicionescerradas._shared.domain.PosicionesCerradasRepository;
import es.serversurvival.bolsa.posicionescerradas._shared.domain.TipoPosicion;

import java.util.*;
import java.util.function.Predicate;


public final class PosicionesCerradasService {
    public static final Comparator<PosicionCerrada> SORT_BY_RENTABILIDADES_ASC = (p1, p2) -> (int) (p1.calculateRentabildiad() - p2.calculateRentabildiad());
    public static final Comparator<PosicionCerrada> SORT_BY_RENTABILIDADES_DESC = (p1, p2) -> (int) (p1.calculateRentabildiad() - p2.calculateRentabildiad());

    private final PosicionesCerradasRepository repositoryDb;

    public PosicionesCerradasService(){
        this.repositoryDb = DependecyContainer.get(PosicionesCerradasRepository.class);
    }

    public void save(String jugador, SupportedTipoActivo tipoActivo, String nombreActivo, int cantidad, double precioApertura,
                     String fechaApertura, double precioCierre, TipoPosicion tipoPosicion) {
        String fechaCierre = Funciones.DATE_FORMATER_LEGACY.format(new Date());

        this.save(new PosicionCerrada(UUID.randomUUID(), jugador, tipoActivo, nombreActivo, cantidad, precioApertura,
                fechaApertura, precioCierre, fechaCierre, tipoPosicion));
    }

    public void save(PosicionCerrada posicionCerrada) {
        this.repositoryDb.save(posicionCerrada);
    }

    public List<PosicionCerrada> findByJugador(String jugador) {
        return this.repositoryDb.findByJugador(jugador);
    }

    public List<PosicionCerrada> findByJugador(String jugador, Comparator<? super PosicionCerrada> comparator){
        return this.findByJugador(jugador).stream()
                .sorted(comparator)
                .toList();
    }

    public List<PosicionCerrada> findAll(Comparator<? super PosicionCerrada> comparator){
        return this.repositoryDb.findAll().stream()
                .sorted(comparator)
                .toList();
    }

    public List<PosicionCerrada> findAllByConditionAndSorted(Predicate<? super PosicionCerrada> condition,
                                                             Comparator<? super PosicionCerrada> comparator) {
        return this.repositoryDb.findAll().stream()
                .filter(condition)
                .sorted(comparator)
                .toList();
    }

    public List<PosicionCerrada> findAllByConditionAndSorted(Predicate<? super PosicionCerrada> condition) {
        return this.repositoryDb.findAll().stream()
                .filter(condition)
                .toList();
    }
}
