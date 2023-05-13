package es.serversurvival.jugadores._shared.application;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival._shared.utils.CollectionUtils;
import es.serversurvival.bolsa.activosinfo._shared.application.ActivosInfoService;
import es.serversurvival.bolsa.activosinfo._shared.domain.ActivoInfo;
import es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesUtils;
import es.serversurvival.bolsa.posicionesabiertas._shared.domain.PosicionAbierta;
import es.serversurvival.deudas._shared.application.DeudasService;
import es.serversurvival.deudas._shared.domain.Deuda;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;
import es.serversurvival.jugadores._shared.domain.Jugador;
import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public final class CalculadorPatrimonio {
    private final ActivosInfoService activosInfoService;
    private final JugadoresService jugadoresService;
    private final PosicionesUtils posicionesUtils;
    private final EmpresasService empresasService;
    private final DeudasService deudasService;

    public Map<String, Double> calcularTopJugadores (boolean creciente) {
        List<Jugador> allJugadordes = jugadoresService.findAll();
        Map<String, ActivoInfo> mapAllLlamadas = activosInfoService.findAllToMap();
        Map<String, List<Deuda>> mapDeudasAcredor = deudasService.getAllDeudasAcredorMap();
        Map<String, List<Deuda>> mapDeudasDeudor = deudasService.getAllDeudasDeudorMap();
        Map<String, List<Empresa>> mapEmpresasJugador = empresasService.getAllEmpresasJugadorMap();
        Map<String, List<PosicionAbierta>> mapPosicionesLargo = posicionesUtils.getAllPosicionesAbiertasMap(PosicionAbierta::esLargo);
        Map<String, List<PosicionAbierta>> mapPosicionesCorto = posicionesUtils.getAllPosicionesAbiertasMap(PosicionAbierta::esCorto);

        HashMap<String, Double> toReturn = new HashMap<>();

        allJugadordes.forEach((jugador) -> {
            double activosTotales = patrimonioJugador(mapAllLlamadas, mapDeudasAcredor, mapDeudasDeudor, mapEmpresasJugador,
                    mapPosicionesLargo, mapPosicionesCorto, jugador);

            toReturn.put(jugador.getNombre(), activosTotales);
        });

        if(creciente)
            return CollectionUtils.sortMapByValueCrec(toReturn);
        else
            return CollectionUtils.sortMapByValueDecre(toReturn);
    }


    public double calcular(String jugadorNombre) {
        Map<String, ActivoInfo> mapAllLlamadas = activosInfoService.findAllToMap();
        Map<String, List<Deuda>> mapDeudasAcredor = deudasService.getAllDeudasAcredorMap();
        Map<String, List<Deuda>> mapDeudasDeudor = deudasService.getAllDeudasDeudorMap();
        Map<String, List<Empresa>> mapEmpresasJugador = empresasService.getAllEmpresasJugadorMap();
        Map<String, List<PosicionAbierta>> mapPosicionesLargo = posicionesUtils.getAllPosicionesAbiertasMap(PosicionAbierta::esLargo);
        Map<String, List<PosicionAbierta>> mapPosicionesCorto = posicionesUtils.getAllPosicionesAbiertasMap(PosicionAbierta::esCorto);

        double activos = 0;

        activos += jugadoresService.getByNombre(jugadorNombre).getPixelcoins();

        // Deudas acredor
        if(mapDeudasAcredor.get(jugadorNombre) != null){
            activos += mapDeudasAcredor.get(jugadorNombre).stream()
                    .mapToDouble(Deuda::getPixelcoinsRestantes)
                    .sum();
        }

        //Deudas deudor
        if(mapDeudasDeudor.get(jugadorNombre) != null){
            activos -= mapDeudasDeudor.get(jugadorNombre).stream()
                    .mapToDouble(Deuda::getPixelcoinsRestantes)
                    .sum();
        }

        //Emrpesas
        if(mapEmpresasJugador.get(jugadorNombre) != null){
            activos += mapEmpresasJugador.get(jugadorNombre).stream()
                    .mapToDouble(Empresa::getPixelcoins)
                    .sum();
        }

        ///Posiciones abiertas largas
        if(mapPosicionesLargo.get(jugadorNombre) != null){
            activos += mapPosicionesLargo.get(jugadorNombre).stream()
                    .mapToDouble(pos -> (mapAllLlamadas.get(pos.getNombreActivo()).getPrecio() * pos.getCantidad()))
                    .sum();
        }

        //Posicioenes abiertas cortas
        if(mapPosicionesCorto.get(jugadorNombre) != null){
            activos += mapPosicionesCorto.get(jugadorNombre).stream()
                    .mapToDouble(pos -> (pos.getPrecioApertura() - mapAllLlamadas.get(pos.getNombreActivo()).getPrecio()) * pos.getCantidad())
                    .sum();
        }

        return activos;
    }

    private double patrimonioJugador(Map<String, ActivoInfo> mapAllLlamadas, Map<String, List<Deuda>> mapDeudasAcredor,
                                            Map<String, List<Deuda>> mapDeudasDeudor, Map<String, List<Empresa>> mapEmpresasJugador,
                                            Map<String, List<PosicionAbierta>> mapPosicionesLargo, Map<String, List<PosicionAbierta>> mapPosicionesCorto, Jugador jugador) {
        double activosTotales = 0;

        //Liquidez
        activosTotales = jugador.getPixelcoins();

        // Deudas acredor
        if(mapDeudasAcredor.get(jugador.getNombre()) != null){
            activosTotales += mapDeudasAcredor.get(jugador.getNombre()).stream()
                    .mapToDouble(Deuda::getPixelcoinsRestantes)
                    .sum();
        }

        //Deudas deudor
        if(mapDeudasDeudor.get(jugador.getNombre()) != null){
            activosTotales -= mapDeudasDeudor.get(jugador.getNombre()).stream()
                    .mapToDouble(Deuda::getPixelcoinsRestantes)
                    .sum();
        }

        //Emrpesas
        if(mapEmpresasJugador.get(jugador.getNombre()) != null){
            activosTotales += mapEmpresasJugador.get(jugador.getNombre()).stream()
                    .mapToDouble(Empresa::getPixelcoins)
                    .sum();
        }

        ///Posiciones abiertas largas
        if(mapPosicionesLargo.get(jugador.getNombre()) != null){
            activosTotales += mapPosicionesLargo.get(jugador.getNombre()).stream()
                    .mapToDouble(pos -> (mapAllLlamadas.get(pos.getNombreActivo()).getPrecio() * pos.getCantidad()))
                    .sum();
        }

        //Posicioenes abiertas cortas
        if(mapPosicionesCorto.get(jugador.getNombre()) != null){
            activosTotales += mapPosicionesCorto.get(jugador.getNombre()).stream()
                    .mapToDouble(pos -> (pos.getPrecioApertura() - mapAllLlamadas.get(pos.getNombreActivo()).getPrecio()) * pos.getCantidad())
                    .sum();
        }
        return activosTotales;
    }
}
