package test;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class TenantResolver {

    public String resolve(HttpServletRequest request) {
        String host = request.getServerName(); // 예: incheon.localhost
        if (host == null) return "default";

        String[] parts = host.split("\\.");
        return parts.length > 0 ? parts[0].toLowerCase() : "default";
    }
}