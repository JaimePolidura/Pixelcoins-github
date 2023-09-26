package es.serversurvival.pixelcoins.lootbox._shared.propiedad.domain;

import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.pixelcoins.lootbox._shared.items.domain.LootboxItemSeleccionadaoResultado;
import es.serversurvival.pixelcoins.lootbox._shared.items.domain.LootboxTier;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
public final class LootboxEnPropiedad {
    @Getter private UUID lootboxEnPropiedadId;
    @Getter private UUID jugadorId;
    @Getter private LootboxTier tier;
    @Getter private LocalDateTime fechaCompra;
    @Getter private LootboxCompradaEstado estado;

    @Getter private UUID lootboxItemAbierto;
    @Getter private LocalDateTime fechaAbierto;
    @Getter private int cantidadResultado;

    public boolean pendienteDeAbrir() {
        return this.estado == LootboxCompradaEstado.PENDIENTE;
    }

    public LootboxEnPropiedad abrir(LootboxItemSeleccionadaoResultado lootboxItemResultado) {
        this.lootboxItemAbierto = lootboxItemResultado.getLootboxItemId();
        this.fechaAbierto = LocalDateTime.now();
        this.cantidadResultado = lootboxItemResultado.getCantidad();
        this.estado = LootboxCompradaEstado.ABIERTA;

        return this;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID lootboxEnPropiedadId;
        private UUID jugadorId;
        private LootboxTier tier;
        private LocalDateTime fechaCompra;
        private LootboxCompradaEstado estado;
        private UUID lootboxItemAbierto;
        private LocalDateTime fechaAbierto;

        public Builder() {
            this.lootboxEnPropiedadId = UUID.randomUUID();
            this.fechaCompra = LocalDateTime.now();
            this.estado = LootboxCompradaEstado.PENDIENTE;
            this.lootboxItemAbierto = Funciones.NULL_ID;
            this.fechaAbierto = Funciones.NULL_LOCALDATETIME;
        }

        public Builder lootboxEnPropiedadId(UUID lootboxEnPropiedadId) {
            this.lootboxEnPropiedadId = lootboxEnPropiedadId;
            return this;
        }

        public Builder jugadorId(UUID jugadorId) {
            this.jugadorId = jugadorId;
            return this;
        }

        public Builder tier(LootboxTier tier) {
            this.tier = tier;
            return this;
        }

        public LootboxEnPropiedad build() {
            return new LootboxEnPropiedad(lootboxEnPropiedadId, jugadorId, tier, fechaCompra, estado,
                    lootboxItemAbierto, fechaAbierto, 0);
        }
    }
}
