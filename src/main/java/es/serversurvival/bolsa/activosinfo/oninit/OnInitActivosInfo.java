package es.serversurvival.bolsa.activosinfo.oninit;

import es.dependencyinjector.annotations.Component;
import es.jaime.EventListener;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.eventospixelcoins.PluginIniciado;
import es.serversurvival.bolsa.activosinfo._shared.application.ActivosInfoService;
import lombok.AllArgsConstructor;
import org.checkerframework.common.util.report.qual.ReportCall;

@Component
@AllArgsConstructor
public final class OnInitActivosInfo {
    private final ActivosInfoService activosInfoService;

    @EventListener
    public void on(PluginIniciado e){
        this.activosInfoService.initializeCache();
    }
}
