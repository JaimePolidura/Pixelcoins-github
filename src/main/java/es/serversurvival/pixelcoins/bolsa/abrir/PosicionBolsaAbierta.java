package es.serversurvival.pixelcoins.bolsa.abrir;

import es.serversurvival._shared.eventospixelcoins.InvocaAUnReto;
import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.pixelcoins.bolsa._shared.activos.dominio.ActivoBolsa;
import es.serversurvival.pixelcoins.bolsa._shared.posiciones.domain.TipoBolsaApuesta;
import es.serversurvival.pixelcoins.retos._shared.retos.application.RetoMapping;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class PosicionBolsaAbierta extends PixelcoinsEvento implements InvocaAUnReto {
    @Getter private UUID jugadorId;
    @Getter private int cantidad;
    @Getter private boolean premarket;
    @Getter private ActivoBolsa activoBolsa;
    @Getter private double precioPorUnidad;
    @Getter private TipoBolsaApuesta tipoApuesta;
    @Getter private double costeTotal;

    public PosicionBolsaAbierta(UUID jugadorId, boolean premarket) {
        this.jugadorId = jugadorId;
        this.premarket = premarket;
    }

    public PosicionBolsaAbierta(UUID jugadorId, int cantidad, boolean premarket, ActivoBolsa activoBolsa, double precioPorUnidad, TipoBolsaApuesta tipoApuesta, double costeTotal) {
        this.jugadorId = jugadorId;
        this.cantidad = cantidad;
        this.premarket = premarket;
        this.activoBolsa = activoBolsa;
        this.precioPorUnidad = precioPorUnidad;
        this.tipoApuesta = tipoApuesta;
        this.costeTotal = costeTotal;
    }

    @Override
    public Map<UUID, List<RetoMapping>> retosByJugadorId() {
        if(premarket){
            return null;
        }

        if(tipoApuesta == TipoBolsaApuesta.LARGO){
            return Map.of(jugadorId, List.of(RetoMapping.BOLSA_ABRIR_LARGO));
        }else{
            return Map.of(jugadorId, List.of(RetoMapping.BOLSA_ABRIR_CORTO));
        }
    }
}
