package es.serversurvival.comandos.subComandos.empresas;

import es.serversurvival.objetos.mySQL.Transacciones;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class DepositarEmpresasSubComando extends EmpresasSubCommand {
    private final String scnombre = "depositar";
    private final String sintaxis = "/empresas depositar <empresa> <pixelcoins>";
    private final String ayuda = "Depositar pixelcoins en tu empresa: para poder pagar el salario de los trabajadores";

    public String getSCNombre() {
        return scnombre;
    }

    public String getSintaxis() {
        return sintaxis;
    }

    public String getAyuda() {
        return ayuda;
    }

    public void execute(Player p, String[] args) {
        if (args.length == 3) {
            try {
                double pixelcoins = Double.parseDouble(args[2]);
                String nombreEmpresa2 = args[1];
                if (pixelcoins > 0) {
                    Transacciones t = new Transacciones();
                    t.conectar();
                    t.depositarPixelcoins(p, pixelcoins, nombreEmpresa2);
                    t.desconectar();
                } else {
                    p.sendMessage(ChatColor.DARK_RED + "Introduce numeros que no sean igual a cero o que no sean negativos");
                }

            } catch (Exception e) {
                p.sendMessage(ChatColor.DARK_RED + "Introduce numeros no texto");
            }
        } else {
            p.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: " + this.sintaxis);
        }
    }
}
