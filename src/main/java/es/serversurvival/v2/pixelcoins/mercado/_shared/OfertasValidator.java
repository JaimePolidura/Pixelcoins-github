package es.serversurvival.v2.pixelcoins.mercado._shared;

import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.serversurvival.v1._shared.exceptions.NotEnoughPixelcoins;
import es.serversurvival.v2.pixelcoins.transacciones.TransaccionesService;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public final class OfertasValidator {
    private final TransaccionesService transaccionesService;
    private final OfertasService ofertasService;

    public void tienePixelcoinsSuficientes(UUID ofertaId, UUID jugadorId) {
        if(transaccionesService.getBalancePixelcions(jugadorId) < ofertasService.getById(ofertaId).getPrecio()){
            throw new NotEnoughPixelcoins("La empresa no tiene las suficientes pixelcoins");
        }
    }

    public void ofertaNoEsRepetida(TipoOferta tipoOferta, String objeto) {
        if(tipoOferta.isObjetoDeOfertaUnico() && ofertasService.findByObjetoAndTipo(objeto, tipoOferta).isPresent()){
            throw new NotEnoughPixelcoins("No puedes subir ofertas iguales");
        }
    }

    public void esVendedor(UUID ofertaId, UUID jugadorId) {
        if(!ofertasService.getById(ofertaId).getVendedorId().equals(jugadorId)) {
            throw new NotTheOwner("No eres el vendedor de la oferta");
        }
    }

    public void noEsVendedor(UUID ofertaId, UUID jugadorId) {
        if(ofertasService.getById(ofertaId).getVendedorId().equals(jugadorId)) {
            throw new NotTheOwner("Eres el vendedor de la oferta");
        }
    }
}
