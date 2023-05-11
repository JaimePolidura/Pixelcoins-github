package es.serversurvival.deudas.prestar;

public record PrestamoConfirmacionMenuState(String destinatarioJugadorNombre, String enviadorJugadorNombre,
                                            double pixelcoins, int dias, int interes) {
}
