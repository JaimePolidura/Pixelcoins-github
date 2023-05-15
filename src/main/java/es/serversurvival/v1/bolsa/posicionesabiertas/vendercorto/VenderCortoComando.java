package es.serversurvival.v1.bolsa.posicionesabiertas.vendercorto;

import lombok.Getter;

public class VenderCortoComando {
    @Getter private String ticker;
    @Getter private int cantidad;
}
