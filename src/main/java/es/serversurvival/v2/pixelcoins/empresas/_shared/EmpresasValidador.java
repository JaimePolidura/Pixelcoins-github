package es.serversurvival.v2.pixelcoins.empresas._shared;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.javaddd.domain.exceptions.AlreadyExists;
import es.jaime.javaddd.domain.exceptions.IllegalQuantity;
import es.jaime.javaddd.domain.exceptions.IllegalState;
import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.serversurvival.v1._shared.exceptions.NotEnoughPixelcoins;
import es.serversurvival.v2.pixelcoins._shared.Validador;
import es.serversurvival.v2.pixelcoins.empresas._shared.accionistas.AccionistaEmpresa;
import es.serversurvival.v2.pixelcoins.empresas._shared.accionistas.AccionistasEmpresasService;
import es.serversurvival.v2.pixelcoins.empresas._shared.empleados.Empleado;
import es.serversurvival.v2.pixelcoins.empresas._shared.empleados.EmpleadosService;
import es.serversurvival.v2.pixelcoins.empresas._shared.empresas.EmpresasService;
import es.serversurvival.v2.pixelcoins.empresas._shared.votaciones.votaciones.EstadoVotacion;
import es.serversurvival.v2.pixelcoins.empresas._shared.votaciones.votaciones.Votacion;
import es.serversurvival.v2.pixelcoins.empresas._shared.votaciones.votaciones.VotacionesService;
import es.serversurvival.v2.pixelcoins.empresas._shared.votaciones._shared.votos.VotosService;
import es.serversurvival.v2.pixelcoins.mercado._shared.MercadoOfertasRepository;
import es.serversurvival.v2.pixelcoins.mercado._shared.Oferta;
import es.serversurvival.v2.pixelcoins.mercado._shared.OfertasService;
import es.serversurvival.v2.pixelcoins.mercado._shared.TipoOferta;
import es.serversurvival.v2.pixelcoins.transacciones.TransaccionesService;
import lombok.AllArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public final class EmpresasValidador {
    private final AccionistasEmpresasService accionistasEmpresasService;
    private final TransaccionesService transaccionesService;
    private final VotacionesService votacionesService;
    private final EmpleadosService empleadosService;
    private final EmpresasService empresasService;
    private final OfertasService ofertasService;
    private final VotosService votosService;
    private final Validador validador;

    public void tienePixelcoinsSuficientes(UUID empresaId, double pixelcoins) {
        if(transaccionesService.getBalancePixelcions(empresaId) < pixelcoins){
           throw new NotEnoughPixelcoins("La empresa no tiene las suficientes pixelcoins");
        }
    }

    public void nombreEmpresaCorrecta(String nombre) {
        validador.stringLongitudEntre(nombre, 3, 16, "nombre de empresa");
    }

    public void periodoPagoCorrecto(long periodoPago) {
        validador.numeroMayorQueCero(periodoPago, "El periodo de pago");
    }

    public void sueldoCorrecto(double sueldo) {
        validador.numeroMayorQueCero(sueldo, "El Sueldo");
    }

    public void descripccionCorrecta(String desc) {
        validador.stringLongitudEntre(desc, 0, 32, "descripccion");
    }

    public void validarIcono(String icono) {
        validador.stringNoVacio(icono, "icono");
    }

    public void empresaNoExiste(String nombreEmpresa) {
        if(this.empresasService.findByNombre(nombreEmpresa).isPresent()){
            throw new AlreadyExists("El nombre de la empresa ya existe");
        }
    }

    public void accionesNoEstanYaALaVenta(UUID accionistaId, int nuevasAccionesALaVenta) {
        AccionistaEmpresa accionistaEmpresa = accionistasEmpresasService.getById(accionistaId);
        Optional<Oferta> ofertaAccionesOptional = ofertasService.findByObjetoAndTipo(accionistaId.toString(), TipoOferta.ACCIONES_SERVER_JUGADOR);

        if(ofertaAccionesOptional.isEmpty()){
            return;
        }

        Oferta ofertaAcciones = ofertaAccionesOptional.get();
        if(ofertaAcciones.getCantidad() + nuevasAccionesALaVenta > accionistaEmpresa.getNAcciones()){
            throw new IllegalQuantity("No puedes poner mas acciones de las que tienes a la venta");
        }
    }

    public void empresaCotizada(UUID empresaId) {
        if(!empresasService.getById(empresaId).isEsCotizada()) {
            throw new IllegalState("La empresa no cotiza en bolsa");
        }
    }

    public void empresaNoCotizada(UUID empresaId) {
        if(empresasService.getById(empresaId).isEsCotizada()) {
            throw new IllegalState("La empresa cotiza en bolsa");
        }
    }

    public void accionistaDeEmpresa(UUID empresaId, UUID jugadorId) {
        if(this.accionistasEmpresasService.findByEmpresaIdAndJugadorId(empresaId, jugadorId).isEmpty()){
            throw new IllegalState("No eres accionista de la empresa");
        }
    }

    public void empresaNoCerrada(UUID empresaId) {
        if(empresasService.getById(empresaId).isEstaCerrado()){
            throw new IllegalState("La empresa esta cerrada");
        }
    }

    public void empleadoNoEsDirector(UUID empresaId, UUID empleadoJugadorId) {
        if(empresasService.getById(empresaId).getDirectorJugadorId().equals(empleadoJugadorId)){
            throw new IllegalState("El jugador es el director de la empresa");
        }
    }

    public void empleadoEmpresaActivo(UUID empresaId, UUID empleadoJugadorId) {
        Optional<Empleado> empleado = empleadosService.findEmpleoActivoByEmpresaIdAndEmpleadoJugadorId(empresaId, empleadoJugadorId);
        if(empleado.isEmpty()){
            throw new NotTheOwner("El jugador no esta empleado");
        }
    }

    public void empleadoEmpresa(UUID empresaId, UUID jugadorId) {
        Optional<Empleado> empleado = empleadosService.findEmpleoActivoByEmpresaIdAndEmpleadoJugadorId(empresaId, jugadorId);
        if(empleado.isEmpty()){
            throw new NotTheOwner("No estas empleado en la empresa");
        }
    }

    public void noEmpleadoEmpresa(UUID empresaId, UUID jugadorId) {
        Optional<Empleado> empleado = empleadosService.findEmpleoActivoByEmpresaIdAndEmpleadoJugadorId(empresaId, jugadorId);
        if(empleado.isPresent()){
            throw new NotTheOwner("El jugador ya esta empleado");
        }
    }

    public void precioPorAccion(double precioPorAccion) {
        if(precioPorAccion <= 0){
            throw new IllegalQuantity("El precio por accion tiene que ser mayor que 0");
        }
    }

    public void numerAccionesValido(double numeroAcciones) {
        if(numeroAcciones <= 0){
            throw new IllegalQuantity("El numero de acciones tiene que ser negativo ni cero");
        }
    }


    public void jugadorTieneAcciones(UUID empresaId, UUID jugadorId, int supuestoNumeroAccionesTiene) {
        Optional<AccionistaEmpresa> accionistaEmpresa = accionistasEmpresasService.findByEmpresaIdAndJugadorId(empresaId, jugadorId);
        if(accionistaEmpresa.isEmpty() || accionistaEmpresa.get().getNAcciones() < supuestoNumeroAccionesTiene){
            throw new IllegalQuantity("No tienes las suficientes acciones");
        }
    }

    public void votacionPerteneceAEmpresa(UUID empresaId, UUID votacionId) {
        if(!this.votacionesService.getById(votacionId).getEmpresaId().equals(empresaId)){
            throw new NotTheOwner("La votacion no esta asignada a la empresa");
        }
    }

    public void noHaVotado(UUID votacionId, UUID jugadorId) {
        boolean yaHaVotado = votosService.findByVotacionId(votacionId).stream()
                .anyMatch(voto -> voto.getJugadorId().equals(jugadorId));

        if(yaHaVotado){
            throw new AlreadyExists("Ya has votado");
        }
    }

    public void votacionAbierta(UUID votacionId) {
        if(this.votacionesService.getById(votacionId).getEstado() != EstadoVotacion.ABIERTA){
            throw new NotTheOwner("La votacion no esta abierta");
        }
    }

    public void noDirectorEmpresa(UUID empresaId, UUID supuestoDirector) {
        if(empresasService.getById(empresaId).getDirectorJugadorId().equals(supuestoDirector)){
            throw new NotTheOwner("Ya es el director de empresa");
        }
    }

    public void votacionNoRepetida(Votacion votacion) {
        boolean repetida = votacionesService.findByEmpresaId(votacion.getEmpresaId()).stream()
                .anyMatch(it -> it.getTipo() == votacion.getTipo() && it.getEstado() == EstadoVotacion.ABIERTA);
        if(repetida){
            throw new AlreadyExists("La votacion ya existe");
        }
    }

    public void directorEmpresa(UUID empresaId, UUID supuestoDirector) {
        if(!empresasService.getById(empresaId).getDirectorJugadorId().equals(supuestoDirector)){
            throw new NotTheOwner("No eres el director de la empresa");
        }
    }
}
