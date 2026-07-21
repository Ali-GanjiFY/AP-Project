package org.example.backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Represents health controller.
 */
@RestController
@RequestMapping("/api/health")
public class HealthController {

    /**
     * Health.
     * @return the result
     */
    @GetMapping
    public String health() {
        return "Backend is running!";
    }
}