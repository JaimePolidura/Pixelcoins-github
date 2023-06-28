package es.serversurvival.minecraftserver.empresas.despedir;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class DespedirEmpleadoComando {
    @Getter private String empresa;
    @Getter private String empleado;
    @Getter private String causaDespido;
}
