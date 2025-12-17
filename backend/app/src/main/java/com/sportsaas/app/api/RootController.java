package com.sportsaas.app.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Controller racine pour les informations de base de l'API.
 */
@RestController
public class RootController {

    @Value("${info.app.name:Sport SaaS}")
    private String appName;

    @Value("${info.app.version:1.0.0}")
    private String appVersion;

    @GetMapping("/")
    public Map<String, Object> root() {
        return Map.of(
            "name", appName,
            "version", appVersion,
            "status", "running",
            "timestamp", LocalDateTime.now()
        );
    }

    @GetMapping("/api")
    public Map<String, Object> apiInfo() {
        return Map.of(
            "name", appName + " API",
            "version", appVersion,
            "documentation", "/swagger-ui.html"
        );
    }
}
