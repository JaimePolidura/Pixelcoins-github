package es.serversurvival.pixelcoins.deudas.pagarcuotas;

import es.serversurvival.pixelcoins._shared.usecases.ParametrosUseCase;
import es.serversurvival.pixelcoins.deudas._shared.domain.Deuda;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class PagarDeudaCuotasParametros implements ParametrosUseCase {
    @Getter private final Deuda deuda;

    public static PagarDeudaCuotasParametros from(Deuda deuda){
        return new PagarDeudaCuotasParametros(deuda);
    }
}
