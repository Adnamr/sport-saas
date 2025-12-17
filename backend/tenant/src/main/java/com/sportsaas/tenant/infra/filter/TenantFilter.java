package com.sportsaas.tenant.infra.filter;

import com.sportsaas.tenant.context.TenantContext;
import com.sportsaas.tenant.domain.model.Tenant;
import com.sportsaas.tenant.domain.service.TenantService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

/**
 * Filtre HTTP pour la résolution du tenant.
 *
 * <p>Ce filtre intercepte chaque requête HTTP et résout le tenant
 * à partir du header X-Tenant-ID. Le tenant résolu est stocké
 * dans le {@link TenantContext} pour être accessible dans toute
 * l'application.</p>
 *
 * <p>Le filtre s'exécute très tôt dans la chaîne (Order = -100)
 * pour que le tenant soit disponible pour tous les autres filtres.</p>
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 100)
@RequiredArgsConstructor
@Slf4j
public class TenantFilter extends OncePerRequestFilter {

    public static final String TENANT_HEADER = "X-Tenant-ID";

    private final TenantService tenantService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        try {
            String tenantHeader = request.getHeader(TENANT_HEADER);

            if (tenantHeader != null && !tenantHeader.isBlank()) {
                resolveTenant(tenantHeader, response);
            } else {
                log.debug("No tenant header found for request: {}", request.getRequestURI());
            }

            filterChain.doFilter(request, response);

        } finally {
            TenantContext.clear();
        }
    }

    private void resolveTenant(String tenantHeader, HttpServletResponse response) throws IOException {
        try {
            UUID tenantId = UUID.fromString(tenantHeader);
            Optional<Tenant> tenant = tenantService.findById(tenantId);

            if (tenant.isPresent()) {
                if (tenant.get().isActive()) {
                    TenantContext.setCurrentTenantId(tenantId);
                    log.debug("Tenant resolved: {} ({})", tenant.get().getName(), tenantId);
                } else {
                    log.warn("Tenant {} is not active (status: {})", tenantId, tenant.get().getStatus());
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Tenant is not active");
                }
            } else {
                log.warn("Tenant not found: {}", tenantId);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid tenant ID");
            }

        } catch (IllegalArgumentException e) {
            log.warn("Invalid tenant ID format: {}", tenantHeader);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid tenant ID format");
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        // Exclude health checks, actuator, and public endpoints
        return path.startsWith("/actuator")
                || path.startsWith("/health")
                || path.equals("/")
                || path.startsWith("/api/public");
    }
}
