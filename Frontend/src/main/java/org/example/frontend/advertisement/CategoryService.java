package org.example.frontend.advertisement;

import com.google.gson.Gson;
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

public class CategoryService {

    private static final String BASE_URL = "http://localhost:8080/api/categories";
    private final HttpClient httpClient;

    public CategoryService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    // GET /api/categories
    public List<CategoryOption> getAllCategories() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                List<CategoryOption> result = new ArrayList<>();
                JsonArray arr = JsonParser.parseString(response.body()).getAsJsonArray();
                for (int i = 0; i < arr.size(); i++) {
                    JsonObject obj = arr.get(i).getAsJsonObject();
                    boolean active = !obj.has("active") || obj.get("active").getAsBoolean();
                    if (!active)
                        continue;
                    Long id = obj.get("id").getAsLong();
                    String name = obj.get("name").getAsString();
                    result.add(new CategoryOption(id, name));
                }
                return result;
            } else {
                System.err.println("خطا در دریافت دسته‌بندی‌ها!" + response.statusCode());
                return Collections.emptyList();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}