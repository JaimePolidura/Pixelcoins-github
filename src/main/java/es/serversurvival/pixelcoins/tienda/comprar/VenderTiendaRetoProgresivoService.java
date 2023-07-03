package es.serversurvival.pixelcoins.tienda.comprar;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.pixelcoins.retos._shared.retos.domain.RetoProgresivoService;
import es.serversurvival.pixelcoins.transacciones.domain.TipoTransaccion;
import es.serversurvival.pixelcoins.transacciones.application.TransaccionesBalanceService;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Service
@AllArgsConstructor
public final class VenderTiendaRetoProgresivoService implements RetoProgresivoService {
    private final TransaccionesBalanceService transaccionesBalanceService;

    @Override
    public double getCantidad(UUID vendedorId, Object otro) {
        return transaccionesBalanceService.get(TipoTransaccion.TIENDA_ITEM_MINECRAFT_COMPRA, vendedorId);
    }
}
