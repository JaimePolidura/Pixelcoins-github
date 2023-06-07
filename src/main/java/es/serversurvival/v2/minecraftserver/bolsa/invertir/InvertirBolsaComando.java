package es.serversurvival.v2.minecraftserver.bolsa.invertir;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class InvertirBolsaComando {
    @Getter private String ticker;
    @Getter private int cantidad;
    @Getter private String tipoApuesta;
}
