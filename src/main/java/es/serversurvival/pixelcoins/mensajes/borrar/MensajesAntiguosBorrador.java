package es.serversurvival.pixelcoins.mensajes.borrar;

import es.bukkitclassmapper.task.BukkitTimeUnit;
import es.bukkitclassmapper.task.Task;
import es.bukkitclassmapper.task.TaskRunner;
import es.serversurvival._shared.ConfigurationVariables;
import es.serversurvival._shared.mysql.TransactionExecutor;
import es.serversurvival.pixelcoins.mensajes._shared.application.MensajesService;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Task(BukkitTimeUnit.DAY)
@AllArgsConstructor
public final class MensajesAntiguosBorrador implements TaskRunner {
    private final TransactionExecutor transactionExecutor;
    private final MensajesService mensajesService;

    @Override
    public void run() {
        LocalDateTime fechaHoy = LocalDateTime.now();
        LocalDateTime fechaMinimoEnviado = fechaHoy.minusNanos(ConfigurationVariables.MENSAJES_MAXIMO_TIEMPO_LEIDO_MS * 1_000_000);

        transactionExecutor.execute(() -> {
            mensajesService.deleteByFechaVistoLessThan(fechaMinimoEnviado);
        });
    }
}
