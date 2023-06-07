package es.serversurvival.minecraftserver.bolsa.invertir;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival.pixelcoins.bolsa._shared.activos.aplicacion.ActivosBolsaService;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.ActivoBolsa;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.TipoActivoBolsa;
import es.serversurvival.pixelcoins.bolsa._shared.posiciones.TipoBolsaApuesta;
import es.serversurvival.pixelcoins.bolsa.abrir.AbrirPosicionBolsaUseCase;
import es.serversurvival.pixelcoins.bolsa.abrir.AbrirPosicoinBolsaParametros;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

import java.util.Optional;

@Command(
        value = "bolsa invertir",
        args = {"ticker", "cantidad", "[tipoApuesta]¡LARGO!"},
        explanation = "Invertir en una accion en largo"
)
@AllArgsConstructor
public final class InvertirBolsaCommandRunner implements CommandRunnerArgs<InvertirBolsaComando> {
    private final AbrirPosicionBolsaUseCase abrirPosicionBolsaUseCase;
    private final ActivosBolsaService activosBolsaService;

    @Override
    public void execute(InvertirBolsaComando comando, Player player) {
        TipoBolsaApuesta tipoActivoBolsa = getTipoApuesta(comando);
        ActivoBolsa activoBolsa = activosBolsaService.findByNombreCortoAndTipoActivo(comando.getTicker(), TipoActivoBolsa.ACCION)
                .orElseThrow(() -> new ResourceNotFound("Accion no encontrada"));

        abrirPosicionBolsaUseCase.abrir(AbrirPosicoinBolsaParametros.builder()
                .jugadorId(player.getUniqueId())
                .tipoApuesta(tipoActivoBolsa)
                .activoBolsaId(activoBolsa.getActivoBolsaId())
                .cantidad(comando.getCantidad())
                .build());

        //TODO Gestionar los mensajes con eventos
    }

    private TipoBolsaApuesta getTipoApuesta(InvertirBolsaComando comando) {
        return Optional.of(TipoBolsaApuesta.valueOf(comando.getTipoApuesta().toUpperCase()))
                .orElseThrow(() -> new ResourceNotFound("El tipo apuesta puede ser LARGO o CORTO"));
    }
}
