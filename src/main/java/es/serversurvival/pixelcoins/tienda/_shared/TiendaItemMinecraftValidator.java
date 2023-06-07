package es.serversurvival.pixelcoins.tienda._shared;

import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.javaddd.domain.exceptions.IllegalType;
import es.serversurvival.pixelcoins._shared.Validador;
import lombok.AllArgsConstructor;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public final class TiendaItemMinecraftValidator {
    private static final List<String> BANNED_ITEMS = Arrays.asList("POTION", "BANNER", "SPLASH_POTION", "LINGERING_POTION", "AIR");

    private final Validador validador;

    public void precio(double precio) {
        validador.numeroMayorQueCero(precio, "El precio");
    }

    public void itemNoBaneado(ItemStack itemStack) {
        boolean baneado = BANNED_ITEMS.stream()
                .anyMatch(itemBaneadoNombre -> itemBaneadoNombre.equalsIgnoreCase(itemStack.getType().toString()));

        if(baneado || itemStack == null){
            throw new IllegalType("Ese tipo de item no se puede subir a la tienda");
        }
    }
}
