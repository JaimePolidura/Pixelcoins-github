package es.serversurvival.minecraftserver.webaction.token;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.minecraftserver.webaction.WebActionType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
public final class WebActionTokenService {
    private static final String SECRET_KEY = "7B9R6rb6157rb761";

    public String generate(WebActionType actionType, UUID jugadorId) {
        return Jwts.builder()
                .setClaims(Map.of(
                        "actionType", actionType.toString(),
                        "jugadorId", jugadorId.toString()
                ))
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(5).toInstant()))
                .compact();
    }

    public boolean isValid(String token) {
        return this.getClaims(token).getExpiration().before(new Date());
    }

    public UUID getJugadorIdFromToken(String token) {
        return UUID.fromString(getClaims(token).get("jugadorid").toString());
    }

    public WebActionType getWebActionTypeFromToken(String token) {
        return WebActionType.valueOf(getClaims(token).get("actionType").toString());
    }

    private Claims getClaims (String token){
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}
