package com.chemquest.backend.controller;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

/** A tiny "is everything working?" endpoint: GET http://localhost:8080/api/health */
@RestController
@RequestMapping("/api")
public class HealthController {

    private final JdbcTemplate jdbc;

    // Spring creates the JdbcTemplate for us and passes it in here.
    public HealthController(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("app", "ChemQuest backend");
        result.put("status", "UP");
        try {
            String version = jdbc.queryForObject("SELECT VERSION()", String.class);
            result.put("database", "UP");
            result.put("mysqlVersion", version);
        } catch (Exception e) {
            result.put("database", "DOWN");
            result.put("databaseError", e.getClass().getSimpleName());
        }
        return result;
    }
}
