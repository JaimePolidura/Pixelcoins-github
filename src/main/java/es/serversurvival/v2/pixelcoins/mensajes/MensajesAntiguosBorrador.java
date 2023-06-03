package es.serversurvival.v2.pixelcoins.mensajes;

import es.bukkitclassmapper.task.BukkitTimeUnit;
import es.bukkitclassmapper.task.Task;
import es.bukkitclassmapper.task.TaskRunner;
import es.serversurvival.v1.mensajes._shared.domain.Mensaje;
import es.serversurvival.v2.pixelcoins.mensajes._shared.MensajesService;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Task(BukkitTimeUnit.DAY)
@AllArgsConstructor
public final class MensajesAntiguosBorrador implements TaskRunner {
    private final MensajesService mensajesService;

    @Override
    public void run() {
        LocalDateTime fechaHoy = LocalDateTime.now();
        LocalDateTime fechaMinimoEnviado = fechaHoy.minusNanos(Mensaje.TIEMPO_MAXIMO_MENSAJE_MS * 1_000_000);

        mensajesService.deleteByFechaVistoLessThan(fechaMinimoEnviado);
    }
}
