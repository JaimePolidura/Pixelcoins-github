package es.serversurvival.comandos.subComandos.empleado;

import es.serversurvival.menus.menus.EmpleosMenu;
import org.bukkit.entity.Player;

public class MisTrabajosEmpleos extends EmpleosSubCommand {
    private final String SCNombre = "misempleos";
    private final String sintaxis = "/empleos misempleos";
    private final String ayuda = "ver todos los trabajos a los que estas contratando";

    public String getSCNombre() {
        return SCNombre;
    }

    public String getSintaxis() {
        return sintaxis;
    }

    public String getAyuda() {
        return ayuda;
    }

    public void execute(Player player, String[] args) {
        EmpleosMenu menu = new EmpleosMenu(player);
        menu.openMenu();
    }
}