package es.serversurvival.empresas.ofertasaccionesserver._shared.application;

import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.empresas.ofertasaccionesserver._shared.domain.OfertaAccionServer;
import es.serversurvival.empresas.ofertasaccionesserver._shared.domain.OfertasAccionesServerRepository;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class OfertasAccionesServerService {
    private final OfertasAccionesServerRepository repositoryDb;

    public OfertasAccionesServerService() {
        this.repositoryDb = DependecyContainer.get(OfertasAccionesServerRepository.class);
    }

    public void save(OfertaAccionServer ofertaMercadoServer) {
        this.repositoryDb.save(ofertaMercadoServer);
    }

    public OfertaAccionServer getById(UUID id) {
        return this.repositoryDb.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Oferta no encontrada"));
    }

    public List<OfertaAccionServer> findAll() {
        return this.repositoryDb.findAll();
    }

    public List<OfertaAccionServer> findByEmpresa(String empresa) {
        return this.repositoryDb.findByEmpresa(empresa);
    }

    public List<OfertaAccionServer> findByEmpresa(String empresa, Predicate<? super OfertaAccionServer> condition) {
        return this.repositoryDb.findByEmpresa(empresa).stream()
                .filter(condition)
                .collect(Collectors.toList());
    }

    public List<OfertaAccionServer> findByOfertanteNombre(String nombreOfertante) {
        return this.repositoryDb.findByOfertanteNombre(nombreOfertante);
    }

    public void deleteById(UUID id) {
        this.repositoryDb.deleteById(id);
    }
}
