package es.serversurvival.pixelcoins.config._shared.infrastructure;

import es.jaime.connection.ConnectionManager;
import es.jaime.repository.EntityMapper;
import es.jaime.repository.Repository;
import es.serversurvival._shared.mysql.MySQLRepository;
import es.serversurvival.pixelcoins.config._shared.domain.ConfigurationEntry;
import es.serversurvival.pixelcoins.config._shared.domain.ConfigurationKey;
import es.serversurvival.pixelcoins.config._shared.domain.ConfigurationRepository;

import java.util.Optional;

@MySQLRepository
public final class MySQLConfigurationRepository extends Repository<ConfigurationEntry, ConfigurationKey, Object> implements ConfigurationRepository {
    public MySQLConfigurationRepository(ConnectionManager connectionManager) {
        super(connectionManager);
    }

    @Override
    public void save(ConfigurationEntry entry) {
        super.save(entry);
    }

    @Override
    public Optional<ConfigurationEntry> findByKey(ConfigurationKey key) {
        return super.findById(key);
    }

    @Override
    public EntityMapper<ConfigurationEntry, Object> entityMapper() {
        return EntityMapper.builder()
                .table("configuration")
                .idField("keyy")
                .classesToMap(ConfigurationEntry.class)
                .build();
    }
}
