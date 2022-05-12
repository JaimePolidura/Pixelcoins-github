package es.serversurvival.bolsa.other.cancelarorderpremarket;

import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.bolsa.ordenespremarket._shared.application.OrdenesPremarketService;

import java.util.UUID;

public final class CancelarOrdenUseCase {
    private final OrdenesPremarketService ordenesPremarketService;

    public CancelarOrdenUseCase(){
        this.ordenesPremarketService = DependecyContainer.get(OrdenesPremarketService .class);
    }

    public void cancelar (UUID id) {
        this.ordenesPremarketService.deleteById(id);
    }
}
