package org.example.frontend.dashboard;

import com.google.gson.Gson;
import org.example.frontend.shared.UserSession;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Represents admin dashboard service.
 */
public class AdminDashboardService {

    private static final String BASE_URL = "http://localhost:8080/api/admin/dashboard";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    /**
     * Gets admin dashboard statistics.
     * @return the result
     */
    public AdminDashboardStats getStats() throws Exception {
        String token = UserSession.getInstance().getToken();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/stats"))
                .header("Authorization", "Bearer " + token)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return gson.fromJson(response.body(), AdminDashboardStats.class);
        } else {
            throw new RuntimeException("خطا در دریافت آمار داشبورد. کد وضعیت: " + response.statusCode());
        }
    }
}
