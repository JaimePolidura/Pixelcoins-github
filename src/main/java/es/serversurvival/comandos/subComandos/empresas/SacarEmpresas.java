package es.serversurvival.comandos.subComandos.empresas;

import es.serversurvival.util.Funciones;
import es.serversurvival.mySQL.Transacciones;
import es.serversurvival.mySQL.tablasObjetos.Empresa;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SacarEmpresas extends EmpresasSubCommand {
    private final String SCNombre = "sacar";
    private final String sintaxis = "/empresas sacar <empresa> <pixelcoins>";
    private final String ayuda = "Sacar determinado numero de pixelcoins de tu empresa";

    public String getSCNombre() {
        return SCNombre;
    }

    public String getSintaxis() {
        return sintaxis;
    }

    public String getAyuda() {
        return ayuda;
    }

    public void execute(Player jugadorPlayer, String[] args) {
        if(args.length != 3){
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: " + this.sintaxis);
            return;
        }
        if(!Funciones.esDouble(args[2])){
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "Introduce numeros no texto");
            return;
        }
        String nombreEmpresa = args[1];
        double pixelcoinsASacar = Double.parseDouble(args[2]);
        if(pixelcoinsASacar <= 0){
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "Introduce numeros que no sean igual a cero o que no sean negativos");
            return;
        }

        empresasMySQL.conectar();
        Empresa empresaASacar = empresasMySQL.getEmpresa(nombreEmpresa);
        if (empresaASacar == null) {
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "Esa empresa no existe");
            empresasMySQL.desconectar();
            return;
        }
        if (!empresaASacar.getOwner().equalsIgnoreCase(jugadorPlayer.getName())) {
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "No eres due?o de esa empresa");
            empresasMySQL.desconectar();
            return;
        }
        double pixelcoinsEmpresa = empresaASacar.getPixelcoins();
        if (pixelcoinsEmpresa < pixelcoinsASacar) {
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "No puedes sacar mas dinero" + "+ del que la empresa tiene");
            empresasMySQL.desconectar();
            return;
        }

        transaccionesMySQL.sacarPixelcoinsEmpresa(jugadorPlayer, pixelcoinsASacar, nombreEmpresa);
        empresasMySQL.desconectar();
    }
}