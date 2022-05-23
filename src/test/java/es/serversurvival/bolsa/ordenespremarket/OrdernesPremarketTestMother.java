package es.serversurvival.bolsa.ordenespremarket;

import es.serversurvival.bolsa.ordenespremarket._shared.domain.OrdenPremarket;

import java.util.UUID;

import static es.serversurvival.bolsa.ordenespremarket._shared.domain.TipoAccion.LARGO_COMPRA;

public final class OrdernesPremarketTestMother {
    public static OrdenPremarket createOrdenPremarket(String jugdor, String nombreActivo){
        return new OrdenPremarket(UUID.randomUUID(), jugdor, nombreActivo, 1, LARGO_COMPRA, UUID.randomUUID());
    }

    public static OrdenPremarket createOrdenPremarket(String jugdor, String nombreActivo, UUID posicionAbiertId){
        return new OrdenPremarket(UUID.randomUUID(), jugdor, nombreActivo, 1, LARGO_COMPRA, posicionAbiertId);
    }
}
