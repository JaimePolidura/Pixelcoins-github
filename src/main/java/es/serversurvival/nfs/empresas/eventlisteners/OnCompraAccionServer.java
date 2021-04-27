package es.serversurvival.nfs.empresas.eventlisteners;

import es.jaime.EventListener;
import es.serversurvival.legacy.mySQL.eventos.empresas.EmpresaServerAccionCompradaEvento;
import es.serversurvival.nfs.bolsa.ofertasmercadoserver.mysql.OfertaMercadoServer;
import es.serversurvival.nfs.bolsa.ofertasmercadoserver.mysql.TipoOfertante;
import es.serversurvival.nfs.empresas.mysql.Empresa;
import es.serversurvival.nfs.shared.mysql.AllMySQLTablesInstances;

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
