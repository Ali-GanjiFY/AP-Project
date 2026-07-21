package org.example.frontend.dashboard;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.frontend.shared.UserSession;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

/**
 * Represents admin user service.
 */
public class AdminUserService {
    private static final String BASE_URL = "http://localhost:8080/api/users";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    /**
     * دریافت لیست همه کاربران.
     * @return the result
     */
    public List<UserResponse> getAllUsers() throws Exception {
        String token = UserSession.getInstance().getToken();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Authorization", "Bearer " + token)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            Type listType = new TypeToken<List<UserResponse>>(){}.getType();
            return gson.fromJson(response.body(), listType);
        } else {
            throw new RuntimeException("خطا در دریافت کاربران. کد وضعیت: " + response.statusCode());
        }
    }

    /**
     * مسدود کردن کاربر.
     * @param userId the user id
     * @return the result
     */
    public UserResponse blockUser(Long userId) throws Exception {
        String token = UserSession.getInstance().getToken();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + userId + "/block"))
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return gson.fromJson(response.body(), UserResponse.class);
        } else {
            throw new RuntimeException("خطا در مسدود کردن کاربر.");
        }
    }

    /**
     * فعال‌سازی مجدد کاربر.
     * @param userId the user id
     * @return the result
     */
    public UserResponse unblockUser(Long userId) throws Exception {
        String token = UserSession.getInstance().getToken();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + userId + "/unblock"))
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return gson.fromJson(response.body(), UserResponse.class);
        } else {
            throw new RuntimeException("خطا در فعال‌سازی کاربر.");
        }
    }
}
