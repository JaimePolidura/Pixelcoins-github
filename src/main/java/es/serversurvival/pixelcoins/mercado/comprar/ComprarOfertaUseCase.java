package es.serversurvival.pixelcoins.mercado.comprar;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseHandler;
import es.serversurvival.pixelcoins.mercado._shared.custom.OfertaCustomCaller;
import es.serversurvival.pixelcoins.mercado._shared.custom.accion.OfertaCompradaListener;
import es.serversurvival.pixelcoins.mercado._shared.Oferta;
import es.serversurvival.pixelcoins.mercado._shared.OfertasService;
import es.serversurvival.pixelcoins.mercado._shared.OfertasValidator;
import es.serversurvival.pixelcoins.transacciones.domain.Transaccion;
import es.serversurvival.pixelcoins.transacciones.application.TransaccionesSaver;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class ComprarOfertaUseCase implements UseCaseHandler<ComprarOfertaParametros> {
    private final TransaccionesSaver transaccionesSaver;
    private final OfertaCustomCaller ofertaAccionCaller;
    private final OfertasValidator ofertasValidator;
    private final OfertasService ofertasService;

    @Override
    public void handle(ComprarOfertaParametros parametros) {
        ofertasValidator.tienePixelcoinsSuficientes(parametros.getOfertaId(), parametros.getJugadorId());
        ofertasValidator.noEsVendedor(parametros.getOfertaId(), parametros.getJugadorId());
        Oferta ofertaAComprar = ofertasService.getById(parametros.getOfertaId());
        ofertaAccionCaller.callCustomComprarValidator(ofertaAComprar, parametros.getJugadorId());

        ofertaAComprar = ofertaAComprar.decrementarCantidad();

        decrementarCantidadOBorrar(ofertaAComprar);
        transaccionesSaver.save(Transaccion.builder()
                .pagadoId(ofertaAComprar.getVendedorId())
                .pagadorId(parametros.getJugadorId())
                .pixelcoins(ofertaAComprar.getPrecio())
                .objeto(ofertaAComprar.getObjeto())
                .tipo(ofertaAComprar.getTipo().getTipoTransaccion())
                .build());

        ofertaAccionCaller.callAccion(OfertaCompradaListener.class, ofertaAComprar, parametros.getJugadorId());
    }

    private void decrementarCantidadOBorrar(Oferta ofertaAComprar) {
        if(ofertaAComprar.getCantidad() == 0)
            ofertasService.deleteById(ofertaAComprar.getOfertaId());
        else
            ofertasService.save(ofertaAComprar);
    }
}
