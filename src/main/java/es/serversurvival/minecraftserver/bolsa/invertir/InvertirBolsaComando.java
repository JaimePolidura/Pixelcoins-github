package es.serversurvival.minecraftserver.bolsa.invertir;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public final class InvertirBolsaComando {
    @Getter private String ticker;
    @Getter private int cantidad;
    @Getter private String tipoApuesta;
}
