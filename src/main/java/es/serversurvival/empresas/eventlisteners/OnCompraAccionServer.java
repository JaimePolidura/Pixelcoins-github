package es.serversurvival.empresas.eventlisteners;

import es.jaime.EventListener;
import es.serversurvival.bolsa.comprarofertasmercadoserver.EmpresaServerAccionCompradaEvento;
import es.serversurvival.bolsa.ofertasmercadoserver.mysql.OfertaMercadoServer;
import es.serversurvival.bolsa.ofertasmercadoserver.mysql.TipoOfertante;
import es.serversurvival.empresas._shared.mysql.Empresa;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;

public final class OnCompraAccionServer implements AllMySQLTablesInstances {
    @EventListener
    public void on (EmpresaServerAccionCompradaEvento evento) {
        OfertaMercadoServer ofertaComprada = evento.getOferta();

        if(evento.getOferta().getTipo_ofertante() == TipoOfertante.EMPRESA){
            Empresa empresa = empresasMySQL.getEmpresa(ofertaComprada.getEmpresa());

            empresasMySQL.setPixelcoins(empresa.getNombre(), empresa.getPixelcoins() + empresa.getPixelcoins());
            empresasMySQL.setIngresos(empresa.getNombre(), empresa.getIngresos() + evento.getPixelcoins());
        }
    }
}
