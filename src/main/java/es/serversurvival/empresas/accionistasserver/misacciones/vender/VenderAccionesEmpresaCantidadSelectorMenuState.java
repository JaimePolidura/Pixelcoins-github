package es.serversurvival.empresas.accionistasserver.misacciones.vender;

import java.util.UUID;

public record VenderAccionesEmpresaCantidadSelectorMenuState(int maxCantidadThatCanBeSold, UUID accionistaId, String empresaNombreVender) {
}
