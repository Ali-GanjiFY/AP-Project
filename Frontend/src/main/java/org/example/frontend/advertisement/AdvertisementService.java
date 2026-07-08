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

public class AdvertisementService {

    private static final String BASE_URL = "http://localhost:8080/api/advertisements";
    private final HttpClient httpClient;
    private final Gson gson;

    public AdvertisementService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        this.gson = new Gson();
    }


    public List<Advertisement> getAllActiveAds() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Type listType = new TypeToken<List<Advertisement>>() {}.getType();
                List<Advertisement> ads = gson.fromJson(response.body(), listType);
                return ads != null ? ads : Collections.emptyList();
            } else {
                System.err.println("خطا در دریافت آگهی‌ها!" + response.statusCode());
                return Collections.emptyList();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}