public class UserAgent {
    private final String osType;
    private final String browserType;

    public UserAgent(String userAgentString) {
        String[] parts = userAgentString.split(";");
        if (parts.length>=2){
            String fragments = parts[1].trim();
            int slashIndex = fragments.indexOf('/');
            if (slashIndex!=-1){
                String osBrowser = fragments.substring(0, slashIndex).trim();
                this.osType=extractOsType(osBrowser);
                this.browserType=extractBrowserType(osBrowser);
            }
            else{
                this.osType = "Unknown";
                this.browserType = "Unknown";
            }
        } else {
            this.osType = "Unknown";
            this.browserType = "Unknown";
        }
    }

    private String extractOsType(String osBrowser){
        if (osBrowser.contains("Windows")) return "Windows";
        else if (osBrowser.contains("macOS")) return "macOS";
        else if (osBrowser.contains("Linux")) return "Linux";
        else return "Unknown";
    }

    private String extractBrowserType(String osBrowser){
        if (osBrowser.contains("Edge")) return "Edge";
        else if (osBrowser.contains("Firefox")) return "Firefox";
        else if (osBrowser.contains("Chrome")) return "Chrome";
        else if (osBrowser.contains("Opera")) return "Opera";
        else return "Other";
    }

    public String getOsType() {
        return osType;
    }

    public String getBrowserType() {
        return browserType;
    }
}
