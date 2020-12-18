package es.serversurvival.comandos.subComandos.empresas;

import es.serversurvival.util.Funciones;
import es.serversurvival.mySQL.tablasObjetos.Empresa;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class DepositarEmpresas extends EmpresasSubCommand {
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

    public void execute(Player jugadorPlayer, String[] args) {
        if(args.length != 3){
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: " + this.sintaxis);
            return;
        }
        if(!Funciones.esDouble(args[2])){
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "Introduce numeros no texto");
            return;
        }
        double pixelcoinsADepositar = Double.parseDouble(args[2]);
        if(pixelcoinsADepositar <= 0){
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "Debes poner las pixelcoins que tal manera que no sean negativas ni cero");
            return;
        }
        String nombreEmpresa = args[1];
        jugadoresMySQL.conectar();
        if(jugadoresMySQL.getJugador(jugadorPlayer.getName()).getPixelcoins() < pixelcoinsADepositar){
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "No puedes meter mas dinero en la empresa del que tienes");
            jugadoresMySQL.desconectar();
            return;
        }
        Empresa empresaADepositar = empresasMySQL.getEmpresa(nombreEmpresa);
        if(empresaADepositar == null){
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "La empresa no existe");
            jugadoresMySQL.desconectar();
            return;
        }
        if(!empresaADepositar.getOwner().equalsIgnoreCase(jugadorPlayer.getName())){
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "No puedes poner dinero en una empresa que no sea tuya");
            empresasMySQL.desconectar();
            return;
        }

        transaccionesMySQL.depositarPixelcoinsEmpresa(jugadorPlayer, pixelcoinsADepositar, nombreEmpresa);
        empresasMySQL.desconectar();
    }
}
