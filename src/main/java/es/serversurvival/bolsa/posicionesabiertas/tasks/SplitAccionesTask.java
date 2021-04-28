package es.serversurvival.bolsa.posicionesabiertas.tasks;

import es.jaimetruman.task.BukkitTimeUnit;
import es.jaimetruman.task.Task;
import es.jaimetruman.task.TaskRunner;
import es.serversurvival.bolsa.posicionesabiertas.mysql.PosicionesAbiertas;

@Task(value = BukkitTimeUnit.DAY, delay = BukkitTimeUnit.MINUTE)
public class SplitAccionesTask implements TaskRunner {
    private final PosicionesAbiertas posicionesAbiertasMySQL = PosicionesAbiertas.INSTANCE;

    @Override
    public void run() {
    }
}
