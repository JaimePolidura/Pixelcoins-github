package es.serversurvival.v1.empresas.ofertasaccionesserver.venderofertaaccionaserver;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.IllegalQuantity;
import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.serversurvival.v1.empresas.accionistasserver._shared.application.AccionistasServerService;
import es.serversurvival.v1.empresas.accionistasserver._shared.domain.AccionistaServer;
import es.serversurvival.v1.empresas.ofertasaccionesserver._shared.application.OfertasAccionesServerService;
import lombok.AllArgsConstructor;

import java.util.UUID;

import static es.serversurvival.v1.empresas.accionistasserver._shared.domain.TipoAccionista.*;

@AllArgsConstructor
@UseCase
public final class VenderOfertaAccionServerUseCase {
    private final AccionistasServerService accionistasEmpresasServerService;
    private final OfertasAccionesServerService ofertasAccionesServerService;
    private final EventBus eventBus;

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
            throw new IllegalQuantity("La cantidad de cantidad a vender deben de ser menores que las que tengas");
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
