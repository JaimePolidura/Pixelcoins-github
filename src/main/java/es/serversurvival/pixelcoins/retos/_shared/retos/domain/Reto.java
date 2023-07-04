package es.serversurvival.pixelcoins.retos._shared.retos.domain;

import es.serversurvival.pixelcoins.lootbox.LootboxTier;
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
    @Getter private RetoMapping mapping; //Utilizado para relacionar eventos con la entidad en la base de datos
    @Getter private ModuloReto moduloReto;
    @Getter private UUID retoAnteriorId;
    @Getter private UUID retoPadreProgresionId;
    @Getter private TipoReto tipo;
    @Getter private double cantidadRequerida;
    @Getter private TipoRecompensa tipoRecompensa;
    @Getter private double recomponsaPixelcoins;
    @Getter private LootboxTier lootboxTierRecompensa;
    @Getter private int nLootboxesRecompensa;

    public boolean esTipoProgresivo() {
        return this.tipo == TipoReto.PROGRESIVO;
    }

    public boolean esTipoIndependiente() {
        return this.tipo == TipoReto.INDEPENDIENTE;
    }
}
