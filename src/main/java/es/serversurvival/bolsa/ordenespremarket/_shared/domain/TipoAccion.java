package es.serversurvival.bolsa.ordenespremarket._shared.domain;

public enum TipoAccion {
    LARGO_COMPRA(false),
    LARGO_VENTA(true),
    CORTO_COMPRA(true),
    CORTO_VENTA(false);

    private final boolean esCerrar;

    TipoAccion(boolean esCerrar) {
        this.esCerrar = esCerrar;
    }

    public boolean closingOperation() {
        return esCerrar;
    }
}
