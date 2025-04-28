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
    private Map<String, Double> osFrequency;

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

        if (entry.getHttpCode().equals("200")) {
            existingPages.add(entry.getRequestPath());
        }

        String osType = entry.getUserAgent().getOsType();
        osFrequency.put(osType, osFrequency.getOrDefault(osType, 0.0)+1);
    }

    public Set<String> getExistingPages() {
        return existingPages;
    }

    public Map<String, Double> getOsStatistics() {
        Map<String, Double> osStats = new HashMap<>();
        double totalOsCount = osFrequency.values().stream().mapToDouble(Double::doubleValue).sum();

        for (Map.Entry<String, Double> entry : osFrequency.entrySet()) {
            String osType = entry.getKey();
            double count = entry.getValue();
            double percentage = (double) count / totalOsCount;
            osStats.put(osType, percentage);
        }

        return osStats;
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
