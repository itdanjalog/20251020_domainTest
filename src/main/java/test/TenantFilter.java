package test;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class TenantFilter implements Filter {

    private final TenantResolver tenantResolver;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String tenant = tenantResolver.resolve(httpRequest);

        TenantContext.setTenant(tenant);
        System.out.println("🌐 Current Tenant: " + tenant);

        try {
            chain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
}