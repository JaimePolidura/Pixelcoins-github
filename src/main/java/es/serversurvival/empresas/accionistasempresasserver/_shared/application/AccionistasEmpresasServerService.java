package es.serversurvival.empresas.accionistasempresasserver._shared.application;

import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.empresas.accionistasempresasserver._shared.domain.AccionistaEmpresaServer;
import es.serversurvival.empresas.accionistasempresasserver._shared.domain.AccionistasEmpresasServerRepository;

import java.util.List;
import java.util.UUID;

public final class AccionistasEmpresasServerService {
    private final AccionistasEmpresasServerRepository repositoryDb;

    public AccionistasEmpresasServerService() {
        this.repositoryDb = DependecyContainer.get(AccionistasEmpresasServerRepository.class);
    }

    public AccionistaEmpresaServer getById(UUID id) {
        return this.repositoryDb.findById(id).orElseThrow(() -> new ResourceNotFound("Accion no encontrada"));
    }

    public void save(AccionistaEmpresaServer accionistaEmpresaServer) {
        this.repositoryDb.save(accionistaEmpresaServer);
    }

    public List<AccionistaEmpresaServer> findByEmpresa(String empresa) {
        return this.repositoryDb.findByEmpresa(empresa);
    }

    public List<AccionistaEmpresaServer> findByNombreAccionista(String nombreAccionista) {
        return this.repositoryDb.findByNombreAccionista(nombreAccionista);
    }

    public void deleteById(UUID id) {
        this.repositoryDb.deleteById(id);
    }
}
