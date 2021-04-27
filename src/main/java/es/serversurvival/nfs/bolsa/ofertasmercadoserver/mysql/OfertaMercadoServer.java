package es.serversurvival.nfs.bolsa.ofertasmercadoserver.mysql;

import es.serversurvival.nfs.shared.mysql.TablaObjeto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class OfertaMercadoServer implements TablaObjeto {
    @Getter private final int id;
    @Getter private final String jugador;
    @Getter private final String empresa;
    @Getter private final double precio;
    @Getter private final int cantidad;
    @Getter private final String fecha;
    @Getter private final TipoOfertante tipo_ofertante;
    @Getter private final double precio_apertura;

    public boolean esTipoOfertanteJugador () {
        return this.tipo_ofertante == TipoOfertante.JUGADOR;
    }
}
