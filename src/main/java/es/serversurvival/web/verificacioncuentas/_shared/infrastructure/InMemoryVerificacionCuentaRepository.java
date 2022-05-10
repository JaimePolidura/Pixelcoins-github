package es.serversurvival.web.verificacioncuentas._shared.infrastructure;

import es.serversurvival.web.verificacioncuentas._shared.domain.VerificacionCuenta;
import es.serversurvival.web.verificacioncuentas._shared.domain.VerificacionCuentaRepository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class InMemoryVerificacionCuentaRepository implements VerificacionCuentaRepository {
    private final Map<String, VerificacionCuenta> numeros;

    public InMemoryVerificacionCuentaRepository(){
        this.numeros = new ConcurrentHashMap<>();
    }

    @Override
    public void save(VerificacionCuenta verificacionCuenta) {
        this.numeros.put(verificacionCuenta.getJugador(), verificacionCuenta);
    }

    @Override
    public Optional<VerificacionCuenta> findByJugador(String jugaor) {
        return Optional.ofNullable(this.numeros.get(jugaor));
    }

    @Override
    public void deleteByJugador(String jugador) {
        this.numeros.remove(jugador);
    }
}
