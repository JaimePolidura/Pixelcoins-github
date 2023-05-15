package es.serversurvival.v1.empresas.accionistasserver.misacciones.vender;

import java.util.UUID;

public record VenderAccionesEmpresaCantidadSelectorMenuState(int maxCantidadThatCanBeSold, UUID accionistaId, String empresaNombreVender) {
    public static VenderAccionesEmpresaCantidadSelectorMenuState of(int maxCantidadThatCanBeSold, UUID accionistaId, String empresaNombreVender){
        return new VenderAccionesEmpresaCantidadSelectorMenuState(maxCantidadThatCanBeSold, accionistaId, empresaNombreVender);
    }
}
