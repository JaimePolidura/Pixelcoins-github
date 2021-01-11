package es.serversurvival.validaciones;

import es.serversurvival.validaciones.misValidaciones.*;
import main.validators.NotNull;
import main.validators.booleans.False;
import main.validators.booleans.True;
import main.validators.chars.MatchCharacters;
import main.validators.classes.DoestThrowException;
import main.validators.classes.InstanceOf;
import main.validators.numbers.*;
import main.validators.numbers.Number;
import main.validators.strings.*;

public final class Validaciones {
    private Validaciones () {}

    public final static Number Number = new Number("Tiene que ser un numero");
    public final static PositiveNumber PositiveNumber = new PositiveNumber("Tiene que ser un numero positivo");
    public final static NaturalNumber NaturalNumber = new NaturalNumber("Tiene que ser un numero positivo y no decimal");
    public final static MaxValue MaxValue = new MaxValue("No puede ser tan grande");
    public final static MaxValue MinValue = new MaxValue("No puede ser tan pequeño");
    public final static Different Different = new Different("No esta permitido ese numero");
    public final static Same Same = new Same("Tiene que ser lo mismo");

    public final static MatchCharacters MatchCharacters = new MatchCharacters("Caracter no permitido");

    public final static InstanceOf InstanceOf = new InstanceOf("No se puede ejecutar el comando");
    public final static DoestThrowException doestThrowException = new DoestThrowException("Error");

    public final static MaxLength MaxLength = new MaxLength("No puede ser muy grande");
    public final static MinLength MinLength = new MinLength("No puede ser tan pequeña");
    public final static NotEqualsIgnoreCase NotEqualsIgnoreCase = new NotEqualsIgnoreCase("No puedes ser tu mismo");
    public final static EqualsIgnoreCase EqualsIgnoreCase = new EqualsIgnoreCase("Jugador no encontrado");
    public final static NotIncludeCharacters NotIncludeCharacters = new NotIncludeCharacters("Uso incorrecto");

    public final static NotNull NotNull = new NotNull("No puede estar vacio");

    public final static True True = new True("error");
    public final static False False = new False("error");

    public final static SuficinestesPixelcoins SuficientesPixelcoins = new SuficinestesPixelcoins("No tienes las suficientes pixelcoins");
    public final static JugadorRegistrado JugadorRegistrado = new JugadorRegistrado("Jugador no encontrado");
    public final static ExisteConversacion ExisteConversacion = new ExisteConversacion("No tienes ninguna conversacion abierta");
    public final static ItemNotBaneadoTienda ItemNotBaneadoTienda = new ItemNotBaneadoTienda("Item no permitido en la tienda");
    public final static NoHaSidoCompradoItem NoHaSidoCompradoItem = new NoHaSidoCompradoItem("No puedes revender un item que ya has comprado");
    public final static SuficientesEspaciosTienda SuficientesEspaciosTienda = new SuficientesEspaciosTienda("Tienes muchos objetos en la tineda");
    public final static JugadorOnline JugadorOnline = new JugadorOnline("Ese jugador no esta online");
    public final static InventarioNoLleno InventarioNoLleno = new InventarioNoLleno("No puedes tener el inventario lleno");
    public final static OwnerPosicionAbierta OwnerPosicionAbierta = new OwnerPosicionAbierta("Esa posicion no estuyta o no existe o no es del tipo correcto");
}
