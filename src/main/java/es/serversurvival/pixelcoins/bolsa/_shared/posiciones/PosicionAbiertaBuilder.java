package es.serversurvival.pixelcoins.bolsa._shared.posiciones;

import java.time.LocalDateTime;
import java.util.UUID;

public final class PosicionAbiertaBuilder {
    private UUID posicionId;
    private UUID activoBolsaId;
    private UUID jugadorId;
    private int cantidad;
    private TipoBolsaApuesta tipoApuesta;
    private TipoPosicion tipoPosicion;
    private double precioApertura;
    private LocalDateTime fechaApertura;

    public PosicionAbiertaBuilder() {
        this.posicionId = UUID.randomUUID();
        this.tipoPosicion = TipoPosicion.ABIERTO;
        this.fechaApertura = LocalDateTime.now();
    }

    public static PosicionAbiertaBuilder builder() {
        return new PosicionAbiertaBuilder();
    }

    public Posicion build() {
        return new Posicion(posicionId, activoBolsaId, jugadorId, cantidad, tipoApuesta, tipoPosicion, precioApertura,
                fechaApertura, 0, null);
    }

    public PosicionAbiertaBuilder precioApertura(double precioApertura) {
        this.precioApertura = precioApertura;
        return this;
    }

    public PosicionAbiertaBuilder posicionId(UUID posicionId) {
        this.posicionId = posicionId;
        return this;
    }

    public PosicionAbiertaBuilder tipoApuesta(TipoBolsaApuesta tipoApuesta) {
        this.tipoApuesta = tipoApuesta;
        return this;
    }

    public PosicionAbiertaBuilder cantidad(int activoBolsaId) {
        this.cantidad = cantidad;
        return this;
    }

    public PosicionAbiertaBuilder activoBolsaId(UUID activoBolsaId) {
        this.activoBolsaId = activoBolsaId;
        return this;
    }

    public PosicionAbiertaBuilder jugadorId(UUID jugadorId) {
        this.jugadorId = jugadorId;
        return this;
    }
}
