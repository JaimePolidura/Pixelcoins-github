package es.serversurvival.minecraftserver._shared;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;

@EventHandler
public final class OnEvento {
    @EventListener
    public void on(PixelcoinsEvento p) {
        System.out.println(p.getClass().getName());
    }
}
