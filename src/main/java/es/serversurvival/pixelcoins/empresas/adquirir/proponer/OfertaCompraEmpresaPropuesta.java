package es.serversurvival.pixelcoins.empresas.adquirir.proponer;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class OfertaCompraEmpresaPropuesta extends PixelcoinsEvento {
    private final UUID compradorId;
    private final ProponerOfertaCompraEmpresaParametros.TipoComprador tipoComprador;
    private final double precioTotal;
    private final UUID empresaAComprarId;
    private final UUID jugadorId;
}
