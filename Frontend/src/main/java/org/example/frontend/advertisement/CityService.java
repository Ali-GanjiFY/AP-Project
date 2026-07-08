package org.example.frontend.advertisement;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CityService {

    private static final String BASE_URL = "http://localhost:8080/api/cities";
    private final HttpClient httpClient;

    public CityService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    // GET /api/cities
    public List<CityOption> getAllCities() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                List<CityOption> result = new ArrayList<>();
                JsonArray arr = JsonParser.parseString(response.body()).getAsJsonArray();
                for (int i = 0; i < arr.size(); i++) {
                    JsonObject obj = arr.get(i).getAsJsonObject();
                    Long id = obj.get("id").getAsLong();
                    String name = obj.get("name").getAsString();
                    result.add(new CityOption(id, name));
                }
                return result;
            } else {
                System.err.println("خطا در دریافت شهرها!" + response.statusCode());
                return Collections.emptyList();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}