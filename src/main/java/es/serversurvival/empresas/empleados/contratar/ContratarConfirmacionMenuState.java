package es.serversurvival.empresas.empleados.contratar;

import es.serversurvival.empresas.empleados._shared.domain.TipoSueldo;

public record ContratarConfirmacionMenuState(String enviadorJugadorNombre, String destinatarioJugadorNombre, String empresa,
        String cargo, double sueldo, TipoSueldo tipoSueldo) {
}
