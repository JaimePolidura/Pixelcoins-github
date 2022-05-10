package es.serversurvival.web.cuentasweb._shared.application;

import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.web.cuentasweb._shared.domain.CuentaWeb;
import es.serversurvival.web.cuentasweb._shared.domain.CuentasWebRepository;

import java.util.UUID;

public final class CuentasWebService {
    private final CuentasWebRepository repositoryDb;

    public CuentasWebService() {
        this.repositoryDb = DependecyContainer.get(CuentasWebRepository.class);
    }

    public void save(CuentaWeb cuentaWeb) {
        this.repositoryDb.save(cuentaWeb);
    }

    public CuentaWeb getById(UUID id) {
        return this.repositoryDb.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Cuenta no encontrada"));
    }

    public CuentaWeb getByUsername(String username) {
        return this.repositoryDb.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFound("Cuenta no encontrada"));
    }


}
