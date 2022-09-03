package es.serversurvival.empresas.accionistasserver._shared.application;

import es.dependencyinjector.annotations.Service;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.empresas.accionistasserver._shared.domain.AccionistaServer;
import es.serversurvival.empresas.accionistasserver._shared.domain.AccionistasServerRepository;
import es.serversurvival.empresas.accionistasserver._shared.domain.TipoAccionista;
import lombok.AllArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

import static es.serversurvival._shared.utils.Funciones.*;

@AllArgsConstructor
@Service
public class AccionistasServerService {
    private final AccionistasServerRepository repositoryDb;

    public AccionistasServerService() {
        this.repositoryDb = DependecyContainer.get(AccionistasServerRepository.class);
    }

    public UUID save(String accionistaName, TipoAccionista tipoAccionista, String empresaName, int cantidad, double precioApertura){
        String date = DATE_FORMATER_LEGACY.format(new Date());
        UUID idAccionista = UUID.randomUUID();

        this.save(new AccionistaServer(
                idAccionista, accionistaName, tipoAccionista, empresaName, cantidad, precioApertura, date
        ));

        return idAccionista;
    }

    public void save(AccionistaServer accionistaEmpresaServer) {
        this.repositoryDb.save(accionistaEmpresaServer);
    }

    public AccionistaServer getById(UUID id) {
        return this.repositoryDb.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Accion no encontrada"));
    }

    public List<AccionistaServer> findByNombreAccionista(String nombreAccionnists){
        return this.repositoryDb.findByNombreAccionista(nombreAccionnists);
    }

    public List<AccionistaServer> findByEmpresa(String empresa) {
        return this.repositoryDb.findByEmpresa(empresa);
    }

    public List<AccionistaServer> findByEmpresaTipoJugador(String empresa){
        return this.findByEmpresa(empresa).stream()
                .filter(AccionistaServer::esJugador)
                .toList();
    }

    public List<AccionistaServer> findByEmpresaTipoEmpresa(String empresa){
        return this.findByEmpresa(empresa).stream()
                .filter(AccionistaServer::esEmpresa)
                .toList();
    }

    public List<AccionistaServer> findByEmpresa(String empresa, Predicate<? super AccionistaServer> condition) {
        return this.findByEmpresa(empresa).stream()
                .filter(condition)
                .toList();
    }

    public void deleteById(UUID id) {
        this.repositoryDb.deleteById(id);
    }
}
