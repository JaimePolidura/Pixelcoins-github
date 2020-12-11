package es.serversurvival.task;

import es.serversurvival.eventosBD.EventoListener;
import org.bukkit.scheduler.BukkitRunnable;

public class EventosBDListenerTask extends BukkitRunnable {
    private EventoListener eventoListener = new EventoListener();

    //Ejecutar cada 5s
    @Override
    public void run() {
        this.eventoListener.searchForEventsAndExecute();
    }
}
