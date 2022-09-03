package es.serversurvival.empresas.empleados._shared.application;

import com.google.common.base.Function;
import es.dependencyinjector.annotations.Service;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.cache.Cache;
import es.serversurvival._shared.cache.LRUCache;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.empresas.empleados._shared.domain.Empleado;
import es.serversurvival.empresas.empleados._shared.domain.EmpleadosRepository;
import es.serversurvival.empresas.empleados._shared.domain.TipoSueldo;
import lombok.AllArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class EmpleadosService {
    public static final int MAX_CARGO_LENGTH = 16;
    public static final int MIN_CARGO_LENGTH = 3;
    public static final int MIN_DESPEDIR_RAZON_LENGH = 3;
    public static final int MAX_DESPEDIR_RAZON_LENGH = 16;

    private final EmpleadosRepository repositoryDb;
    private final Cache<UUID, Empleado> cache;

    public EmpleadosService() {
        this.repositoryDb = DependecyContainer.get(EmpleadosRepository.class);
        this.cache = new LRUCache<>(150);
    }

    public void save(String empleado, String empresa, double sueldo, TipoSueldo tipo, String cargo) {
        String fechaPaga = Funciones.DATE_FORMATER_LEGACY.format(new Date());

        this.save(new Empleado(UUID.randomUUID(), empleado, empresa, sueldo, cargo, tipo, fechaPaga));
    }

    public void save(Empleado empleado) {
        this.repositoryDb.save(empleado);
        this.cache.put(empleado.getEmpleadoId(), empleado);
    }

    public Empleado getById(UUID empleadoId) {
        return this.cache.find(empleadoId)
                .orElseGet(() -> this.repositoryDb.findById(empleadoId)
                .map(saveToCache())
                .orElseThrow(() -> new ResourceNotFound("Empleado no encontrado")));
    }

    public Empleado getEmpleadoInEmpresa (String empleadoNombre, String empresa) {
        return this.repositoryDb.findByJugador(empleadoNombre).stream()
                .filter(empleo -> empleo.getEmpresa().equalsIgnoreCase(empresa))
                .findAny()
                .map(saveToCache())
                .orElseThrow(() -> new ResourceNotFound("Empleo no encontrado"));
    }

    public List<Empleado> findByJugador(String nombre) {
        return this.repositoryDb.findByJugador(nombre).stream()
                .map(saveToCache())
                .toList();
    }

    public List<Empleado> findByEmpresa(String empresa){
        return this.repositoryDb.findByEmpresa(empresa).stream()
                .map(saveToCache())
                .toList();
    }

    public List<Empleado> findAll() {
        return this.repositoryDb.findAll();
    }

    public void deleteById(UUID id) {
        this.repositoryDb.deleteById(id);
        this.cache.remove(id);
    }

    private Function<Empleado, Empleado> saveToCache(){
        return empleado -> {
            this.cache.put(empleado.getEmpleadoId(), empleado);

            return empleado;
        };
    }

}
