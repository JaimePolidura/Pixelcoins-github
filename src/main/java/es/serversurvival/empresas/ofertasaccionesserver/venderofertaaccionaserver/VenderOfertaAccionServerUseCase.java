package es.serversurvival.empresas.ofertasaccionesserver.venderofertaaccionaserver;

import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.IllegalQuantity;
import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.empresas.accionistasserver._shared.application.AccionistasServerService;
import es.serversurvival.empresas.accionistasserver._shared.domain.AccionistaServer;
import es.serversurvival.empresas.accionistasserver._shared.domain.TipoAccionista;
import es.serversurvival.empresas.ofertasaccionesserver._shared.application.OfertasAccionesServerService;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import lombok.AllArgsConstructor;

import java.util.UUID;

import static es.serversurvival.empresas.accionistasserver._shared.domain.TipoAccionista.*;


@AllArgsConstructor
public final class VenderOfertaAccionServerUseCase {
    private final AccionistasServerService accionistasEmpresasServerService;
    private final OfertasAccionesServerService ofertasAccionesServerService;
    private final EventBus eventBus;

    public VenderOfertaAccionServerUseCase() {
        this.accionistasEmpresasServerService = DependecyContainer.get(AccionistasServerService.class);
        this.ofertasAccionesServerService = DependecyContainer.get(OfertasAccionesServerService.class);
        this.eventBus = DependecyContainer.get(EventBus.class);
    }

    public void vender(String vendedorNombre, UUID accionEmpresServerId, double precio, int cantidad) {
        this.ensureCantidadAndPrecioCorrectFormat(precio, cantidad);
        var accionEmpresaServer = this.accionistasEmpresasServerService.getById(accionEmpresServerId);
        this.ensureCantidadNotBiggerThanAccionEmpresaServer(cantidad, accionEmpresaServer);
        this.ensureOwnsAccion(vendedorNombre, accionEmpresaServer);

        this.ofertasAccionesServerService.save(
                vendedorNombre, accionEmpresaServer.getEmpresa(), precio, cantidad, JUGADOR,
                accionEmpresaServer.getPrecioApertura(), accionEmpresServerId
        );

        this.eventBus.publish(NuevaOfertaAccionServer.of(vendedorNombre, accionEmpresaServer.getEmpresa(), JUGADOR,
                precio, cantidad));
    }

    private void ensureCantidadNotBiggerThanAccionEmpresaServer(int cantidad, AccionistaServer accionEmpresaServer){
        if(accionEmpresaServer.getCantidad() < cantidad){
            throw new IllegalQuantity("La cantidad de acciones a vender deben de ser menores que las que tengas");
        }
    }

    private void ensureCantidadAndPrecioCorrectFormat(double precio, int cantidad){
        if(precio <= 0 || cantidad <= 0)
            throw new IllegalQuantity("El price y la cantiadad deben ser mas grandes que 0");
    }

    private void ensureOwnsAccion(String jugadorNombre, AccionistaServer accionEmpresaServer){
        if(!jugadorNombre.equalsIgnoreCase(accionEmpresaServer.getNombreAccionista()))
            throw new NotTheOwner("No eres el owner de la accion");
    }
}
