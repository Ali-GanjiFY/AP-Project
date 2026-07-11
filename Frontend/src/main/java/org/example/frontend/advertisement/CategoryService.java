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
        List<CategoryOption> all = fetchAll();
        List<CategoryOption> result = new ArrayList<>();
        for (CategoryOption c : all) {
            if (c.isActive()) {
                result.add(c);
            }
        }
        return result;
    }

    // GET /api/categories
    public List<CategoryOption> getAllCategoriesForAdmin() {
        return fetchAll();
    }

    private List<CategoryOption> fetchAll() {
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
                    Long id = obj.get("id").getAsLong();
                    String name = obj.get("name").getAsString();
                    String description = (obj.has("description") && !obj.get("description").isJsonNull())
                            ? obj.get("description").getAsString() : null;
                    Long parentId = (obj.has("parentCategoryId") && !obj.get("parentCategoryId").isJsonNull())
                            ? obj.get("parentCategoryId").getAsLong() : null;
                    String parentName = (obj.has("parentCategoryName") && !obj.get("parentCategoryName").isJsonNull())
                            ? obj.get("parentCategoryName").getAsString() : null;
                    boolean active = !obj.has("active") || obj.get("active").getAsBoolean();
                    result.add(new CategoryOption(id, name, description, parentId, parentName, active));
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

    // POST /api/categories -> admin only
    public String createCategory(String token, String name, String description, Long parentCategoryId) {
        try {
            JsonObject body = new JsonObject();
            body.addProperty("name", name);
            if (description != null && !description.isBlank()) {
                body.addProperty("description", description);
            }
            if (parentCategoryId != null) {
                body.addProperty("parentCategoryID", parentCategoryId);
            }
            body.addProperty("active", true);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201 || response.statusCode() == 200) {
                return "SUCCESS";
            } else {
                return "خطا در افزودن دسته‌بندی!" + response.statusCode();
            }
        } catch (java.net.ConnectException e) {
            return "خطا: امکان اتصال به سرور بک‌اند وجود ندارد.";
        } catch (Exception e) {
            e.printStackTrace();
            return "خطایی در سیستم رخ داده است: " + e.getMessage();
        }
    }

    // DELETE /api/categories/{id} -> admin only
    public String deleteCategory(String token, Long id) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/" + id))
                    .header("Authorization", "Bearer " + token)
                    .DELETE()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 204 || response.statusCode() == 200) {
                return "SUCCESS";
            } else {
                return "خطا در حذف دسته‌بندی!" + response.statusCode();
            }
        } catch (java.net.ConnectException e) {
            return "خطا: امکان اتصال به سرور بک‌اند وجود ندارد.";
        } catch (Exception e) {
            e.printStackTrace();
            return "خطایی در سیستم رخ داده است: " + e.getMessage();
        }
    }
}