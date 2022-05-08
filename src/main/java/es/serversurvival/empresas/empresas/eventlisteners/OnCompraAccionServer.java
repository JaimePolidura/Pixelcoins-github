package es.serversurvival.empresas.empresas.eventlisteners;

import es.jaime.EventListener;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa._shared.ofertasmercadoserver.mysql.OfertaMercadoServer;
import es.serversurvival.bolsa._shared.ofertasmercadoserver.mysql.TipoOfertante;
import es.serversurvival.bolsa.comprarofertasmercadoserver.EmpresaServerAccionCompradaEvento;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;

public final class OnCompraAccionServer implements AllMySQLTablesInstances {
    private final EmpresasService empresasService;

    public OnCompraAccionServer() {
        this.empresasService = DependecyContainer.get(EmpresasService.class);
    }

    @EventListener
    public void on (EmpresaServerAccionCompradaEvento evento) {
        OfertaMercadoServer ofertaComprada = evento.getOferta();

        if(evento.getOferta().getTipo_ofertante() == TipoOfertante.EMPRESA){
            Empresa empresa = this.empresasService.getEmpresaByNombre(ofertaComprada.getEmpresa());

            empresasService.save(empresa.incrementPixelcoinsBy(evento.getPixelcoins())
                    .incrementIngresosBy(evento.getPixelcoins()));
        }
    }
}
