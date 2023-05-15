package es.serversurvival.v1.bolsa.activosinfo.oninit;

import es.dependencyinjector.dependencies.annotations.Component;
import es.jaime.EventListener;
import es.serversurvival.v1._shared.eventospixelcoins.PluginIniciado;
import es.serversurvival.v1.bolsa.activosinfo._shared.application.ActivosInfoService;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public final class OnInitActivosInfo {
    private final ActivosInfoService activosInfoService;

    @EventListener
    public void on(PluginIniciado e){
        this.activosInfoService.initializeCache();
    }
}
