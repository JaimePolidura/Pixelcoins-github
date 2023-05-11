package es.serversurvival.empresas.empresas.vender;

public record VenderEmpresaConfirmacionMenuState(
        String enviador,
        String destinatario,
        String empresa,
        double precio
) {  }
