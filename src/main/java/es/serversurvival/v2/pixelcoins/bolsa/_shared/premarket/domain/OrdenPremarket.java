package es.serversurvival.v2.pixelcoins.bolsa._shared.premarket.domain;

import es.serversurvival.v2.pixelcoins.bolsa._shared.posiciones.TipoBolsaApuesta;
import es.serversurvival.v2.pixelcoins.bolsa._shared.posiciones.TipoPosicion;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
public final class OrdenPremarket {
    @Getter private final UUID ordenPremarketId;
    @Getter private final UUID jugadorId;
    @Getter private final LocalDateTime fechaCreacion;
    @Getter private final TipoPosicion tipoPosicion;
    @Getter private final int cantidad;
    @Getter private final TipoBolsaApuesta tipoBolsaApuesta;

    @Getter private final UUID activoBolsaId;

    @Getter private final UUID posicionAbiertaId;

    public static OrdenPremarketAbrirBuilder abrirBuilder() {
        return new OrdenPremarketAbrirBuilder();
    }

    public static OrdenPremarketCerrarBuilder cerrarBuilder() {
        return new OrdenPremarketCerrarBuilder();
    }

    public static abstract class AbstractOrdenPremarketBuilder<T extends AbstractOrdenPremarketBuilder> {
        protected UUID ordenPremarketId;
        protected UUID jugadorId;
        protected LocalDateTime fechaCreacion;
        protected TipoPosicion tipoPosicion;
        protected int cantidad;

        protected TipoBolsaApuesta tipoBolsaApuesta;
        protected UUID activoBolsaId;
        protected UUID posicionAbiertaId;

        public AbstractOrdenPremarketBuilder() {
            this.ordenPremarketId = UUID.randomUUID();
            this.fechaCreacion = LocalDateTime.now();
            this.tipoPosicion = tipoPosicion();
        }

        protected abstract TipoPosicion tipoPosicion();

        public T tipoBolsaApuesta(TipoBolsaApuesta tipoBolsaApuesta) {
            this.tipoBolsaApuesta = tipoBolsaApuesta;
            return (T) this;
        }

        public T jugadorId(UUID jugadorId) {
            this.jugadorId = jugadorId;
            return (T) this;
        }

        public T cantidad(int cantidad) {
            this.cantidad = cantidad;
            return (T) this;
        }

        public OrdenPremarket build() {
            return new OrdenPremarket(ordenPremarketId, jugadorId, fechaCreacion, tipoPosicion, cantidad,
                    tipoBolsaApuesta, activoBolsaId, posicionAbiertaId);
        }
    }

    public static class OrdenPremarketCerrarBuilder extends AbstractOrdenPremarketBuilder<OrdenPremarketCerrarBuilder> {
        @Override
        protected TipoPosicion tipoPosicion() {
            return TipoPosicion.CERRADO;
        }

        public OrdenPremarketCerrarBuilder posicionAbiertaId(UUID posicionAbiertaId) {
            this.posicionAbiertaId = posicionAbiertaId;
            return this;
        }
    }

    public static class OrdenPremarketAbrirBuilder extends AbstractOrdenPremarketBuilder<OrdenPremarketAbrirBuilder> {
        @Override
        protected TipoPosicion tipoPosicion() {
            return TipoPosicion.ABIERTO;
        }

        public OrdenPremarketAbrirBuilder activoBolsaId(UUID activoBolsaId) {
            this.activoBolsaId = activoBolsaId;
            return this;
        }
    }
}
