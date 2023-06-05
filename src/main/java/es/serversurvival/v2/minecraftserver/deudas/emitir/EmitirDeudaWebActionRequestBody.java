package es.serversurvival.v2.minecraftserver.deudas.emitir;

import es.serversurvival.v2.minecraftserver.webaction.messages.WebActionRequestBody;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class EmitirDeudaWebActionRequestBody extends WebActionRequestBody {
    @Getter private final double nominal;
    @Getter private final double interes;
    @Getter private final int numeroCuotasTotales;
    @Getter private final long periodoPagoCuota;
}
