package es.serversurvival.pixelcoins.mensajes.borrar;

import es.bukkitclassmapper.task.BukkitTimeUnit;
import es.bukkitclassmapper.task.Task;
import es.bukkitclassmapper.task.TaskRunner;
import es.jaime.connection.transactions.DatabaseTransacionExecutor;
import es.serversurvival._shared.ConfigurationVariables;
import es.serversurvival._shared.mysql.TransactionExecutor;
import es.serversurvival.pixelcoins.config._shared.application.Configuration;
import es.serversurvival.pixelcoins.config._shared.domain.ConfigurationKey;
import es.serversurvival.pixelcoins.mensajes._shared.application.MensajesService;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Task(BukkitTimeUnit.DAY)
@AllArgsConstructor
public final class MensajesAntiguosBorrador implements TaskRunner {
    private final TransactionExecutor transactionExecutor;
    private final MensajesService mensajesService;
    private final Configuration configuration;

    @Override
    public void run() {
        LocalDateTime fechaHoy = LocalDateTime.now();

        long maximoTiempoLeidoMs = configuration.getLong(ConfigurationKey.MENSAJES_MAXIMO_TIEMPO_LEIDO_MS);
        LocalDateTime fechaMinimoEnviado = fechaHoy.minusNanos(maximoTiempoLeidoMs * 1_000_000);

        transactionExecutor.execute(DatabaseTransacionExecutor.ExceptionHandlingMethod.ONLY_RETHROW, () -> {
            mensajesService.deleteByFechaVistoLessThan(fechaMinimoEnviado);
        });
    }
}
