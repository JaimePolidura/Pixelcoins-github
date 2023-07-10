package es.serversurvival.minecraftserver.bolsa.precio;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.dependencyinjector.dependencies.DependenciesRepository;
import es.serversurvival.pixelcoins.bolsa._shared.activos.aplicacion.ActivoBolsaUltimosPreciosService;
import es.serversurvival.pixelcoins.bolsa._shared.activos.aplicacion.ActivosBolsaService;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.ActivoBolsa;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.TipoActivoBolsa;
import es.serversurvival.pixelcoins.bolsa._shared.activos.infrastructure.AccionesBolsaInformationAPIService;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.Optional;

import static es.serversurvival._shared.utils.Funciones.*;
import static org.bukkit.ChatColor.*;

@Command(
        value = "bolsa precio",
        args = {"ticker"},
        isAsync = true
)
@RequiredArgsConstructor
public final class VerPrecioCommandRunner implements CommandRunnerArgs<VerPrecioComando> {
    private final ActivoBolsaUltimosPreciosService activoBolsaUltimosPreciosService;
    private final DependenciesRepository dependenciesRepository;
    private final ActivosBolsaService activosBolsaService;

    @Override
    public void execute(VerPrecioComando comando, Player player) {
        Optional<ActivoBolsa> activoBolsaOptional = activosBolsaService.findByNombreCortoAndTipoActivo(comando.getTicker(), TipoActivoBolsa.ACCION);

        if(activoBolsaOptional.isPresent()){
            enviarPrecioDeActivoGuardado(comando, player);
        }else{
            enviarPrecioDeActivoNoGuardado(comando, player);
        }
    }

    private void enviarPrecioDeActivoGuardado(VerPrecioComando comando, Player player) {
        ActivoBolsa activoBolsa = activosBolsaService.getByNombreCortoAndTipoActivo(comando.getTicker(), TipoActivoBolsa.ACCION);
        double ultimoPrecio = activoBolsaUltimosPreciosService.getUltimoPrecio(activoBolsa.getActivoBolsaId(), player.getUniqueId());

        player.sendMessage(GOLD + "El precio de " + activoBolsa.getNombreLargo() + " es " + formatPixelcoins(ultimoPrecio) + "/ Accion");
    }

    private void enviarPrecioDeActivoNoGuardado(VerPrecioComando comando, Player player) {
        AccionesBolsaInformationAPIService accionesBolsaInformationAPIService = dependenciesRepository.get(AccionesBolsaInformationAPIService.class);
        double ultimoPrecio = accionesBolsaInformationAPIService.getUltimoPrecio(comando.getTicker());
        String nombreLargo = accionesBolsaInformationAPIService.getNombreLargo(comando.getTicker());

        guardarActivo(comando, nombreLargo);

        player.sendMessage(GOLD + "El precio de " + nombreLargo + " es " + formatPixelcoins(ultimoPrecio) + "/ Accion");
    }

    private void guardarActivo(VerPrecioComando comando, String nombreLargo) {
        activosBolsaService.save(ActivoBolsa.builder()
                .nombreCorto(comando.getTicker())
                .nombreLargo(nombreLargo)
                .tipoActivoBolsa(TipoActivoBolsa.ACCION)
                .build());
    }
}
