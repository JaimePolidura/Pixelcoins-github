package es.serversurvival.minecraftserver.webaction.token;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival._shared.ConfigurationVariables;
import es.serversurvival.minecraftserver.webaction.WebActionType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.vavr.control.Try;

import java.time.ZonedDateTime;
import java.util.*;

@Service
public final class WebActionTokenService {
    public String generate(WebActionType actionType, UUID jugadorId) {
        return Jwts.builder()
                .setClaims(new HashMap<>(Map.of(
                        "actionType", actionType.name(),
                        "jugadorId", jugadorId.toString()
                )))
                .setSubject(jugadorId.toString())
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, ConfigurationVariables.WEB_ACTIONS_SECRET_KEY)
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(5).toInstant()))
                .compact();
    }

    public boolean isNotExpired(String token) {
        Try<Boolean> result = Try.of(() -> getClaims(token).getExpiration().before(new Date(System.currentTimeMillis())));

        return result.isSuccess() && result.get();
    }

    public WebActionType getWebActionTypeFromToken(String token) {
        return WebActionType.valueOf(getClaims(token).get("actionType").toString());
    }

    public UUID getJugadorIdFromToken(String token) {
        return UUID.fromString(getClaims(token).get("jugadorId").toString());
    }

    private Claims getClaims (String token){
        return Jwts.parser()
                .setSigningKey(ConfigurationVariables.WEB_ACTIONS_SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}
