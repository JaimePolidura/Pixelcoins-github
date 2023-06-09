package es.serversurvival.pixelcoins.bolsa._shared.posiciones.domain;

import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.TipoApuestaService;
import es.serversurvival.pixelcoins.bolsa._shared.posiciones.application.CortoTipoApuestaService;
import es.serversurvival.pixelcoins.bolsa._shared.posiciones.application.LargoTipoApuestaService;
import es.serversurvival.pixelcoins.transacciones.TipoTransaccion;
import lombok.Getter;
import org.bukkit.Material;

public enum TipoBolsaApuesta {
    LARGO(LargoTipoApuestaService.class, TipoTransaccion.BOLSA_ABRIR_LARGO, TipoTransaccion.BOLSA_CERRAR_LARGO, Material.NAME_TAG),
    CORTO(CortoTipoApuestaService.class, TipoTransaccion.BOLSA_ABRIR_CORTO, TipoTransaccion.BOLSA_CERRAR_CORTO, Material.REDSTONE_TORCH);

    @Getter private final Class<? extends TipoApuestaService> tipoApuestaService;
    @Getter private final TipoTransaccion tipoTransaccionAbrir;
    @Getter private final TipoTransaccion tipoTransaccionCerrar;
    @Getter private final Material material;

    TipoBolsaApuesta(Class<? extends TipoApuestaService> tipoApuestaService, TipoTransaccion tipoTransaccionAbrir,
                     TipoTransaccion tipoTransaccionCerrar, Material material) {
        this.tipoApuestaService = tipoApuestaService;
        this.tipoTransaccionAbrir = tipoTransaccionAbrir;
        this.tipoTransaccionCerrar = tipoTransaccionCerrar;
        this.material = material;
    }
}
