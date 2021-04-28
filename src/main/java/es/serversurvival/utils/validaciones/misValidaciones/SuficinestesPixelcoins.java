package es.serversurvival.utils.validaciones.misValidaciones;

import es.serversurvival.jugadores.mySQL.Jugadores;
import main.ValidationResult;
import main.validators.Validator;

import java.util.function.Supplier;

public class SuficinestesPixelcoins implements Validator {
    private boolean preFailed = false;
    private String messageOnFailed;
    private double pixelcoins;
    private String player;

    public SuficinestesPixelcoins(String messageOnFailed) {
        this.messageOnFailed = messageOnFailed;
    }

    public SuficinestesPixelcoins(String messageOnFailed, double pixelcoins, String player) {
        this.messageOnFailed = messageOnFailed;
        this.pixelcoins = pixelcoins;
        this.player = player;
    }

    public SuficinestesPixelcoins(String messageOnFailed, double pixelcoins, String player, boolean preFailed) {
        this.preFailed = preFailed;
        this.messageOnFailed = messageOnFailed;
        this.pixelcoins = pixelcoins;
        this.player = player;
    }

    @Override
    public String getMessageOnFailed() {
        return messageOnFailed;
    }

    @Override
    public ValidationResult check(Object o) {
        if(preFailed){
            return ValidationResult.failed(messageOnFailed);
        }

        String string = (String) o;
        double pixelcoins = Double.parseDouble(string);

        try{
            return Jugadores.INSTANCE.getJugador(player).getPixelcoins() >= pixelcoins ?
                    ValidationResult.success() :
                    ValidationResult.failed(messageOnFailed);
        }catch (Exception e) {
            return ValidationResult.failed(messageOnFailed);
        }
    }

    public SuficinestesPixelcoins of (String playername) {
        return new SuficinestesPixelcoins(messageOnFailed, pixelcoins, playername);
    }

    public SuficinestesPixelcoins of (String playername, String messageOnFailed) {
        return new SuficinestesPixelcoins(messageOnFailed, pixelcoins, playername);
    }

    public SuficinestesPixelcoins of (Supplier<? extends String> playerSupplier, String messageOnFailed) {
        try{
            return new SuficinestesPixelcoins(messageOnFailed, pixelcoins, playerSupplier.get());
        }catch (Exception e){
            return new SuficinestesPixelcoins(messageOnFailed, -1, player, true);
        }
    }

    public SuficinestesPixelcoins of (String playername, Supplier<? extends String> pixelcoinsSupplier) {
        try{
            double pixelcoinsDouble = Double.parseDouble(pixelcoinsSupplier.get());

            return new SuficinestesPixelcoins(messageOnFailed, pixelcoinsDouble, playername);
        }catch (Exception e) {
            return new SuficinestesPixelcoins(messageOnFailed, -1, player, true);
        }
    }
}
