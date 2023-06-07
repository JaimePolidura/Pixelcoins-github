package es.serversurvival.minecraftserver.empresas.ipo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class EmpresaIPOComando {
    @Getter private String empresa;
    @Getter private double precioPorAccion;
    @Getter private int numeroAccionesEnPropiedadAVender;
}
