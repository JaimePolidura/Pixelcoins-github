package es.serversurvival.v1.empresas.empresas.borrar;

public record BorrarEmpresaConfirmacionMenuState(String nombreEmpresa) {
    public static BorrarEmpresaConfirmacionMenuState fromCommand(BorrarEmpresaComando borrarEmpresaComando) {
        return new BorrarEmpresaConfirmacionMenuState(borrarEmpresaComando.getNombre());
    }
}
