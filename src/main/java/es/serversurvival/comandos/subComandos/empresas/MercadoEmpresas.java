package es.serversurvival.comandos.subComandos.empresas;

import es.serversurvival.menus.menus.EmpresasMercadoMenu;
import org.bukkit.entity.Player;

public class MercadoEmpresas extends EmpresasSubCommand{
    private final String SCNombre = "mercado";
    private final String sintaxis = "/empresas mercado";
    private final String ayuda = "Mercado de acciones de las empresas del servido de minecraft";

    @Override
    public String getSCNombre() {
        return SCNombre;
    }

    @Override
    public String getSintaxis() {
        return sintaxis;
    }

    @Override
    public String getAyuda() {
        return ayuda;
    }

    @Override
    public void execute(Player player, String[] args) {
        EmpresasMercadoMenu empresasMercadoMenu = new EmpresasMercadoMenu(player);
    }
}
