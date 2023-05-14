package es.serversurvival.deudas._shared.application;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.deudas._shared.domain.Deuda;
import es.serversurvival.deudas._shared.domain.DeudasRepository;
import lombok.AllArgsConstructor;

import java.util.*;

import static es.jaime.javaddd.application.utils.CollectionUtils.*;

@Service
@AllArgsConstructor
public class DeudasService {
    private final DeudasRepository deudasRepository;

    public UUID save(String deudor, String acredor, double pixelcoins, int tiempo, int interes) {
        String fechaHoy = Funciones.hoy();
        int cuota = (int) Math.round(pixelcoins / tiempo);
        UUID deudaID = UUID.randomUUID();

        this.deudasRepository.save(new Deuda(deudaID, deudor, acredor, pixelcoins, tiempo, interes, cuota, fechaHoy));

        return deudaID;
    }

    public void save(Deuda deuda){
        this.deudasRepository.save(deuda);
    }

    public Deuda getById(UUID id) {
        return this.deudasRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Deuda not found exception"));
    }

    public List<Deuda> findByAcredor(String acredor) {
        return this.deudasRepository.findByAcredor(acredor);
    }

    public List<Deuda> findByDeudor(String deudor) {
        return this.deudasRepository.findByDeudor(deudor);
    }

    public List<Deuda> findByJugador(String jugador){
        var deduasDeduor = this.deudasRepository.findByDeudor(jugador);
        var deduasAcredor = this.deudasRepository.findByAcredor(jugador);

        deduasAcredor.addAll(deduasDeduor);

        return deduasAcredor;
    }

    public List<Deuda> findAll() {
        return this.deudasRepository.findAll();
    }

    public double getAllPixelcoinsDeudasAcredor (String jugador) {
        return getSum( findByAcredor(jugador), Deuda::getPixelcoinsRestantes);
    }

    public double getAllPixelcoinsDeudasDeudor (String jugador) {
        return getSum( findByDeudor(jugador), Deuda::getPixelcoinsRestantes);
    }

    public void deleteById(UUID id) {
        this.deudasRepository.deleteById(id);
    }
}
