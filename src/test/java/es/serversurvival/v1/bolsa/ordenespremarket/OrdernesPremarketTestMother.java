package es.serversurvival.v1.bolsa.ordenespremarket;

import es.serversurvival.v1.bolsa.ordenespremarket._shared.domain.OrdenPremarket;
import es.serversurvival.v1.bolsa.ordenespremarket._shared.domain.TipoAccion;

import java.util.UUID;

public final class OrdernesPremarketTestMother {
    public static OrdenPremarket createOrdenPremarket(String jugdor, String nombreActivo){
        return new OrdenPremarket(UUID.randomUUID(), jugdor, nombreActivo, 1, TipoAccion.LARGO_COMPRA, UUID.randomUUID());
    }

    public static OrdenPremarket createOrdenPremarket(String jugdor, String nombreActivo, UUID posicionAbiertId){
        return new OrdenPremarket(UUID.randomUUID(), jugdor, nombreActivo, 1, TipoAccion.LARGO_COMPRA, posicionAbiertId);
    }

    public static OrdenPremarket createOrdenPremarket(String jugdor, String nombreActivo, UUID posicionAbiertId, TipoAccion tipoAccion){
        return new OrdenPremarket(UUID.randomUUID(), jugdor, nombreActivo, 1, tipoAccion, posicionAbiertId);
    }

    public static OrdenPremarket createOrdenPremarket(String jugdor, String nombreActivo, UUID posicionAbiertId, TipoAccion tipoAccion, int cantidad){
        return new OrdenPremarket(UUID.randomUUID(), jugdor, nombreActivo, cantidad, tipoAccion, posicionAbiertId);
    }
}
