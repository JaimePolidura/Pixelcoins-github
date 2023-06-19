package es.serversurvival.pixelcoins.jugadores.patrimonio;

import lombok.Getter;

public enum TipoCuentaPatrimonio {
    EFECTIVO("Efectivo", 10),
    BOLSA("Bolsa", 9),
    DEUDA_ACREDOR("Deudas acredor", 7),
    DEUDA_DEUDOR("Deudas deudor", 6),
    EMPRESAS_ACCIONES("Empresas", 8);

    @Getter private final String alias;
    @Getter private final int showPrioriy;

    TipoCuentaPatrimonio(String alias, int showPrioriy) {
        this.alias = alias;
        this.showPrioriy = showPrioriy;
    }
}
