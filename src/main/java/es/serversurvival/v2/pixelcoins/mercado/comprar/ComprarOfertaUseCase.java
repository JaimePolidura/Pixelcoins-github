package es.serversurvival.v2.pixelcoins.mercado.comprar;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.serversurvival.v2.pixelcoins.mercado._shared.accion.OfertaAccionCaller;
import es.serversurvival.v2.pixelcoins.mercado._shared.accion.OfertaCompradaListener;
import es.serversurvival.v2.pixelcoins.mercado._shared.Oferta;
import es.serversurvival.v2.pixelcoins.mercado._shared.OfertasService;
import es.serversurvival.v2.pixelcoins.mercado._shared.OfertasValidator;
import es.serversurvival.v2.pixelcoins.transacciones.Transaccion;
import es.serversurvival.v2.pixelcoins.transacciones.TransaccionesService;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class ComprarOfertaUseCase {
    private final OfertaAccionCaller ofertaAccionCaller;
    private final TransaccionesService transaccionesService;
    private final OfertasValidator ofertasValidator;
    private final OfertasService ofertasService;

    public void comprarOferta(ComprarOfertaParametros parametros) {
        ofertasValidator.tienePixelcoinsSuficientes(parametros.getOfertaId(), parametros.getJugadorId());
        ofertasValidator.noEsVendedor(parametros.getOfertaId(), parametros.getJugadorId());

        Oferta ofertaAComprar = ofertasService.getById(parametros.getOfertaId())
                .decrementarCantidad();

        decrementarCantidadOBorrar(ofertaAComprar);
        transaccionesService.save(Transaccion.builder()
                        .pagadoId(ofertaAComprar.getVendedorId())
                        .pagadorId(parametros.getJugadorId())
                        .pixelcoins(ofertaAComprar.getPrecio())
                        .objeto(ofertaAComprar.getObjeto())
                        .tipo(ofertaAComprar.getTipoOferta().getTipoTransaccion())
                .build());

        ofertaAccionCaller.call(OfertaCompradaListener.class, ofertaAComprar, parametros.getJugadorId());
    }

    private void decrementarCantidadOBorrar(Oferta ofertaAComprar) {
        if(ofertaAComprar.getCantidad() == 1)
            ofertasService.deleteById(ofertaAComprar.getOfertaId());
        else
            ofertasService.save(ofertaAComprar);
    }
}
