package es.serversurvival.deudas.prestar;

public record PrestamoConfirmacionMenuState(
        String destinatarioJugadorNombre,
        String enviadorJugadorNombre,
        double pixelcoins,
        int dias,
        int interes) {

    public static PrestamoConfirmacionMenuState fromCommnad(String enviador, PrestarComando prestarComando) {
        return new PrestamoConfirmacionMenuState(
                prestarComando.getJugador().getName(),
                enviador,
                prestarComando.getPixelcoins(),
                prestarComando.getDias(),
                prestarComando.getInteres()
        );
    }
}
