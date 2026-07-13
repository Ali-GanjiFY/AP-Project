package org.example.frontend.advertisement;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Collections;
import java.util.List;

public class FavoriteService {

    private static final String BASE_URL = "http://localhost:8080/api/favorites";

    private final HttpClient httpClient;
    private final Gson gson;

    public FavoriteService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        this.gson = new Gson();
    }

    public boolean isFavorite(String token, Long advertisementId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/" + advertisementId + "/status"))
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return Boolean.parseBoolean(response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String addFavorite(String token, Long advertisementId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/" + advertisementId))
                    .header("Authorization", "Bearer " + token)
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 201 || response.statusCode() == 200) {
                return "SUCCESS";
            }
            return extractError(response.body(), "خطا در افزودن به علاقه‌مندی‌ها");
        } catch (java.net.ConnectException e) {
            return "خطا: امکان اتصال به سرور بک‌اند وجود ندارد.";
        } catch (Exception e) {
            e.printStackTrace();
            return "خطایی در سیستم رخ داده است: " + e.getMessage();
        }
    }

    public String removeFavorite(String token, Long advertisementId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/" + advertisementId))
                    .header("Authorization", "Bearer " + token)
                    .DELETE()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 204 || response.statusCode() == 200) {
                return "SUCCESS";
            }
            return extractError(response.body(), "خطا در حذف از علاقه‌مندی‌ها");
        } catch (java.net.ConnectException e) {
            return "خطا: امکان اتصال به سرور بک‌اند وجود ندارد.";
        } catch (Exception e) {
            e.printStackTrace();
            return "خطایی در سیستم رخ داده است: " + e.getMessage();
        }
    }

    public List<FavoriteItem> getMyFavorites(String token) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .header("Accept", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                Type listType = new TypeToken<List<FavoriteItem>>() {}.getType();
                List<FavoriteItem> items = gson.fromJson(response.body(), listType);
                return items != null ? items : Collections.emptyList();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    private String extractError(String body, String fallback) {
        try {
            JsonObject err = gson.fromJson(body, JsonObject.class);
            if (err != null && err.has("message")) {
                return err.get("message").getAsString();
            }
        } catch (Exception ignored) {
        }
        return fallback;
    }

    public static class FavoriteItem {
        private Long id;
        private String savedAt;
        private Advertisement advertisement;

        public Long getId() { return id; }
        public String getSavedAt() { return savedAt; }
        public Advertisement getAdvertisement() { return advertisement; }
    }
}