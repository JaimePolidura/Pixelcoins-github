package es.serversurvival.web.verificacioncuentas.enviarnumero;

import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.web.verificacioncuentas._shared.application.VerificacionCuentaService;
import es.serversurvival.web.webconnection.socketmessages.SocketMessagge;
import es.serversurvival.web.webconnection.socketmessages.SocketMessaggeExecutor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Form of the messagge: sendnumber-name=jaimetruman&number=10
 */
public class SendNumberMessagge extends SocketMessaggeExecutor {
    private final VerificacionCuentaService verificacionCuentaService;
    private final String NAME = "sendnumber";

    public SendNumberMessagge() {
        this.verificacionCuentaService = DependecyContainer.get(VerificacionCuentaService.class);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public SocketMessagge execute(SocketMessagge messagge) {
        Player player = Bukkit.getPlayer(messagge.get("name"));

        if(player != null){
            int number = (int) (Math.random() * 9999);

            this.verificacionCuentaService.save(player.getName(), number);

            player.sendMessage(ChatColor.GOLD + "El numero de verificacion: " + number);
        }

        return NO_RESPONSE;
    }
}
