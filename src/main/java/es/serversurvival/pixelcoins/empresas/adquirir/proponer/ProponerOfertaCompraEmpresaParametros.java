package es.serversurvival.pixelcoins.empresas.adquirir.proponer;

import es.serversurvival.pixelcoins._shared.usecases.ParametrosUseCase;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class ProponerOfertaCompraEmpresaParametros implements ParametrosUseCase {
    private final UUID compradorId;
    private final TipoComprador tipoComprador;
    private final double precioTotal;
    private final UUID empresaAComprarId;
    private final UUID jugadorIdIniciador;

    public enum TipoComprador {
        JUGADOR,
        EMPRESA
    }
}
