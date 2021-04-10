package es.serversurvival.mobs;

import es.jaimetruman.Mapper;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginMain extends JavaPlugin {
    @Override
    public void onEnable() {
        String onWrongCommand = ChatColor.DARK_RED + "Command not found";
        String onWrongSender = ChatColor.DARK_RED + "You have to be in the server";

        Mapper.build("es.jaime", this)
                .commandMapper(onWrongCommand, onWrongSender)
                .mobMapper()
                .eventListenerMapper()
                .startScanning();
    }
}
