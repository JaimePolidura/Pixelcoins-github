package es.serversurvival.pixelcoins.bolsa.cerrar;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.ActivoBolsa;
import es.serversurvival.pixelcoins.bolsa._shared.posiciones.domain.TipoBolsaApuesta;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

public final class PosicionBolsaCerrada extends PixelcoinsEvento {
    @Getter private UUID jugadorId;
    @Getter private boolean premarket;
    @Getter private int cantidad;
    @Getter private double precioApertura;
    @Getter private double precioCierre;
    @Getter private double rentabilidad;
    @Getter private ActivoBolsa activoBolsa;
    @Getter private TipoBolsaApuesta tipoApuesta;
    @Getter private double pixelcoinsTotales;

    public PosicionBolsaCerrada(UUID jugadorId, int cantidad, double precioApertura, double precioCierre, double rentabilidad,
                                ActivoBolsa activoBolsa, TipoBolsaApuesta tipoApuesta, double pixelcoinsTotales) {
        this.pixelcoinsTotales = pixelcoinsTotales;
        this.precioApertura = precioApertura;
        this.precioCierre = precioCierre;
        this.rentabilidad = rentabilidad;
        this.activoBolsa = activoBolsa;
        this.tipoApuesta = tipoApuesta;
        this.jugadorId = jugadorId;
        this.cantidad = cantidad;
        this.premarket = false;
    }

    public PosicionBolsaCerrada(UUID jugadorId) {
        this.jugadorId = jugadorId;
        this.premarket = true;
    }
}
