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
                return parseCityListFromJson(response.body());
            } else {
                System.err.println("خطا در دریافت شهرها!" + response.statusCode());
                return Collections.emptyList();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public String createCity(String token, String name, String province) {
        try {
            JsonObject bodyJson = new JsonObject();
            bodyJson.addProperty("name", name);

            if (province != null && !province.isBlank()) {
                bodyJson.addProperty("province", province);
            }

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .POST(HttpRequest.BodyPublishers.ofString(bodyJson.toString()))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201 || response.statusCode() == 200) {
                return "SUCCESS";
            } else {
                return "خطا در افزودن شهر!" + response.statusCode();
            }

        } catch (java.net.ConnectException e) {
            return "خطا: امکان اتصال به سرور بک‌اند وجود ندارد.";
        } catch (Exception e) {
            e.printStackTrace();
            return "خطایی در سیستم رخ داده است: " + e.getMessage();
        }
    }

    public String deleteCity(String token, Long id) {
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
                return "خطا در حذف شهر!" + response.statusCode();
            }

        } catch (java.net.ConnectException e) {
            return "خطا: امکان اتصال به سرور بک‌اند وجود ندارد.";
        } catch (Exception e) {
            e.printStackTrace();
            return "خطایی در سیستم رخ داده است: " + e.getMessage();
        }
    }

    private List<CityOption> parseCityListFromJson(String jsonBody) {
        List<CityOption> result = new ArrayList<>();
        JsonArray jsonArray = JsonParser.parseString(jsonBody).getAsJsonArray();

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject cityObject = jsonArray.get(i).getAsJsonObject();
            Long cityId = cityObject.get("id").getAsLong();
            String cityName = cityObject.get("name").getAsString();
            String province = null;
            if (cityObject.has("province") && !cityObject.get("province").isJsonNull()) {
                province = cityObject.get("province").getAsString();
            }
            result.add(new CityOption(cityId, cityName, province));
        }

        return result;
    }
}