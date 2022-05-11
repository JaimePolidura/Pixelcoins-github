package es.serversurvival.bolsa.ordenespremarket._shared.application;

import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.ordenespremarket._shared.domain.OrdenPremarket;
import es.serversurvival.bolsa.ordenespremarket._shared.domain.OrderesPremarketRepository;

import java.util.List;
import java.util.UUID;

public final class OrdenPremarketService {
    private final OrderesPremarketRepository repositoryDb;

    public OrdenPremarketService() {
        this.repositoryDb = DependecyContainer.get(OrderesPremarketRepository.class);
    }

    public void save(OrdenPremarket orden) {
        this.repositoryDb.save(orden);
    }

    public boolean isOrdenRegisteredFromPosicionAbierta (String jugador, UUID posicionAbiertaId) {
        return this.repositoryDb.findByJugador(jugador).stream()
                .anyMatch(order -> order.getIdPosicionabierta().equals(posicionAbiertaId));
    }

    public List<OrdenPremarket> findAll() {
        return this.repositoryDb.findAll();
    }

    public List<OrdenPremarket> findByJugador(String jugador) {
        return this.repositoryDb.findByJugador(jugador);
    }

    public void deleteById(UUID id) {
        this.repositoryDb.deleteById(id);
    }

}
