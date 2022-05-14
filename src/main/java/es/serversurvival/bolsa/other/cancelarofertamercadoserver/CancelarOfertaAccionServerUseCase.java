package es.serversurvival.bolsa.other.cancelarofertamercadoserver;

import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.other._shared.llamadasapi.mysql.TipoActivo;
import es.serversurvival.bolsa.other._shared.ofertasmercadoserver.mysql.OfertaMercadoServer;
import es.serversurvival.bolsa.posicionescerradas._shared.domain.TipoPosicion;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesAbiertasSerivce;

public final class CancelarOfertaAccionServerUseCase implements AllMySQLTablesInstances {
    private final PosicionesAbiertasSerivce posicionesAbiertasSerivce;

    public CancelarOfertaAccionServerUseCase() {
        this.posicionesAbiertasSerivce = DependecyContainer.get(PosicionesAbiertasSerivce.class);
    }

    public void cancelar(String player, int id) {
        OfertaMercadoServer oferta = ofertasMercadoServerMySQL.get(id);

        ofertasMercadoServerMySQL.borrar(id);
        posicionesAbiertasSerivce.save(player, TipoActivo.ACCIONES_SERVER, oferta.getEmpresa(), oferta.getCantidad(),
                oferta.getPrecio_apertura(), TipoPosicion.LARGO);
    }


}
