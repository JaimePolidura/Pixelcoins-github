package es.serversurvival.comandos.subComandos.empresas;

import es.serversurvival.mySQL.Empresas;
import es.serversurvival.mySQL.MySQL;
import es.serversurvival.mySQL.tablasObjetos.Empresa;
import es.serversurvival.util.Funciones;
import es.serversurvival.validaciones.Validaciones;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static es.serversurvival.validaciones.Validaciones.*;

public class CrearEmpresas extends EmpresasSubCommand {
    private final String scnombre = "crear";
    private final String sintaxis = "/empresas crear <nombre> <descripccion...>";
    private final String ayuda = "Crear una empresa con una descripccion";

    public String getSCNombre() {
        return scnombre;
    }

    public String getSintaxis() {
        return sintaxis;
    }

    public String getAyuda() {
        return ayuda;
    }

    public void execute(Player jugadorPlayer, String[] args) {
        MySQL.conectar();

        ValidationResult result = ValidationsService.startValidating(args.length >= 3, True.of(mensajeUsoIncorrecto()))
                .andMayThrowException(() -> args[1], mensajeUsoIncorrecto(), NombreEmpresaNoPillado, MaxLength.of(Empresas.CrearEmpresaNombreLonMax, "El nombre no puede ser tan grande"))
                .andMayThrowException(() -> Funciones.buildStringFromArray(args, 2), mensajeUsoIncorrecto(), MaxLength.of(Empresas.CrearEmpresaDescLonMax, "La descripcion no puede ser tan larga"))
                .and(empresasMySQL.getEmpresasOwner(jugadorPlayer.getName()).size() + 1 <= Empresas.nMaxEmpresas, True.of("No puedes tener tantas empresas"))
                .validateAll();

        if (result.isFailed()){
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + result.getMessage());
            MySQL.desconectar();
            return;
        }

        empresasMySQL.crearEmpresa(args[1], jugadorPlayer, Funciones.buildStringFromArray(args, 2));

        MySQL.desconectar();
    }
}
