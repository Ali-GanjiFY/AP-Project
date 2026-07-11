package org.example.frontend.admin;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.example.frontend.advertisement.Advertisement;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdminReviewService {

    private static final String BASE_URL = "http://localhost:8080/api/admin/advertisements";

    private final HttpClient httpClient;
    private final Gson gson;

    public AdminReviewService() {
        httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        gson = new Gson();
    }

    public List<Advertisement> getPendingAdvertisements(String token) {
        List<Advertisement> result = new ArrayList<>();

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/pending"))
                    .header("Accept", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Advertisement[] adsArray = gson.fromJson(response.body(), Advertisement[].class);
                if (adsArray != null) {
                    result = Arrays.asList(adsArray);
                }
            } else {
                System.err.println("خطا در دریافت آگهی‌های در انتظار بررسی!" + response.statusCode());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public String reviewAdvertisement(String token, Long advertisementId, String decision, String note) {
        try {
            JsonObject body = new JsonObject();
            body.addProperty("decision", decision);

            if (note == null) {
                note = "";
            }
            body.addProperty("note", note);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/" + advertisementId + "/review"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .PUT(HttpRequest.BodyPublishers.ofString(gson.toJson(body)))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return "SUCCESS";
            } else {
                return "خطا در ثبت تصمیم!" + response.statusCode();
            }

        } catch (java.net.ConnectException e) {
            return "خطا: امکان اتصال به سرور بک‌اند وجود ندارد.";
        } catch (Exception e) {
            e.printStackTrace();
            return "خطایی در سیستم رخ داده است: " + e.getMessage();
        }
    }
}