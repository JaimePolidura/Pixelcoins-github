package es.serversurvival.web.verificacioncuentas._shared.application;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.web.verificacioncuentas._shared.domain.VerificacionCuenta;
import es.serversurvival.web.verificacioncuentas._shared.domain.VerificacionCuentaRepository;

@Service
public final class VerificacionCuentaService {
    private final VerificacionCuentaRepository repositoryDb;

    public VerificacionCuentaService(){
        this.repositoryDb = DependecyContainer.get(VerificacionCuentaRepository.class);
    }

    public void save(String jugador, int numero) {
        this.repositoryDb.save(new VerificacionCuenta(jugador, numero));
    }

    public VerificacionCuenta getByJugador(String jugaor) {
        return this.repositoryDb.findByJugador(jugaor)
                .orElseThrow(() -> new ResourceNotFound("Numero verificaion cuenta no encontrado"));
    }

    public void deleteByJugador(String jugador) {
        this.repositoryDb.deleteByJugador(jugador);
    }
}
