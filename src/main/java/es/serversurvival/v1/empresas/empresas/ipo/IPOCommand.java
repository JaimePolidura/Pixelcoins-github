package es.serversurvival.v1.empresas.empresas.ipo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public final class IPOCommand {
    @Getter private String empresa;
    @Getter private int accionesTotales;
    @Getter private int accionesOwner;
    @Getter private double precioPorAccion;
}
