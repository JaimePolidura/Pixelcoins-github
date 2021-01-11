package es.serversurvival.main.Pixelcoins.comandos;

import es.serversurvival.mySQL.MySQL;
import es.serversurvival.util.Funciones;
import main.ValidationResult;
import main.ValidationsService;
import main.ValidationsService;
import org.bukkit.ChatColor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static es.serversurvival.util.Funciones.*;
import static es.serversurvival.validaciones.Validaciones.*;
import static es.serversurvival.validaciones.Validaciones.ExisteConversacion;

public class ReTest {
    private final String sender = "JaimeTruman";

    @Test
    public void legit () {
        String[] args = new String[]{"hola tio que tal"};
        Assert.assertTrue(check(args, sender).isSuccessful());
    }

    @Test
    public void length () {
        String[] args = new String[]{};
        Assert.assertTrue(check(args, sender).isFailed());
    }

    @Test
    public void maxSize () {
        String[] args = new String[]{"hola que tal estas tio me cago en tu puta madre subnoraml"};
        Assert.assertTrue(check(args, sender).isFailed());
    }

    @Test
    public void includeCharacters () {
        String[] args = new String[]{"hola que tal&"};
        Assert.assertTrue(check(args, sender).isFailed());
    }

    public ValidationResult check (String[] args, String sender) {
        return ValidationsService.startValidating(args.length, Different.of(0, "No puede estar vacio"))
                //.and(sender, ExisteConversacion) Already tested in the minecraft server
                .and(buildStringFromArray(args), MaxLength.of(50), NotIncludeCharacters.of('&', '-'))
                .validateAll();
    }
}
