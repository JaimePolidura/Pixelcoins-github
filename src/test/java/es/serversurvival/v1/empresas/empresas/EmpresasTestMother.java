package es.serversurvival.v1.empresas.empresas;

import es.serversurvival.v1.empresas.empresas._shared.domain.Empresa;

import java.util.UUID;

public final class EmpresasTestMother {
    public static Empresa createEmpresa(String nombre, String owner){
        return new Empresa(UUID.randomUUID(), nombre, owner, 0, 0, 0, null, "", false, 0);
    }

    public static Empresa createEmpresa(String nombre, String owner, double pixelcoins){
        return new Empresa(UUID.randomUUID(), nombre, owner, pixelcoins, 0, 0, null, "", false, 0);
    }
}
