package es.serversurvival.v2.pixelcoins.bolsa._shared.posiciones;

import es.serversurvival.v2.pixelcoins.bolsa._shared.activos.dominio.CortoTipoApuestaService;
import es.serversurvival.v2.pixelcoins.bolsa._shared.activos.dominio.LargoTipoApuestaService;
import es.serversurvival.v2.pixelcoins.bolsa._shared.activos.dominio.TipoApuestaService;
import es.serversurvival.v2.pixelcoins.transacciones.TipoTransaccion;
import lombok.Getter;

public enum TipoBolsaApuesta {
    LARGO(LargoTipoApuestaService.class, TipoTransaccion.BOLSA_ABRIR_LARGO, TipoTransaccion.BOLSA_CERRAR_LARGO),
    CORTO(CortoTipoApuestaService.class, TipoTransaccion.BOLSA_ABRIR_CORTO, TipoTransaccion.BOLSA_CERRAR_CORTO);

    @Getter private final Class<? extends TipoApuestaService> tipoApuestaService;
    @Getter private final TipoTransaccion tipoTransaccionAbrir;
    @Getter private final TipoTransaccion tipoTransaccionCerrar;

    TipoBolsaApuesta(Class<? extends TipoApuestaService> tipoApuestaService, TipoTransaccion tipoTransaccionAbrir, TipoTransaccion tipoTransaccionCerrar) {
        this.tipoApuestaService = tipoApuestaService;
        this.tipoTransaccionAbrir = tipoTransaccionAbrir;
        this.tipoTransaccionCerrar = tipoTransaccionCerrar;
    }
}
