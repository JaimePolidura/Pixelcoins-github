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

    public UUID getObjetoToUUID() {
        return UUID.fromString(objeto);
    }

    public static abstract class AbstractOfertaBuilder<T extends AbstractOfertaBuilder> {
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
            this.cantidad = 1;
        }

        public abstract Oferta build();

        public T tipoOferta(TipoOferta tipoOferta) {
            this.tipoOferta = tipoOferta;
            return (T) this;
        }

        public T objeto(UUID objeto) {
            this.objeto = objeto.toString();
            return (T) this;
        }

        public T objeto(String objeto) {
            this.objeto = objeto;
            return (T) this;
        }

        public T precio(double precio) {
            this.precio = precio;
            return (T) this;
        }

        public T cantidad(int cantidad) {
            this.cantidad = cantidad;
            return (T) this;
        }
        
        public T vendedorId(UUID vendedorId) {
            this.vendedorId = vendedorId;
            return (T) this;
        }
    }
}
