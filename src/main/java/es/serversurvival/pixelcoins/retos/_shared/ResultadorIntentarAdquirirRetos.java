package es.serversurvival.pixelcoins.retos._shared;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@AllArgsConstructor
public final class ResultadorIntentarAdquirirRetos {
    private final boolean puedeAdquirir;
    @Getter private final List<Integer> retosIdQuePuedeAdquirir;

    public boolean puedeAdquirirAlgunReto() {
        return this.puedeAdquirir;
    }
}
