package es.serversurvival.empresas.empresas.ipo.prepare;

import es.serversurvival.empresas.empresas._shared.domain.Empresa;

public record EmpresaIPOConfirmMenuState(
        Empresa empresaToIpo,
        int accionesTotales,
        double precioPorAccion,
        int accionesOwner
) {
}
