package es.serversurvival.v2.minecraftserver.empresas.editarempleado;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class EditarEmpleadoComando {
    @Getter private String empresa;
    @Getter private String empleado;
    @Getter private String queSeEditar;
    @Getter private String nuevoValor;
}
