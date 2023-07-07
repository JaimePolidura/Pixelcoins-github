package es.serversurvival.pixelcoins.retos._shared.retos.domain;

import es.serversurvival.pixelcoins.lootbox.items.domain.LootboxTier;
import es.serversurvival.pixelcoins.retos._shared.retos.application.RetoMapping;
import es.serversurvival.pixelcoins.retos._shared.retos.domain.recompensas.TipoRecompensa;
import lombok.*;

import java.util.UUID;

@Builder
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class Reto {
    @Getter private UUID retoId;
    @Getter private String nombre;
    @Getter private String descripccion;
    @Getter private String logotipo;
    @Getter private RetoMapping mapping; //Utilizado para relacionar eventos con la entidad en la base de datos
    @Getter private ModuloReto modulo;
    @Getter private UUID retoPadreId;
    @Getter private UUID retoPadreProgresionId;
    @Getter private int posicionEnProgresion;
    @Getter private TipoReto tipo;
    @Getter private double cantidadRequerida;
    @Getter private String nombreUnidadCantidadRequerida;
    @Getter private FormatoCantidadRequerida formatoCantidadRequerida;
    @Getter private TipoRecompensa tipoRecompensa;
    @Getter private double recompensaPixelcoins;
    @Getter private LootboxTier lootboxTierRecompensa;
    @Getter private int nLootboxesRecompensa;
    @Getter private boolean tieneHijos;

    public boolean esTipoProgresivo() {
        return this.tipo == TipoReto.PROGRESIVO;
    }

    public boolean esIndependiente() {
        return this.tipo == TipoReto.INDEPENDIENTE;
    }
}
