package es.serversurvival.minecraftserver.bolsa.invertir;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.dependencyinjector.dependencies.DependenciesRepository;
import es.jaime.javaddd.application.utils.ExceptionUtils;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseBus;
import es.serversurvival.pixelcoins.bolsa._shared.activos.aplicacion.ActivosBolsaService;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.ActivoBolsa;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.TipoActivoBolsaService;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.TipoActivoBolsa;
import es.serversurvival.pixelcoins.bolsa._shared.posiciones.domain.TipoBolsaApuesta;
import es.serversurvival.pixelcoins.bolsa.abrir.AbrirPosicoinBolsaParametros;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

import java.util.Optional;

@Command(
        value = "bolsa invertir",
        args = {"ticker", "cantidad", "[tipoApuesta]¡LARGO!"},
        explanation = "Invertir en una accion en largo o en corto",
        isAsync = true
)
@AllArgsConstructor
public final class InvertirBolsaCommandRunner implements CommandRunnerArgs<InvertirBolsaComando> {
    private final DependenciesRepository dependenciesRepository;
    private final ActivosBolsaService activosBolsaService;
    private final UseCaseBus useCaseBus;

    @Override
    public void execute(InvertirBolsaComando comando, Player player) {
        TipoBolsaApuesta tipoActivoBolsa = getTipoApuesta(comando);
        ActivoBolsa activoBolsa = getActivoBolsa(comando);

        useCaseBus.handle(AbrirPosicoinBolsaParametros.builder()
                .jugadorId(player.getUniqueId())
                .tipoApuesta(tipoActivoBolsa)
                .activoBolsaId(activoBolsa.getActivoBolsaId())
                .cantidad(comando.getCantidad())
                .build());
    }

    private ActivoBolsa getActivoBolsa(InvertirBolsaComando comando) {
        Optional<ActivoBolsa> tipoActivoBolsaOpt = activosBolsaService.findByNombreCortoAndTipoActivo(comando.getTicker(), TipoActivoBolsa.ACCION);
        if(tipoActivoBolsaOpt.isPresent()){
            return tipoActivoBolsaOpt.get();
        }

        Class<? extends TipoActivoBolsaService> tipoActivoBolsaServiceClass = TipoActivoBolsa.ACCION.getActivoInfoService();
        TipoActivoBolsaService tipoActivoBolsaService = dependenciesRepository.get(tipoActivoBolsaServiceClass);
        String nombreLargo = ExceptionUtils.rethrow(() -> tipoActivoBolsaService.getNombreLargo(comando.getTicker()),
                () -> new ResourceNotFound("Accion no encontrada")) ;

        ActivoBolsa activoBolsa = ActivoBolsa.builder()
                .tipoActivoBolsa(TipoActivoBolsa.ACCION)
                .nombreLargo(nombreLargo)
                .nombreCorto(comando.getTicker())
                .build();

        activosBolsaService.save(activoBolsa);

        return activoBolsa;
    }


    private TipoBolsaApuesta getTipoApuesta(InvertirBolsaComando comando) {
        return Optional.of(TipoBolsaApuesta.valueOf(comando.getTipoApuesta().toUpperCase()))
                .orElseThrow(() -> new ResourceNotFound("El tipo apuesta puede ser LARGO o CORTO"));
    }
}
