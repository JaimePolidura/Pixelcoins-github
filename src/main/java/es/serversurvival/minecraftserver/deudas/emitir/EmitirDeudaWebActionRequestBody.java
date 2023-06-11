package es.serversurvival.minecraftserver.deudas.emitir;

import es.serversurvival.minecraftserver.webaction.messages.WebActionRequestBody;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class EmitirDeudaWebActionRequestBody extends WebActionRequestBody {
    @Getter private final double nominal;
    @Getter private final double interes;
    @Getter private final int numeroCuotasTotales;
    @Getter private final long periodoPagoCuotaEnSegundos;
}
