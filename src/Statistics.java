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
    private Set<String> notExistingPages;
    private Map<String, Double> browserFrequency;

    public Statistics() {
        this.totalTraffic = 0;
        this.minTime = null;
        this.maxTime = null;
        this.existingPages = new HashSet<>();
        this.osFrequency = new HashMap<>();
        this.notExistingPages = new HashSet<>();
        this.browserFrequency = new HashMap<>();
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

        if (entry.getHttpCode().equals("404")) {
            notExistingPages.add(entry.getRequestPath());
        }

        String osType = entry.getUserAgent().getOsType();
        osFrequency.put(osType, osFrequency.getOrDefault(osType, 0.0)+1);

        String browserType = entry.getUserAgent().getBrowserType();
        browserFrequency.put(browserType, browserFrequency.getOrDefault(browserType, 0.0)+1);
    }

    public Set<String> getExistingPages() {
        return existingPages;
    }

    public Set<String> getNotExistingPages() {
        return notExistingPages;
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

    public Map<String, Double> getBrowserStatistics(){
        Map<String, Double> browserStats = new HashMap<>();
        double totalBrowserCount = browserFrequency.values().stream().mapToDouble(Double::doubleValue).sum();

        for(Map.Entry<String, Double> entry : browserFrequency.entrySet()){
            String browserType = entry.getKey();
            double count = entry.getValue();
            double percentage = (double) count/totalBrowserCount;
            browserStats.put(browserType, percentage);
        }
        return browserStats;
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
