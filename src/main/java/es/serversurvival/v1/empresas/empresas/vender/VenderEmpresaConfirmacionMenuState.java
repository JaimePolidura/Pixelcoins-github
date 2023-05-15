package es.serversurvival.v1.empresas.empresas.vender;

public record VenderEmpresaConfirmacionMenuState(
        String enviador,
        String destinatario,
        String empresa,
        double precio
) {
    public static VenderEmpresaConfirmacionMenuState fromCommand(String enviador, VenderEmpresaComando comando) {
        return new VenderEmpresaConfirmacionMenuState(enviador, comando.getJugador(), comando.getEmpresa(), comando.getPrecio());
    }
}
