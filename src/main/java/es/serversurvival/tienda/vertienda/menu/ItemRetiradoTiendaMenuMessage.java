package es.serversurvival.tienda.vertienda.menu;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class ItemRetiradoTiendaMenuMessage {
    @Getter private final int pageNumber;
    @Getter private final int slotItem;
}
