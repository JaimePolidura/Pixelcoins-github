package es.serversurvival.bolsa.ofertasmercadoserver.vender;

import es.serversurvival.bolsa.ofertasmercadoserver.mysql.TipoOfertante;
import es.serversurvival.bolsa.posicionesabiertas.mysql.PosicionAbierta;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;
import org.bukkit.entity.Player;


public final class VenderOfertaAccionServerUseCase implements AllMySQLTablesInstances {
    public static final VenderOfertaAccionServerUseCase INSTANCE = new VenderOfertaAccionServerUseCase();

    private VenderOfertaAccionServerUseCase() {}

    public void vender(Player player, PosicionAbierta posicionAVender, double precio) {
        ofertasMercadoServerMySQL.nueva(player.getName(), posicionAVender.getNombre_activo(), precio, posicionAVender.getCantidad(), TipoOfertante.JUGADOR, posicionAVender.getPrecio_apertura());
        posicionesAbiertasMySQL.borrarPosicionAbierta(posicionAVender.getId());
    }
}
