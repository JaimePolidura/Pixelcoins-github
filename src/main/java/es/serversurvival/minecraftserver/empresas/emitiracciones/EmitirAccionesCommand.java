package es.serversurvival.minecraftserver.empresas.emitiracciones;

import lombok.Getter;

public final class EmitirAccionesCommand {
    @Getter private String empresa;
    @Getter private int numeroNuevasAcciones;
    @Getter private double precioPorAccion;
}
