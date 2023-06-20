package es.serversurvival._shared;

import es.dependencyinjector.dependencies.annotations.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class TiempoService {
    public long millis() {
        return System.currentTimeMillis();
    }

    public long toMillis(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);

        return zonedDateTime.toInstant().toEpochMilli();
    }
}
