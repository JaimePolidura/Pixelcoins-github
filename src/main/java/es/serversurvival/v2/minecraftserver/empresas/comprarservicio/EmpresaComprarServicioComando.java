package es.serversurvival.v2.minecraftserver.empresas.comprarservicio;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class EmpresaComprarServicioComando {
    @Getter private final String empresa;
    @Getter private final double precio;
}
