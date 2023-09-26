package es.serversurvival.minecraftserver.jugadores.tpa.tpaaccpet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
public final class TpaAcceptCommand {
    @Getter private UUID token;
}
