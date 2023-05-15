package es.serversurvival.v1.empresas.empleados.contratar;

import es.serversurvival.v1.empresas.empleados._shared.domain.TipoSueldo;

public record ContratarConfirmacionMenuState(String enviadorJugadorNombre, String destinatarioJugadorNombre, String empresa,
        String cargo, double sueldo, TipoSueldo tipoSueldo) {
}
