import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogEntry {
    private final String ipAddress;
    private final String propertyOne;
    private final String propertyTwo;
    private final LocalDateTime dateTime;
    private final HttpMethod method;
    private final String requestPath;
    private final String httpCode;
    private final String httpVersion;
    private final int bytesSent;
    private final String referer;
    private final UserAgent userAgent;
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z");

    public LogEntry(String logEntry) {
        String regex = "^(?<ip>\\S+) (?<prop1>\\S+) (?<prop2>\\S+) \\[(?<time>[^\\]]+)\\] " +
                "\"(?<method>\\S+) (?<path>[^ ]+) (?<protocol>\\S+)\" " +
                "(?<status>\\d+) (?<size>\\d+) " +
                "\"(?<referer>[^\"]*)\" \"(?<userAgent>[^\"]*)\"";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(logEntry);

        if (!matcher.matches()) throw new IllegalArgumentException("Неверный формат строки!");

        this.ipAddress = matcher.group("ip");
        this.propertyOne = matcher.group("prop1");
        this.propertyTwo = matcher.group("prop2");

        // Парсинг даты и времени
        String date = matcher.group("time");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);
        this.dateTime = ZonedDateTime.parse(date, formatter).toLocalDateTime();

        this.method = HttpMethod.valueOf(matcher.group("method"));
        this.requestPath = matcher.group("path");
        this.httpVersion = matcher.group("protocol");
        this.httpCode = matcher.group("status");
        this.bytesSent = Integer.parseInt(matcher.group("size"));
        this.referer = matcher.group("referer");
        this.userAgent = new UserAgent(matcher.group("userAgent"));
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getPropertyOne() {
        return propertyOne;
    }

    public String getPropertyTwo() {
        return propertyTwo;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public String getHttpCode() {
        return httpCode;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public int getBytesSent() {
        return bytesSent;
    }

    public String getReferer() {
        return referer;
    }

    public UserAgent getUserAgent() {
        return userAgent;
    }
}

enum HttpMethod {
    GET, POST, PUT, DELETE, HEAD, OPTIONS, TRACE, CONNECT, PATCH
}