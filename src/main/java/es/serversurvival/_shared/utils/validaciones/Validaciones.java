package es.serversurvival._shared.utils.validaciones;

import es.serversurvival._shared.utils.validaciones.misValidaciones.*;
import main.validators.NotNull;
import main.validators.booleans.False;
import main.validators.booleans.True;
import main.validators.numbers.*;
import main.validators.strings.*;

public final class Validaciones {
    private Validaciones () {}

    public final static PositiveNumber PositiveNumber = new PositiveNumber("Tiene que ser un numero positivo");
    public final static NaturalNumber NaturalNumber = new NaturalNumber("Tiene que ser un numero positivo y no decimal");
    public final static Different Different = new Different("No esta permitido ese numero");
    public final static Same Same = new Same("Tiene que ser lo mismo");



    public final static MaxLength MaxLength = new MaxLength("No puede ser muy grande");
    public final static NotEqualsIgnoreCase NotEqualsIgnoreCase = new NotEqualsIgnoreCase("No puedes ser tu mismo");
    public final static NotIncludeCharacters NotIncludeCharacters = new NotIncludeCharacters("Uso incorrecto");

    public final static NotNull NotNull = new NotNull("No puede estar vacio");

    public final static main.validators.booleans.True True = new True("error");
    public final static main.validators.booleans.False False = new False("error");

    public final static SuficinestesPixelcoins SuficientesPixelcoins = new SuficinestesPixelcoins("No tienes las suficientes pixelcoins");
    public final static es.serversurvival._shared.utils.validaciones.misValidaciones.JugadorRegistrado JugadorRegistrado = new JugadorRegistrado("Jugador no encontrado");
    public final static es.serversurvival._shared.utils.validaciones.misValidaciones.NoHaSidoCompradoItem NoHaSidoCompradoItem = new NoHaSidoCompradoItem("No puedes revender un item que ya has comprado");
    public final static JugadorOnline JugadorOnline = new JugadorOnline("Ese jugador no esta online");
    public final static es.serversurvival._shared.utils.validaciones.misValidaciones.InventarioNoLleno InventarioNoLleno = new InventarioNoLleno("No puedes tener el inventario lleno");
    public final static OwnerDeEmpresa OwnerDeEmpresa = new OwnerDeEmpresa("No eres el owner de la empresa");
    public final static NoLeHanEnviadoSolicitud NoLeHanEnviadoSolicitud = new NoLeHanEnviadoSolicitud ("A ese jugador ya le has enviado una solicitud / ya le han enviado una solicitud");
}
