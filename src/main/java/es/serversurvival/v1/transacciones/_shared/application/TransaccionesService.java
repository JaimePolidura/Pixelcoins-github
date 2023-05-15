package es.serversurvival.v1.transacciones._shared.application;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.v1.transacciones._shared.domain.Transaccion;
import es.serversurvival.v1.transacciones._shared.domain.TransaccionesRepository;
import lombok.AllArgsConstructor;

import java.util.List;

@Service
@AllArgsConstructor
public final class TransaccionesService {
    private final TransaccionesRepository repository;

    public void save(Transaccion transaccion){
        this.repository.save(transaccion);
    }

    public List<Transaccion> findByJugador(String jugador){
        return this.repository.findByJugador(jugador);
    }
}
