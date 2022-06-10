package es.serversurvival.bolsa.activosinfo.oninit;

import es.jaime.EventListener;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.eventospixelcoins.PluginIniciado;
import es.serversurvival.bolsa.activosinfo._shared.application.ActivosInfoService;
import es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesAbiertasSerivce;

public final class OnInitActivosInfo {
    private final PosicionesAbiertasSerivce posicionesAbiertasSerivce;
    private final ActivosInfoService activosInfoService;

    public OnInitActivosInfo() {
        this.posicionesAbiertasSerivce = DependecyContainer.get(PosicionesAbiertasSerivce.class);
        this.activosInfoService = DependecyContainer.get(ActivosInfoService.class);
    }

    @EventListener
    public void on(PluginIniciado e){
    }
}
