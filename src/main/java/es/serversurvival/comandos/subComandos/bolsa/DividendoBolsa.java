package es.serversurvival.comandos.subComandos.bolsa;

import es.serversurvival.mySQL.MySQL;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Objects;

public class DividendoBolsa extends BolsaSubCommand{
    private final String scnombre = "dividendo";
    private final String sintaxis = "/bolsa dividendo [ticker]";
    private final String ayuda = "Ver cuando te van a pagar los dividendos de las empresas que tienes en cartera. O con [ticker] ver una accion en especifico";

    @Override
    public String getSCNombre() {
        return scnombre;
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
        if(args.length > 2){
            player.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: " + this.sintaxis);
            return;
        }

        boolean verCarteraEntera = args.length == 1;

        if(verCarteraEntera){
            posicionesAbiertasMySQL.mostrarDividendosCarteraEntera(player);
        }else{
            posicionesAbiertasMySQL.mostrarDividendoEmpresa(player, args[1]);
        }
    }
}