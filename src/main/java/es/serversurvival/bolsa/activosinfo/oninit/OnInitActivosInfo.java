package es.serversurvival.bolsa.activosinfo.oninit;

import es.jaime.EventListener;
import es.jaimetruman.annotations.Component;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.eventospixelcoins.PluginIniciado;
import es.serversurvival.bolsa.activosinfo._shared.application.ActivosInfoService;

@Component
public final class OnInitActivosInfo {
    private final ActivosInfoService activosInfoService;

    public OnInitActivosInfo() {
        this.activosInfoService = DependecyContainer.get(ActivosInfoService.class);
    }

    @EventListener
    public void on(PluginIniciado e){
        this.activosInfoService.initializeCache();
    }
}
