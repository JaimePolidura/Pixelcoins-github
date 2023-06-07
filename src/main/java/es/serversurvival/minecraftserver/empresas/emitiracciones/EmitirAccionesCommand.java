package es.serversurvival.minecraftserver.empresas.emitiracciones;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class EmitirAccionesCommand {
    @Getter private String empresa;
    @Getter private int numeroNuevasAcciones;
    @Getter private double precioPorAccion;
}
