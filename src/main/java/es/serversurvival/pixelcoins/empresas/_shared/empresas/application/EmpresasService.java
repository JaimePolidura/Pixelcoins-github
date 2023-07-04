package es.serversurvival.pixelcoins.empresas._shared.empresas.application;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival._shared.cache.Cache;
import es.serversurvival._shared.cache.UnlimitedCacheSize;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.EmpresasRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmpresasService {
    private final EmpresasRepository empresasRepository;
    private final Cache<UUID, Empresa> empresaByIdCache;

    public EmpresasService(EmpresasRepository empresasRepository) {
        this.empresaByIdCache = new UnlimitedCacheSize<>();
        this.empresasRepository = empresasRepository;
    }

    public void save(Empresa empresa) {
        this.empresasRepository.save(empresa);
        this.empresaByIdCache.put(empresa.getEmpresaId(), empresa);
    }

    public Empresa getById(UUID empresaId) {
        return empresaByIdCache.find(empresaId)
                .or(() -> empresasRepository.findById(empresaId))
                .map(empresa -> empresaByIdCache.put(empresa.getEmpresaId(), empresa))
                .orElseThrow(() -> new ResourceNotFound("Empresa no encontrada"));
    }

    public Empresa getByNombre(String nombre) {
        return this.empresasRepository.findByNombre(nombre)
                .orElseThrow(() -> new ResourceNotFound("Empresa no encontrada"));
    }

    public Optional<Empresa> findByNombre(String nombre) {
        return this.empresasRepository.findByNombre(nombre);
    }

    public List<Empresa> findAllNoCerradas() {
        return this.empresasRepository.findAll().stream()
                .filter(empresa -> !empresa.isEstaCerrado())
                .collect(Collectors.toList());
    }
}
