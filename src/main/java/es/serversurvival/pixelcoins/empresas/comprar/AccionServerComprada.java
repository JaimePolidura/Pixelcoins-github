package es.serversurvival.pixelcoins.empresas.comprar;

import es.serversurvival._shared.eventospixelcoins.InvocaAUnReto;
import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.pixelcoins.mercado._shared.TipoOferta;
import es.serversurvival.pixelcoins.retos._shared.retos.application.RetoMapping;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
public final class AccionServerComprada extends PixelcoinsEvento implements InvocaAUnReto {
    @Getter private final UUID empresaId;
    @Getter private final UUID directorEmpresaId;
    @Getter private final UUID compradorJugadorId;
    @Getter private final double precioPorAccion;
    @Getter private final TipoOferta tipoOferta;
    @Getter private final UUID vendedorJugadorId;

    @Override
    public Map<UUID, List<RetoMapping>> retosByJugadorId() {
        if(vendedorJugadorId.equals(compradorJugadorId)) {
            return null;
        }

        Map<UUID, List<RetoMapping>> retos = new HashMap<>();
        retos.put(compradorJugadorId, List.of(RetoMapping.EMPRESAS_ACCIONISTAS_COMPRAR));

        if(tipoOferta == TipoOferta.ACCIONES_SERVER_IPO){
            retos.put(directorEmpresaId, List.of(RetoMapping.EMPRESAS_BOLSA_RECAUDAR_IPO));
        }
        if(tipoOferta == TipoOferta.ACCIONES_SERVER_EMISION){
            retos.put(directorEmpresaId, List.of(RetoMapping.EMPRESAS_BOLSA_RECAUDAR_EMISION));
        }
        if(tipoOferta == TipoOferta.ACCIONES_SERVER_JUGADOR){
            retos.put(vendedorJugadorId, List.of(RetoMapping.EMPRESAS_ACCIONISTAS_VENTA));
        }

        return retos;
    }
}
