package org.example.backend.service;

import org.example.backend.dto.response.AdminDashboardStatsResponse;

/**
 * Contract for admin dashboard service.
 */
public interface AdminDashboardService {

    /**
     * Gets admin dashboard statistics.
     * @return the result
     */
    AdminDashboardStatsResponse getStats();
}
