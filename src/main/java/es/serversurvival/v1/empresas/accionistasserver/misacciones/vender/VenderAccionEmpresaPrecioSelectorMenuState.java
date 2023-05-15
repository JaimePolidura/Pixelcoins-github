package es.serversurvival.v1.empresas.accionistasserver.misacciones.vender;

import es.serversurvival.v1.empresas.accionistasserver._shared.domain.AccionistaServer;

public record VenderAccionEmpresaPrecioSelectorMenuState(int cantidadAVender, AccionistaServer accionAVender) {
    public static VenderAccionEmpresaPrecioSelectorMenuState from(int cantidadAVender, AccionistaServer accionAVender) {
        return new VenderAccionEmpresaPrecioSelectorMenuState(cantidadAVender, accionAVender);
    }
}
