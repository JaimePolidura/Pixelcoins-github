package es.serversurvival.empresas.empresas.sacar;

import es.jaime.javaddd.domain.exceptions.IllegalQuantity;
import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.serversurvival.Pixelcoin;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;

public final class SacarPixelcoinsUseCase implements AllMySQLTablesInstances {
    private final EmpresasService empresasService;
    private final JugadoresService jugadoresService;

    public SacarPixelcoinsUseCase(){
        this.empresasService = DependecyContainer.get(EmpresasService.class);
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
    }

    public void sacar (String playerName, String empresaNombre, double pixelcoinsASacar) {
        this.ensureCorrectFormatPixelcoins(pixelcoinsASacar);
        Jugador jugador = this.jugadoresService.getByNombre(playerName);
        Empresa empresaASacar = this.empresasService.getEmpresaByNombre(empresaNombre);
        this.ensureOwnerOfEmpresa(empresaASacar, playerName);

        pixelcoinsASacar = pixelcoinsASacar > empresaASacar.getPixelcoins() ?
                empresaASacar.getPixelcoins() :
                pixelcoinsASacar;

        empresasService.save(empresaASacar.decrementPixelcoinsBy(pixelcoinsASacar));
        jugadoresService.save(jugador.incrementPixelcoinsBy(pixelcoinsASacar));

        Pixelcoin.publish(new PixelcoinsSacadasEvento(jugador, empresaASacar, pixelcoinsASacar));
    }


    private void ensureOwnerOfEmpresa(Empresa empresa, String playerName){
        if(!empresa.getOwner().equalsIgnoreCase(playerName))
            throw new NotTheOwner("No eres el owner de la empresa");
    }

    private void ensureCorrectFormatPixelcoins(double pixelcoins){
        if(pixelcoins <= 0)
            throw new IllegalQuantity("Las pixelcoins deben de ser un numero natural");
    }
}
