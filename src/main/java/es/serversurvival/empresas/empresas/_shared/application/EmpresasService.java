package es.serversurvival.empresas.empresas._shared.application;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival._shared.cache.Cache;
import es.serversurvival._shared.cache.LRUCache;
import es.serversurvival._shared.utils.CollectionUtils;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;
import es.serversurvival.empresas.empresas._shared.domain.EmpresasRepostiory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import static es.serversurvival._shared.utils.CollectionUtils.*;

@Service
public class EmpresasService {
    public static final int MAX_EMPRESAS_PER_JUGADOR = 5;
    public static final int MAX_NOMBRE_LONGITUD = 16;
    public static final int MAX_DESC_LONGITUD = 200;

    private final EmpresasRepostiory empresasRepostiory;
    private final Cache<String, Empresa> cache;

    public EmpresasService(EmpresasRepostiory empresasRepostiory){
        this.empresasRepostiory = empresasRepostiory;
        this.cache = new LRUCache<>(200);
    }

    //TODO Logica de negocio en persistencia
    public void save(String nombre, String jugadorNombre, String descripccion){
        this.save(new Empresa(UUID.randomUUID(), nombre, jugadorNombre, 0, 0,
                0, "DIAMOND_PICKAXE", descripccion, false, 0));
    }

    public void save(Empresa empresa) {
        this.empresasRepostiory.save(empresa);
        this.cache.put(empresa.getNombre(), empresa);
    }

    public boolean isOwner(String jugadorNombre, String empresaNombre){
        Optional<Empresa> empresaByNombre = this.empresasRepostiory.findByNombre(empresaNombre);

        return empresaByNombre.isPresent() && empresaByNombre.get().getOwner().equalsIgnoreCase(jugadorNombre);
    }

    public Empresa getById(UUID empresaId) {
        return cache.findValueIf(empresa -> empresa.getEmpresaId() == empresaId).orElseGet(() -> this.empresasRepostiory.findById(empresaId)
                .map(saveEmpresaToCache())
                .orElseThrow(() -> new ResourceNotFound("Empresa no encontrada")));
    }

    public Empresa getByNombre(String nombre) {
        return cache.find(nombre).orElseGet(() -> this.empresasRepostiory.findByNombre(nombre)
                .map(saveEmpresaToCache())
                .orElseThrow(() -> new ResourceNotFound("Empresa no encontrada")));
    }

    public List<Empresa> getByOwner(String owner) {
        return this.empresasRepostiory.findByOwner(owner).stream()
                .map(saveEmpresaToCache())
                .toList();
    }

    public List<Empresa> findAll() {
        return this.empresasRepostiory.findAll().stream()
                .map(saveEmpresaToCache())
                .toList();
    }

    public double getAllPixelcoinsEnEmpresas (String jugador){
        return getSumaTotalListDouble( getByOwner(jugador), Empresa::getPixelcoins );
    }

    public void deleteByEmpresaId(UUID empresaId) {
        this.empresasRepostiory.deleteById(empresaId);
        this.cache.remove(empresa -> empresa.getEmpresaId().equals(empresaId));
    }

    public Map<String, List<Empresa>> getAllEmpresasJugadorMap () {
        return CollectionUtils.mergeMapList(this.findAll(), Empresa::getOwner);
    }

    private Function<Empresa, Empresa> saveEmpresaToCache(){
        return empresa -> {
            this.cache.put(empresa.getNombre(), empresa);

            return empresa;
        };
    }
}
