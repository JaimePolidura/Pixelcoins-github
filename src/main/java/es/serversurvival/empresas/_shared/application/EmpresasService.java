package es.serversurvival.empresas._shared.application;

import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.cache.Cache;
import es.serversurvival._shared.cache.LRUCache;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.empresas._shared.domain.Empresa;
import es.serversurvival.empresas._shared.domain.EmpresasRepostiory;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import static es.serversurvival._shared.utils.Funciones.getSumaTotalListDouble;

public final class EmpresasService {
    private final EmpresasRepostiory repostioryDb;
    private final Cache<String, Empresa> cache;

    public EmpresasService(){
        this.repostioryDb = DependecyContainer.get(EmpresasRepostiory.class);
        this.cache = new LRUCache<>(200);
    }

    public void save(Empresa empresa) {
        this.repostioryDb.save(empresa);
        this.cache.put(empresa.getNombre(), empresa);
    }

    public Empresa getByEmpresaId(UUID empresaId) {
        return cache.findValue(empresa -> empresa.getEmpresaId() == empresaId).orElseGet(() -> this.repostioryDb.findByEmpresaId(empresaId)
                .map(saveEmpresaToCache())
                .orElseThrow(() -> new ResourceNotFound("Empresa no encontrada")));
    }

    public Empresa getEmpresaByNombre(String nombre) {
        return cache.find(nombre).orElseGet(() -> this.repostioryDb.findEmpresaByNombre(nombre)
                .map(saveEmpresaToCache())
                .orElseThrow(() -> new ResourceNotFound("Empresa no encontrada")));
    }

    public List<Empresa> getByOwner(String owner) {
        return this.repostioryDb.findByOwner(owner).stream()
                .map(saveEmpresaToCache())
                .toList();
    }

    public List<Empresa> getAll() {
        return this.repostioryDb.findAll().stream()
                .map(saveEmpresaToCache())
                .toList();
    }

    public double getAllPixelcoinsEnEmpresas (String jugador){
        return getSumaTotalListDouble( getByOwner(jugador), Empresa::getPixelcoins );
    }

    public void deleteByEmpresaId(UUID empresaId) {
        this.repostioryDb.deleteByEmpresaId(empresaId);
        this.cache.delete(empresa -> empresa.getEmpresaId().equals(empresaId));
    }

    public boolean isCotizada(String nombre){
        return this.getEmpresaByNombre(nombre).isCotizada();
    }

    public Map<String, List<Empresa>> getAllEmpresasJugadorMap () {
        return Funciones.mergeMapList(this.getAll(), Empresa::getOwner);
    }

    private Function<Empresa, Empresa> saveEmpresaToCache(){
        return empresa -> {
            this.cache.put(empresa.getNombre(), empresa);

            return empresa;
        };
    }
}
