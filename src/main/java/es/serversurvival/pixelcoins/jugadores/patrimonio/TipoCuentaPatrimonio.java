package es.serversurvival.pixelcoins.jugadores.patrimonio;

import lombok.Getter;

public enum TipoCuentaPatrimonio {
    EFECTIVO("Efectivo", 10, true),
    BOLSA("Bolsa", 9, true),
    DEUDA_ACREDOR("Deudas acredor", 7, true),
    DEUDA_DEUDOR("Deudas deudor", 6, false),
    EMPRESAS_ACCIONES("Empresas", 8, true);

    @Getter private final String alias;
    @Getter private final int showPrioriy;
    @Getter private final boolean esActivo;

    TipoCuentaPatrimonio(String alias, int showPrioriy, boolean esActivo) {
        this.alias = alias;
        this.showPrioriy = showPrioriy;
        this.esActivo = esActivo;
    }
}
