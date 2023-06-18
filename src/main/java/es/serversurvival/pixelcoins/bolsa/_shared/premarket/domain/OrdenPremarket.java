package es.serversurvival.pixelcoins.bolsa._shared.premarket.domain;

import es.serversurvival.pixelcoins.bolsa._shared.posiciones.domain.TipoBolsaApuesta;
import es.serversurvival.pixelcoins.bolsa._shared.posiciones.domain.TipoPosicion;
import es.serversurvival.pixelcoins.bolsa.abrir.AbrirPosicoinBolsaParametros;
import es.serversurvival.pixelcoins.bolsa.cerrar.CerrarPosicionParametros;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
public final class OrdenPremarket {
    @Getter private UUID ordenPremarketId;
    @Getter private UUID jugadorId;
    @Getter private LocalDateTime fechaCreacion;
    @Getter private TipoPosicion tipoPosicion;
    @Getter private int cantidad;
    @Getter private TipoBolsaApuesta tipoBolsaApuesta;

    @Getter private UUID activoBolsaId;

    @Getter private UUID posicionAbiertaId;

    public AbrirPosicoinBolsaParametros toAbrirPosicoinBolsaParametros() {
        return new AbrirPosicoinBolsaParametros(jugadorId, tipoBolsaApuesta, activoBolsaId, cantidad);
    }

    public CerrarPosicionParametros toCerrarPosicionParametros() {
        return new CerrarPosicionParametros(jugadorId, cantidad, posicionAbiertaId);
    }

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
