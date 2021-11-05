package es.serversurvival.bolsa.ofertasmercadoserver.vender;

import es.serversurvival.Pixelcoin;
import es.serversurvival.bolsa.llamadasapi.mysql.LlamadaApi;
import es.serversurvival.bolsa.ofertasmercadoserver.mysql.TipoOfertante;
import es.serversurvival.bolsa.posicionesabiertas.mysql.PosicionAbierta;
import es.serversurvival.bolsa.posicionesabiertas.venderlargo.PosicionVentaLargoEvento;
import es.serversurvival.bolsa.posicionescerradas.mysql.PosicionCerrada;
import es.serversurvival.shared.eventospixelcoins.PosicionCerradaEvento;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.transacciones.mySQL.Transaccion;
import org.bukkit.entity.Player;


public final class VenderOfertaAccionServerUseCase implements AllMySQLTablesInstances {
    public static final VenderOfertaAccionServerUseCase INSTANCE = new VenderOfertaAccionServerUseCase();

    private VenderOfertaAccionServerUseCase() {}

    public void vender(String playerName, PosicionAbierta posicionAVender, double precio) {
        ofertasMercadoServerMySQL.nueva(playerName, posicionAVender.getNombre_activo(), precio, posicionAVender.getCantidad(), TipoOfertante.JUGADOR, posicionAVender.getPrecio_apertura());
        posicionesAbiertasMySQL.borrarPosicionAbierta(posicionAVender.getId());
    }
}
