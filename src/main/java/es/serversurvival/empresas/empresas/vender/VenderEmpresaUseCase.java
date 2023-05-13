package es.serversurvival.empresas.empresas.vender;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.IllegalQuantity;
import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.serversurvival._shared.exceptions.NotEnoughPixelcoins;
import es.serversurvival.empresas.empleados._shared.application.EmpleadosService;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class VenderEmpresaUseCase {
    private final EmpresasService empresasService;
    private final JugadoresService jugadoresService;
    private final EmpleadosService empleadosService;
    private final EventBus eventBus;

    public void vender (String vendedor, String comprador, double precioEmpresa, String nombreEmpresa) {
        this.ensureCorrectFormatPixelcoins(precioEmpresa);
        var empresa = empresasService.getByNombre(nombreEmpresa);
        this.ensureOwnerOfEmpresa(vendedor, empresa);
        var jugadorComprador = this.jugadoresService.getByNombre(comprador);
        var jugadorVendedor = this.jugadoresService.getByNombre(vendedor);
        this.ensureCompradorHasEnoughPixelcoins(jugadorComprador, precioEmpresa);

        this.empresasService.save(empresa.withOwner(comprador));
        this.jugadoresService.realizarTransferencia(jugadorComprador, jugadorVendedor, precioEmpresa);
        this.empleadosService.findByEmpresa(nombreEmpresa).stream()
                .filter(empleado -> empleado.getNombre().equalsIgnoreCase(comprador))
                .findFirst()
                .ifPresent(empleado -> this.empleadosService.deleteById(empleado.getEmpleadoId()));

        this.eventBus.publish(new EmpresaVendedida(comprador, vendedor, precioEmpresa, nombreEmpresa));
    }

    private void ensureCompradorHasEnoughPixelcoins(Jugador vendedor, double pixelcoins){
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
