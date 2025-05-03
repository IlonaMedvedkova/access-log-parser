import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Statistics {
    private int totalTraffic;
    private LocalDateTime minTime;
    private LocalDateTime maxTime;
    private Set<String> existingPages;
    private Map<String, Double> osFrequency;
    private Set<String> notExistingPages;
    private Map<String, Double> browserFrequency;
    private int totalVisits;
    private Set<String> uniqueIPs;
    private int errorRequests;
    private Map<Integer, Integer> visitsPerSecond;
    private Set<String> refererDomains;
    private List<LogEntry> logEntries;

    public Statistics() {
        this.totalTraffic = 0;
        this.minTime = null;
        this.maxTime = null;
        this.existingPages = new HashSet<>();
        this.osFrequency = new HashMap<>();
        this.notExistingPages = new HashSet<>();
        this.browserFrequency = new HashMap<>();
        this.totalVisits = 0;
        this.errorRequests = 0;
        this.uniqueIPs = new HashSet<>();
        this.visitsPerSecond = new HashMap<>();
        this.refererDomains = new HashSet<>();
        this.logEntries = new ArrayList<>();
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

        if(!UserAgent.isBot(entry.getUserAgent().getUserAgentString())){
            totalVisits++;
            uniqueIPs.add(entry.getIpAddress());

            int second = entryTime.getSecond();
            visitsPerSecond.put(second, visitsPerSecond.getOrDefault(second, 0)+1);
        }

        if (entry.getHttpCode().startsWith("4")||entry.getHttpCode().startsWith("5")){
            errorRequests++;
        }

        String referer = entry.getReferer();
        if (!referer.isEmpty()){
            String domain = extractDomain(referer);
            refererDomains.add(domain);
        }

        logEntries.add(entry);
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

    public double getAverageVisitsPerHour(){
        if (minTime == null || maxTime == null) {
            return 0.0;
        }

        long hoursBetween = ChronoUnit.HOURS.between(minTime, maxTime);
        if (hoursBetween == 0) {
            return 0.0;
        }

        return (double) totalVisits/hoursBetween;
    }

    public double getAverageErrorsPerHour(){
        if (minTime == null || maxTime == null) {
            return 0.0;
        }

        long hoursBetween = ChronoUnit.HOURS.between(minTime, maxTime);
        if (hoursBetween == 0) {
            return 0.0;
        }
        return (double) errorRequests/hoursBetween;
    }

    public double getAverageVisitsPerUser(){
        if(uniqueIPs.isEmpty()) return 0.0;
        return (double) totalVisits/uniqueIPs.size();
    }

    public int getPeakVisitsPerSec(){
        return visitsPerSecond.values().stream().mapToInt(Integer::intValue).max().orElse(0);
    }

    public Set<String> getRefererDomains(){
        return refererDomains;
    }

    public int getMaxVisitsPerUser() {
        Map<String, Integer> visitsPerIp = new HashMap<>();
        for (String ip : uniqueIPs) {
            visitsPerIp.put(ip, 0);
        }

        for (LogEntry entry : logEntries) {
            if (!UserAgent.isBot(entry.getUserAgent().getUserAgentString())) {
                String ip = entry.getIpAddress();
                visitsPerIp.put(ip, visitsPerIp.getOrDefault(ip, 0) + 1);
            }
        }

        return visitsPerIp.values().stream().mapToInt(Integer::intValue).max().orElse(0);
    }

    private String extractDomain(String referer) {
        int start = referer.indexOf("//") + 2;
        int end = referer.indexOf("/", start);
        if (end == -1) {
            end = referer.length();
        }
        return referer.substring(start, end);
    }

}
