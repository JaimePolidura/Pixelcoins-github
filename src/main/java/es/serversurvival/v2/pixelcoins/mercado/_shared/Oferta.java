package es.serversurvival.v2.pixelcoins.mercado._shared;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
public abstract class Oferta {
    @Getter private final UUID ofertaId;
    @Getter private final UUID vendedorId;
    @Getter private final LocalDateTime fechaSubida;
    @Getter private int cantidad;
    @Getter private final double precio;
    @Getter private final String objeto;
    @Getter private final TipoOferta tipoOferta;

    public Oferta decrementarCantidad() {
        this.cantidad--;
        return this;
    }

    public UUID objetoToUUID() {
        return UUID.fromString(objeto);
    }

    public static abstract class AbstractOfertaBuilder {
        protected UUID ofertaId;
        protected UUID vendedorId;
        protected LocalDateTime fechaSubida;
        protected int cantidad;
        protected double precio;
        protected String objeto;
        protected TipoOferta tipoOferta;

        public AbstractOfertaBuilder() {
            this.ofertaId = UUID.randomUUID();
            this.fechaSubida = LocalDateTime.now();
        }

        public abstract Oferta build();

        public AbstractOfertaBuilder tipoOferta(TipoOferta tipoOferta) {
            this.tipoOferta = tipoOferta;
            return this;
        }

        public AbstractOfertaBuilder objeto(String objeto) {
            this.objeto = objeto;
            return this;
        }

        public AbstractOfertaBuilder precio(double precio) {
            this.precio = precio;
            return this;
        }

        public AbstractOfertaBuilder cantidad(int cantidad) {
            this.cantidad = cantidad;
            return this;
        }

        public AbstractOfertaBuilder vendedorId(UUID vendedorId) {
            this.vendedorId = vendedorId;
            return this;
        }
    }
}
