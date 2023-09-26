package es.serversurvival.minecraftserver.jugadores.tpa;

import es.bukkitclassmapper.task.BukkitTimeUnit;
import es.bukkitclassmapper.task.Task;
import es.bukkitclassmapper.task.TaskRunner;
import es.serversurvival.pixelcoins.config._shared.application.Configuration;
import es.serversurvival.pixelcoins.config._shared.domain.ConfigurationKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Task(BukkitTimeUnit.MINUTE * 5)
public final class TpaTokensService implements TaskRunner {
    private final Configuration configuration;
    private final Map<UUID, Token> tokensByTokenId;
    private final Lock lock;

    public TpaTokensService(Configuration configuration) {
        this.tokensByTokenId = new ConcurrentHashMap<>();
        this.lock = new ReentrantLock(true);
        this.configuration = configuration;
    }

    public Optional<Token> retrieve(UUID tokenId) {
        lock.lock();

        if (!tokensByTokenId.containsKey(tokenId)) {
            lock.unlock();
            return Optional.empty();
        }

        Token token = tokensByTokenId.get(tokenId);
        tokensByTokenId.remove(tokenId);

        lock.unlock();

        if(token.isTimedout(configuration.getLong(ConfigurationKey.TPA_TIMEOUT_MS))) {
            return Optional.empty();
        } else {
            return Optional.of(token);
        }
    }

    public UUID crear(Player playerToBeTeleported, Player destino) {
        Token token = new Token(UUID.randomUUID(), System.currentTimeMillis(), playerToBeTeleported.getName(), destino.getName());

        lock.lock();
        tokensByTokenId.put(token.getTokenId(), token);
        lock.unlock();

        return token.getTokenId();
    }

    @Override
    public void run() {
        lock.lock();

        for (Map.Entry<UUID, Token> entry : this.tokensByTokenId.entrySet()) {
            var token = entry.getValue();

            if(token.isTimedout(configuration.getLong(ConfigurationKey.TPA_TIMEOUT_MS))) {
                this.tokensByTokenId.remove(token.tokenId);
            }
        }

        lock.unlock();
    }

    @AllArgsConstructor
    public static class Token {
        @Getter private final UUID tokenId;
        @Getter private final long fechaCreacionMs;
        @Getter private final String playerToBeTeleported;
        @Getter private final String destino;

        public boolean isTimedout(long timeout) {
            return System.currentTimeMillis() - fechaCreacionMs >= timeout;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Token token1 = (Token) o;
            return Objects.equals(tokenId, token1.tokenId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(tokenId);
        }
    }
}
