package es.serversurvival.pixelcoins.mercado._shared;

import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.serversurvival._shared.exceptions.NotEnoughPixelcoins;
import es.serversurvival.pixelcoins.transacciones.TransaccionesBalanceService;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public final class OfertasValidator {
    private final TransaccionesBalanceService transaccionesBalanceService;
    private final OfertasService ofertasService;

    public void tienePixelcoinsSuficientes(UUID ofertaId, UUID jugadorId) {
        if(transaccionesBalanceService.get(jugadorId) < ofertasService.getById(ofertaId).getPrecio()){
            throw new NotEnoughPixelcoins("La empresa no tiene las suficientes pixelcoins");
        }
    }

    public void ofertaNoEsRepetida(TipoOferta tipoOferta, String objeto) {
        if(!tipoOferta.isElObjetoPuedeEstarRepetido() && ofertasService.findByObjetoAndTipo(objeto, tipoOferta).isPresent()){
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
