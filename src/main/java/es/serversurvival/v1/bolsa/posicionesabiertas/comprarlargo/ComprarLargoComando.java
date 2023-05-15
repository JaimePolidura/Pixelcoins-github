package es.serversurvival.v1.bolsa.posicionesabiertas.comprarlargo;

import lombok.Getter;

public class ComprarLargoComando {
    @Getter private String ticker;
    @Getter private int cantidad;
}
