import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Statistics {
    private int totalTraffic;
    private LocalDateTime minTime;
    private LocalDateTime maxTime;
    private Set<String> existingPages;
    private Map<String, Integer> osFrequency;

    public Statistics() {
        this.totalTraffic = 0;
        this.minTime = null;
        this.maxTime = null;
        this.existingPages = new HashSet<>();
        this.osFrequency = new HashMap<>();
    }

    public void addEntry(LogEntry entry) {
        totalTraffic += entry.getBytesSent();

        LocalDateTime entryTime = entry.getDateTime();
        if (minTime == null || entryTime.isBefore(minTime)) {
            minTime = entryTime;
        }
        if (maxTime == null || entryTime.isAfter(maxTime)) {
            maxTime = entryTime;
        }

        if (entry.getHttpCode().equals("200")) existingPages.add(entry.getRequestPath());

        String osType = entry.getUserAgent().getOsType();
        osFrequency.put(osType, osFrequency.getOrDefault(osType, 0)+1);

    }

    public Set<String> getExistingPages(){
        return existingPages;
    }

    public Map<String, Integer> getOsFrequency() {
        return new HashMap<>(osFrequency);
    }

    public double getTrafficRate() {
        if (minTime == null || maxTime == null) {
            return 0.0;
        }

        long hoursBetween = ChronoUnit.HOURS.between(minTime, maxTime);
        if (hoursBetween == 0) {
            return 0.0;
        }

        return (double) totalTraffic / hoursBetween;
    }
}
