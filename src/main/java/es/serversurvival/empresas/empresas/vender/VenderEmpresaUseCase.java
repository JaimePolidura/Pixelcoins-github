package es.serversurvival.empresas.empresas.vender;

import es.jaime.javaddd.domain.exceptions.IllegalQuantity;
import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.serversurvival.Pixelcoin;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.exceptions.NotEnoughPixelcoins;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.empresas.empleados._shared.domain.Empleado;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;
import es.serversurvival.jugadores._shared.newformat.application.JugadoresService;
import es.serversurvival.jugadores._shared.newformat.domain.Jugador;

public final class VenderEmpresaUseCase implements AllMySQLTablesInstances {
    private final EmpresasService empresasService;
    private final JugadoresService jugadoresService;

    public VenderEmpresaUseCase() {
        this.empresasService = DependecyContainer.get(EmpresasService.class);
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
    }

    public void vender (String vendedor, String comprador, double precioEmpresa, String nombreEmpresa) {
        this.ensureCorrectFormatPixelcoins(precioEmpresa);
        var empresa = empresasService.getEmpresaByNombre(nombreEmpresa);
        this.ensureOwnerOfEmpresa(vendedor, empresa);
        var jugadorComprador = this.jugadoresService.getJugadorByNombre(comprador);
        var jugadorVendedor = this.jugadoresService.getJugadorByNombre(vendedor);
        this.ensureVendedorHasEnoughPixelcoins(jugadorVendedor, precioEmpresa);

        this.empresasService.save(empresa.withOwner(comprador));
        this.jugadoresService.realizarTransferenciaConEstadisticas(jugadorComprador, jugadorVendedor, precioEmpresa);

        Empleado emplado = empleadosMySQL.getEmpleado(comprador, nombreEmpresa);
        if(emplado != null)
            empleadosMySQL.borrarEmplado(emplado.getEmpleadoId());

        Pixelcoin.publish(new EmpresaVendedidaEvento(comprador, vendedor, precioEmpresa, nombreEmpresa));
    }

    private void ensureVendedorHasEnoughPixelcoins(Jugador vendedor, double pixelcoins){
        if(vendedor.getPixelcoins() < pixelcoins)
            throw new NotEnoughPixelcoins("No tienes las suficientes pixelcoins");
    }

    private void ensureOwnerOfEmpresa(String payerName, Empresa empresa){
        if(!empresa.getOwner().equalsIgnoreCase(payerName))
            throw new NotTheOwner("El vendedor no es el owner de la empresa");
    }

    private void ensureCorrectFormatPixelcoins(double pixelcoins){
        if(pixelcoins <= 0)
            throw new IllegalQuantity("Las pixelcoins han de ser un numero natural");
    }
}
