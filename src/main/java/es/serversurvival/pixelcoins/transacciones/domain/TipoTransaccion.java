package es.serversurvival.pixelcoins.transacciones.domain;

import es.serversurvival.pixelcoins.jugadores.patrimonio.TipoCuentaPatrimonio;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static es.serversurvival.pixelcoins.jugadores.patrimonio.TipoCuentaPatrimonio.*;
import static es.serversurvival.pixelcoins.transacciones.domain.TipoTransaccion.CuentaAfectada.*;
import static es.serversurvival.pixelcoins.transacciones.domain.TipoTransaccion.CuentaAfectada.cuentaAfectadaPagador;

@AllArgsConstructor
public enum TipoTransaccion {
    JUGADORES_PAGO                   (cuentaAfectadaPagador(null, EFECTIVO),           cuentaAfectadaPagado(EFECTIVO, null)),
    JUGADORES_CAMBIO_INGRESAR_ITEM   (cuentaAfectadaPagador(null, EFECTIVO),           cuentaAfectadaPagado(EFECTIVO, null)),
    JUGADORES_CAMBIO_SACAR_ITEM      (cuentaAfectadaPagador(EFECTIVO, null),          cuentaAfectadaPagado(null, EFECTIVO)),
    JUGADORES_CAMBIO_SACAR_MAX_ITEM  (cuentaAfectadaPagador(EFECTIVO, null),          cuentaAfectadaPagado(null, EFECTIVO)),

    DEUDAS_PRIMER_DESEMBOLSO         (cuentaAfectadaPagador(DEUDA_ACREDOR, EFECTIVO),       cuentaAfectadaPagado(EFECTIVO, DEUDA_DEUDOR)),
    DEUDAS_CUOTA                     (cuentaAfectadaPagador(DEUDA_DEUDOR, EFECTIVO),        cuentaAfectadaPagado(EFECTIVO, DEUDA_ACREDOR)),
    DEUDAS_PAGO_COMPLETO             (cuentaAfectadaPagador(DEUDA_DEUDOR, EFECTIVO),        cuentaAfectadaPagado(EFECTIVO, DEUDA_ACREDOR)),
    DEUDAS_MERCADO_SECUNDARIO_COMPRA (cuentaAfectadaPagador(DEUDA_ACREDOR, EFECTIVO),       cuentaAfectadaPagado(EFECTIVO, DEUDA_ACREDOR)),
    DEUDAS_MERCADO_PRIMARIO_COMPRA   (cuentaAfectadaPagador(DEUDA_ACREDOR, EFECTIVO),       cuentaAfectadaPagado(EFECTIVO, DEUDA_DEUDOR)),

    EMPRESAS_PAGAR                   (cuentaAfectadaPagador(null, EFECTIVO),           cuentaAfectadaPagado(EFECTIVO, null)),
    EMPRESAS_CERRAR                  (cuentaAfectadaPagador(null, EFECTIVO),           cuentaAfectadaPagado(EFECTIVO, EMPRESAS_ACCIONES)),
    EMPRESAS_DEPOSITAR               (cuentaAfectadaPagador(EMPRESAS_ACCIONES, EFECTIVO),    cuentaAfectadaPagado(EFECTIVO, null)),
    EMPRESAS_DIVIDENDO               (cuentaAfectadaPagador(null, EFECTIVO),           cuentaAfectadaPagado(EFECTIVO, EMPRESAS_ACCIONES)),
    EMPRESAS_SUELDO                  (cuentaAfectadaPagador(EFECTIVO, null),           cuentaAfectadaPagado(EFECTIVO, null)),
    EMPRESAS_SACAR                   (cuentaAfectadaPagador(null, EFECTIVO),           cuentaAfectadaPagado(EFECTIVO, EMPRESAS_ACCIONES)),
    EMPRESAS_ACCIONES_COMPRA_JUGADOR (cuentaAfectadaPagador(EMPRESAS_ACCIONES, EFECTIVO),    cuentaAfectadaPagado(EFECTIVO, EMPRESAS_ACCIONES)),
    EMPRESAS_ACCIONES_COMPRA_EMISION (cuentaAfectadaPagador(EMPRESAS_ACCIONES, EFECTIVO),    cuentaAfectadaPagado(EFECTIVO, null)),
    EMPRESAS_ACCIONES_COMPRA_IPO     (cuentaAfectadaPagador(EMPRESAS_ACCIONES, EFECTIVO),    cuentaAfectadaPagado(EFECTIVO, null)),

    TIENDA_ITEM_MINECRAFT_COMPRA     (cuentaAfectadaPagador(null, EFECTIVO),            cuentaAfectadaPagado(EFECTIVO, null)),

    BOLSA_ABRIR_LARGO                (cuentaAfectadaPagador(BOLSA, EFECTIVO),               cuentaAfectadaPagado(null, null)),
    BOLSA_ABRIR_CORTO                (cuentaAfectadaPagador(BOLSA, EFECTIVO),               cuentaAfectadaPagado(null, null)),
    BOLSA_CERRAR_LARGO               (cuentaAfectadaPagador(null, null ),       cuentaAfectadaPagado(EFECTIVO, BOLSA)),
    BOLSA_CERRAR_CORTO               (cuentaAfectadaPagador(null, null ),       cuentaAfectadaPagado(EFECTIVO, BOLSA)),

    RETOS_RECOMPENSA_PIXELCOINS      (cuentaAfectadaPagador(null, null),        cuentaAfectadaPagado(EFECTIVO, null));

    @Getter private final CuentaAfectada cuentaAfectadaPagador;
    @Getter private final CuentaAfectada cuentaAfectadaPagado;

    @AllArgsConstructor
    public static class CuentaAfectada {
        @Getter private final TipoCuentaPatrimonio debe;
        @Getter private final TipoCuentaPatrimonio haber;

        public static CuentaAfectada cuentaAfectadaPagador(TipoCuentaPatrimonio debe, TipoCuentaPatrimonio haber) {
            return new CuentaAfectada(debe, haber);
        }

        public static CuentaAfectada cuentaAfectadaPagado(TipoCuentaPatrimonio debe, TipoCuentaPatrimonio haber) {
            return new CuentaAfectada(debe, haber);
        }
    }

}
