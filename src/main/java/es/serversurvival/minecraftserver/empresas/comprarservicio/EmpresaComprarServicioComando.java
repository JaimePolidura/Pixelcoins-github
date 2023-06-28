package es.serversurvival.minecraftserver.empresas.comprarservicio;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class EmpresaComprarServicioComando {
    @Getter private String empresa;
    @Getter private double precio;
}
