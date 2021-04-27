package es.serversurvival.nfs.bolsa.venderlargo;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.nfs.shared.comandos.PixelcoinCommand;
import es.serversurvival.nfs.bolsa.llamadasapi.mysql.TipoActivo;
import es.serversurvival.nfs.bolsa.posicionescerradas.mysql.TipoPosicion;
import es.serversurvival.nfs.bolsa.ordenespremarket.abrirorden.AbrirOrdenUseCase;
import es.serversurvival.nfs.bolsa.posicionesabiertas.venderlargo.CerrarPosicionUseCase;
import es.serversurvival.nfs.bolsa.posicionesabiertas.mysql.PosicionAbierta;
import es.serversurvival.nfs.utils.Funciones;
import es.serversurvival.nfs.utils.validaciones.Validaciones;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.command.CommandSender;

import static org.bukkit.ChatColor.*;

@Command("bolsa vender")
public class VenderBolsaComando extends PixelcoinCommand implements CommandRunner {
    private final String mensajeIncorrecto = DARK_RED + "Uso incorrecto: /bolsa vender <id> [numero a vender]";
    private final CerrarPosicionUseCase venderUseCase = CerrarPosicionUseCase.INSTANCE;
    private final AbrirOrdenUseCase abrirOrdenUseCase = AbrirOrdenUseCase.INSTANCE;

    @Override
    public void execute(CommandSender player, String[] args) {
        ValidationResult result =ValidationsService.startValidating(args.length != 3 && args.length != 2, Validaciones.False.of(mensajeIncorrecto))
                .andMayThrowException(() -> args[1], mensajeIncorrecto, Validaciones.NaturalNumber, Validaciones.OwnerPosicionAbierta.de(player.getName(), TipoPosicion.LARGO))
                .andIfExists(() -> args[2], Validaciones.NaturalNumber)
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(DARK_RED + result.getMessage());
            return;
        }

        PosicionAbierta posicionAVender = posicionesAbiertasMySQL.getPosicionAbierta(Integer.parseInt(args[1]));

        int cantidad = args.length == 2 ?
                posicionAVender.getCantidad() :
                Integer.parseInt(args[2]);

        if (posicionAVender.getCantidad() < cantidad)
            cantidad = posicionAVender.getCantidad();

        if(posicionAVender.getTipo_activo() == TipoActivo.ACCIONES_SERVER){
            //TODO:
            return;
        }

        if(Funciones.mercadoEstaAbierto()){
            venderUseCase.venderPosicion(posicionAVender, cantidad, player.getName());
        }else{
            abrirOrdenUseCase.abrirOrdenCompraLargo(player.getName(), args[1], cantidad);
        }

    }
}
