package es.serversurvival.comandos;

import org.bukkit.entity.Player;

import java.text.DecimalFormat;

public abstract class Comando {
    public DecimalFormat formatea = new DecimalFormat("###,###.##");

    public abstract String getCNombre();

    public abstract String getSintaxis();

    public abstract String getAyuda();

    public abstract void execute(Player player, String[] args);
}