package es.serversurvival.empresas.empresas.eventlisteners;

import es.jaime.EventListener;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.empresas.ofertasaccionesserver._shared.domain.OfertaAccionServer;
import es.serversurvival.empresas.ofertasaccionesserver._shared.domain.TipoOfertante;
import es.serversurvival.empresas.ofertasaccionesserver.comprarofertasmercadoserver.EmpresaServerAccionCompradaEvento;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;

public final class OnCompraAccionServer {
    private final EmpresasService empresasService;

    public OnCompraAccionServer() {
        this.empresasService = DependecyContainer.get(EmpresasService.class);
    }

    @EventListener
    public void on (EmpresaServerAccionCompradaEvento evento) {
        OfertaAccionServer ofertaComprada = evento.getOferta();

        if(evento.getOferta().getTipoOfertante() == TipoOfertante.EMPRESA){
            Empresa empresa = this.empresasService.getEmpresaByNombre(ofertaComprada.getEmpresa());

            empresasService.save(empresa.incrementPixelcoinsBy(evento.getPixelcoins())
                    .incrementIngresosBy(evento.getPixelcoins()));
        }
    }
}
