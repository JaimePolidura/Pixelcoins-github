package es.serversurvival.empresas.ofertasaccionesserver._shared.domain;

import es.jaime.javaddd.domain.Aggregate;
import es.serversurvival._shared.mysql.TablaObjeto;
import es.serversurvival.empresas.accionistasempresasserver._shared.domain.TipoAccionista;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class OfertaAccionServer extends Aggregate implements TablaObjeto {
    @Getter private final UUID ofertasAccionesServerId;
    @Getter private final String nombreOfertante;
    @Getter private final String empresa;
    @Getter private final double precio;
    @Getter private final int cantidad;
    @Getter private final String fecha;
    @Getter private final TipoAccionista tipoOfertante;
    @Getter private final double precioApertura;

    public OfertaAccionServer withNombreOfertante(String nombreOfertante){
        return new OfertaAccionServer(ofertasAccionesServerId, nombreOfertante, empresa, precio,
                cantidad, fecha, tipoOfertante, precioApertura);
    }

    public OfertaAccionServer withCantidad(int cantidad){
        return new OfertaAccionServer(ofertasAccionesServerId, nombreOfertante, empresa, precio,
                cantidad, fecha, tipoOfertante, precioApertura);
    }

    public boolean esTipoOfertanteJugador () {
        return this.tipoOfertante == TipoAccionista.JUGADOR;
    }
}
