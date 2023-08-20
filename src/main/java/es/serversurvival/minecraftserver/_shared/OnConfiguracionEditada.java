package es.serversurvival.minecraftserver._shared;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival.Pixelcoin;
import es.serversurvival.pixelcoins.config.editar.ConfigurationEditada;
import lombok.RequiredArgsConstructor;

@EventHandler
@RequiredArgsConstructor
public final class OnConfiguracionEditada {
    @EventListener
    public void on(ConfigurationEditada evento) {
        switch (evento.getKey()) {
            case PRINT_COMMANDS_EXCEPTIONS -> {
                Pixelcoin.CLASS_MAPPER.setPrintExceptions(Boolean.parseBoolean(evento.getNewValue()));
            }
        }
    }
}
