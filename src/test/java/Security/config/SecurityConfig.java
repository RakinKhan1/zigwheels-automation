package security.config;

import java.util.List;

public final class SecurityConfig {

    public static final List<String> TARGETS = List.of(
            "https://www.zigwheels.com/"
    );

    public static final String ZAP_HOST =
            System.getenv().getOrDefault("ZAP_HOST", "127.0.0.1");

    public static final int ZAP_PORT = Integer.parseInt(
            System.getenv().getOrDefault("ZAP_PORT", "8080"));

    public static final String ZAP_API_KEY =
            System.getenv().getOrDefault("ZAP_API_KEY", "mhke42j0t1d6ljiina270fgaqc");

    public static final String ZAP_PROXY =
            "http://" + ZAP_HOST + ":" + ZAP_PORT;

    public static final int MAX_HIGH_ALERTS = 0;
    public static final int MAX_MEDIUM_ALERTS = 5;

    private SecurityConfig() {
    }
}