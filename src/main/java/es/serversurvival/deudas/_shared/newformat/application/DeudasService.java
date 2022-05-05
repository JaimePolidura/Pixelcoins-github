package es.serversurvival.deudas._shared.newformat.application;

import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.deudas._shared.newformat.domain.Deuda;
import es.serversurvival.deudas._shared.newformat.domain.DeudasRepository;

import java.util.*;

import static es.serversurvival._shared.mysql.AllMySQLTablesInstances.dateFormater;
import static es.serversurvival._shared.utils.Funciones.getSumaTotalListInteger;

public final class DeudasService {
    private final DeudasRepository deudasRepository;

    public DeudasService(){
        this.deudasRepository = DependecyContainer.get(DeudasRepository.class);
    }

    public void save(String deudor, String acredor, int pixelcoins, int tiempo, int interes) {
        String fechaHoy = dateFormater.format(new Date());
        int cuota = (int) Math.round((double) pixelcoins / tiempo);

        this.deudasRepository.save(new Deuda(UUID.randomUUID(), deudor, acredor, pixelcoins, tiempo, interes, cuota, fechaHoy));
    }

    public void save(Deuda deuda){
        this.deudasRepository.save(deuda);
    }

    public Deuda getDeudaById(UUID id) {
        return this.deudasRepository.findByDeudaId(id)
                .orElseThrow(() -> new ResourceNotFound("Deuda not found exception"));
    }

    public List<Deuda> findDeudasByAcredor(String acredor) {
        return this.deudasRepository.findDeudasByAcredor(acredor);
    }

    public List<Deuda> findDeudasByDeudor(String deudor) {
        return this.deudasRepository.findDeudasByDeudor(deudor);
    }

    public boolean isDeudor(UUID deudaId, String jugador){
        var deudaOptional = this.deudasRepository.findByDeudaId(deudaId);

        return deudaOptional.isPresent() && deudaOptional.get().getDeudor().equals(jugador);
    }

    public List<Deuda> findAll() {
        return this.deudasRepository.findAll();
    }

    public int getAllPixelcoinsDeudasAcredor (String jugador) {
        return getSumaTotalListInteger( findDeudasByAcredor(jugador), Deuda::getPixelcoins_restantes);
    }

    public int getAllPixelcoinsDeudasDeudor (String jugador) {
        return getSumaTotalListInteger( findDeudasByDeudor(jugador), Deuda::getPixelcoins_restantes);
    }

    public Map<String, List<Deuda>> getAllDeudasDeudorMap () {
        return Funciones.mergeMapList(this.findAll(), Deuda::getDeudor);
    }

    public void deleteById(UUID id) {
        this.deudasRepository.deleteById(id);
    }
}
