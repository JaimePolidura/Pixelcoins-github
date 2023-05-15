package es.serversurvival.v1.empresas.empresas.ipo.prepare;

import es.serversurvival.v1.empresas.empresas._shared.domain.Empresa;

public record EmpresaIPOConfirmMenuState(
        Empresa empresaToIpo,
        int accionesTotales,
        double precioPorAccion,
        int accionesOwner
) {
}
