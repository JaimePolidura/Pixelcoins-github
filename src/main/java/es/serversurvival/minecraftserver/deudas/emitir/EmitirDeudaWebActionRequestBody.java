package es.serversurvival.minecraftserver.deudas.emitir;

import es.serversurvival.minecraftserver.webaction.messages.WebActionRequestBody;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class EmitirDeudaWebActionRequestBody extends WebActionRequestBody {
    @Getter private long periodoPagoCuotaEnDias;
    @Getter private int numeroCuotasTotales;
    @Getter private double pixelcoins;
    @Getter private double interes;
}
