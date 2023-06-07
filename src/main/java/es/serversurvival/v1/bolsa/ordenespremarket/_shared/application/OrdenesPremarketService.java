package es.serversurvival.v1.bolsa.ordenespremarket._shared.application;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival.v1.bolsa.ordenespremarket._shared.domain.OrdenPremarket;
import es.serversurvival.v1.bolsa.ordenespremarket._shared.domain.OrderesPremarketRepository;
import es.serversurvival.v1.bolsa.ordenespremarket._shared.domain.TipoAccion;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OrdenesPremarketService {
    private final OrderesPremarketRepository repositoryDb;

    public void save(String jugador, String nombreActivo, int cantidad, TipoAccion tipoAccion, UUID posicionAbiertaId){
        this.repositoryDb.save(new OrdenPremarket(UUID.randomUUID(), jugador, nombreActivo, cantidad,
                tipoAccion, posicionAbiertaId));
    }


    public void save(OrdenPremarket orden) {
        this.repositoryDb.save(orden);
    }

    public OrdenPremarket getById(UUID id){
        return this.repositoryDb.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Orden no encontrada para esa id"));
    }

    public boolean isOrdenRegisteredFromPosicionAbierta (String jugador, UUID posicionAbiertaId) {
        return this.repositoryDb.findByJugador(jugador).stream()
                .anyMatch(order -> order.getPosicionAbiertaId().equals(posicionAbiertaId));
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
