package es.serversurvival.v1.empresas.empleados._shared.application;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival.v1._shared.cache.Cache;
import es.serversurvival.v1._shared.cache.LRUCache;
import es.serversurvival.v1._shared.utils.Funciones;
import es.serversurvival.v1.empresas.empleados._shared.domain.Empleado;
import es.serversurvival.v1.empresas.empleados._shared.domain.EmpleadosRepository;
import es.serversurvival.v1.empresas.empleados._shared.domain.TipoSueldo;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@Service
public class EmpleadosService {
    public static final int MAX_CARGO_LENGTH = 16;
    public static final int MIN_CARGO_LENGTH = 3;
    public static final int MIN_DESPEDIR_RAZON_LENGH = 3;
    public static final int MAX_DESPEDIR_RAZON_LENGH = 16;
    
    private final EmpleadosRepository empleadosRepository;
    private final Cache<UUID, Empleado> cache;

    public EmpleadosService(EmpleadosRepository empleadosRepository) {
        this.empleadosRepository = empleadosRepository;
        this.cache = new LRUCache<>(150);
    }

    public void save(String empleado, String empresa, double sueldo, TipoSueldo tipo, String cargo) {
        String fechaPaga = Funciones.DATE_FORMATER_LEGACY.format(new Date());

        this.save(new Empleado(UUID.randomUUID(), empleado, empresa, sueldo, cargo, tipo, fechaPaga));
    }

    public void save(Empleado empleado) {
        this.empleadosRepository.save(empleado);
        this.cache.put(empleado.getEmpleadoId(), empleado);
    }

    public Empleado getById(UUID empleadoId) {
        return this.cache.find(empleadoId)
                .orElseGet(() -> this.empleadosRepository.findById(empleadoId)
                .map(saveToCache())
                .orElseThrow(() -> new ResourceNotFound("Empleado no encontrado")));
    }

    public Empleado getEmpleadoInEmpresa (String empleadoNombre, String empresa) {
        return this.empleadosRepository.findByJugador(empleadoNombre).stream()
                .filter(empleo -> empleo.getEmpresa().equalsIgnoreCase(empresa))
                .findAny()
                .map(saveToCache())
                .orElseThrow(() -> new ResourceNotFound("Empleo no encontrado"));
    }

    public List<Empleado> findByJugador(String nombre) {
        return this.empleadosRepository.findByJugador(nombre).stream()
                .map(saveToCache())
                .toList();
    }

    public List<Empleado> findByEmpresa(String empresa){
        return this.empleadosRepository.findByEmpresa(empresa).stream()
                .map(saveToCache())
                .toList();
    }

    public List<Empleado> findAll() {
        return this.empleadosRepository.findAll();
    }

    public void deleteById(UUID id) {
        this.empleadosRepository.deleteById(id);
        this.cache.remove(id);
    }

    private Function<Empleado, Empleado> saveToCache(){
        return empleado -> {
            this.cache.put(empleado.getEmpleadoId(), empleado);

            return empleado;
        };
    }

}
