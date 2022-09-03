package es.serversurvival.transacciones._shared.application;

import es.dependencyinjector.annotations.Service;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.transacciones._shared.domain.Transaccion;
import es.serversurvival.transacciones._shared.domain.TransaccionesRepository;

import java.util.List;

@Service
public final class TransaccionesService {
    private final TransaccionesRepository repository;

    public TransaccionesService() {
        this.repository = DependecyContainer.get(TransaccionesRepository.class);
    }

    public void save(Transaccion transaccion){
        this.repository.save(transaccion);
    }

    public List<Transaccion> findByJugador(String jugador){
        return this.repository.findByJugador(jugador);
    }
}
