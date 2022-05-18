package es.serversurvival.empresas.accionistasempresasserver._shared.application;

import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.empresas.accionistasempresasserver._shared.domain.AccionEmpresaServer;
import es.serversurvival.empresas.accionistasempresasserver._shared.domain.AccionistasEmpresasServerRepository;
import es.serversurvival.empresas.accionistasempresasserver._shared.domain.TipoAccionista;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

import static es.serversurvival._shared.utils.Funciones.*;

public class AccionistasEmpresasServerService {
    private final AccionistasEmpresasServerRepository repositoryDb;

    public AccionistasEmpresasServerService() {
        this.repositoryDb = DependecyContainer.get(AccionistasEmpresasServerRepository.class);
    }
    public UUID save(String accionistaName, TipoAccionista tipoAccionista, String empresaName, int cantidad, double precioApertura){
        String date = DATE_FORMATER_LEGACY.format(new Date());
        UUID idAccionista = UUID.randomUUID();

        this.save(new AccionEmpresaServer(
                idAccionista, accionistaName, tipoAccionista, empresaName, cantidad, precioApertura, date
        ));

        return idAccionista;
    }

    public void save(AccionEmpresaServer accionistaEmpresaServer) {
        this.repositoryDb.save(accionistaEmpresaServer);
    }

    public AccionEmpresaServer getById(UUID id) {
        return this.repositoryDb.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Accion no encontrada"));
    }

    public List<AccionEmpresaServer> findByEmpresa(String empresa) {
        return this.repositoryDb.findByEmpresa(empresa);
    }

    public List<AccionEmpresaServer> findByEmpresa(String empresa, Predicate<? super AccionEmpresaServer> condition) {
        return this.findByEmpresa(empresa).stream()
                .filter(condition)
                .toList();
    }

    public List<AccionEmpresaServer> findByNombreAccionista(String nombreAccionista) {
        return this.repositoryDb.findByNombreAccionista(nombreAccionista);
    }

    public void deleteById(UUID id) {
        this.repositoryDb.deleteById(id);
    }
}
