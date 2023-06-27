package es.serversurvival.minecraftserver.deudas.emitir;

import es.serversurvival.minecraftserver.webaction.messages.WebActionFormParam;
import es.serversurvival.minecraftserver.webaction.messages.WebActionRequestBody;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class EmitirDeudaWebActionRequestBody extends WebActionRequestBody {
    @Getter
    @WebActionFormParam(showPriory = 10, desc = "Pixelcoins con las que te vas a endeudar")
    private double pixelcoins;

    @Getter
    @WebActionFormParam(desc = "Interes de la deuda, expresado en %", showPriory = 9)
    private double interes;

    @Getter
    @WebActionFormParam(desc = "Cada cuantos dias tendras que hacer pagos de la deuda", showPriory = 7)
    private int periodoPagoCuotaEnDias;

    @Getter
    @WebActionFormParam(desc = "Numero veces totales de pagos que vas a tener que hacer", showPriory = 6)
    private int numeroCuotasTotales;
}
