package es.serversurvival.empresas.empresas.ipo;

import lombok.Getter;

public final class IPOCommand {
    @Getter private String empresa;
    @Getter private int accionesTotales;
    @Getter private int accionesOwner;
    @Getter private double precioPorAccion;
}
