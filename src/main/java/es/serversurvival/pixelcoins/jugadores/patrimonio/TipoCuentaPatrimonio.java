package es.serversurvival.pixelcoins.jugadores.patrimonio;

import lombok.Getter;

public enum TipoCuentaPatrimonio {
    EFECTIVO("Efectivo"),
    BOLSA("Bolsa"),
    DEUDA_ACREDOR("Deudas acredor"),
    DEUDA_DEUDOR("Deudas deudor"),
    EMPRESAS_ACCIONES("Empresas");

    @Getter private final String alias;

    TipoCuentaPatrimonio(String alias) {
        this.alias = alias;
    }
}
