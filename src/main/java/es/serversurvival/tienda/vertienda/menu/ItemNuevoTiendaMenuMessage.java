package es.serversurvival.tienda.vertienda.menu;

import es.serversurvival.tienda._shared.domain.TiendaObjeto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class ItemNuevoTiendaMenuMessage {
    @Getter private final TiendaObjeto tiendaObjeto;
}
