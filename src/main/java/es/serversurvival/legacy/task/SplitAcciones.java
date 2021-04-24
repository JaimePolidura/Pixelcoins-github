package es.serversurvival.legacy.task;

import es.jaimetruman.task.BukkitTimeUnit;
import es.jaimetruman.task.Task;
import es.jaimetruman.task.TaskRunner;
import es.serversurvival.legacy.mySQL.PosicionesAbiertas;

@Task(period = BukkitTimeUnit.DAY, delay = BukkitTimeUnit.MINUTE)
public class SplitAcciones implements TaskRunner {
    private final PosicionesAbiertas posicionesAbiertasMySQL = PosicionesAbiertas.INSTANCE;

    @Override
    public void run() {
        posicionesAbiertasMySQL.actualizarSplits();
    }
}
