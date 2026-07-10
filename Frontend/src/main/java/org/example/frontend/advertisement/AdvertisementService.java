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

    public List<Advertisement> getMyAdvertisements(String token) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/mine"))
                    .header("Accept", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Type listType = new TypeToken<List<Advertisement>>() {}.getType();
                List<Advertisement> ads = gson.fromJson(response.body(), listType);
                return ads != null ? ads : Collections.emptyList();
            } else {
                System.err.println("خطا در دریافت آگهی‌های من!" + response.statusCode());
                return Collections.emptyList();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public String deleteAdvertisement(String token, Long id) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/" + id))
                    .header("Authorization", "Bearer " + token)
                    .DELETE()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 204 || response.statusCode() == 200) {
                return "SUCCESS";
            }
            return "خطا در حذف آگهی! کد: " + response.statusCode();
        } catch (java.net.ConnectException e) {
            return "خطا: امکان اتصال به سرور بک‌اند وجود ندارد.";
        } catch (Exception e) {
            e.printStackTrace();
            return "خطایی در سیستم رخ داده است: " + e.getMessage();
        }
    }

    public String markAsSold(String token, Long id) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/" + id + "/sold"))
                    .header("Authorization", "Bearer " + token)
                    .PUT(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return "SUCCESS";
            }
            try {
                JsonObject err = gson.fromJson(response.body(), JsonObject.class);
                if (err != null && err.has("message")) {
                    return err.get("message").getAsString();
                }
            } catch (Exception ignored) { }
            return "خطا در تغییر وضعیت آگهی! کد: " + response.statusCode();
        } catch (java.net.ConnectException e) {
            return "خطا: امکان اتصال به سرور بک‌اند وجود ندارد.";
        } catch (Exception e) {
            e.printStackTrace();
            return "خطایی در سیستم رخ داده است: " + e.getMessage();
        }
    }

    public AdvertisementDetail getAdvertisementDetail(Long id) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/" + id))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return gson.fromJson(response.body(), AdvertisementDetail.class);
            } else {
                System.err.println("خطا در دریافت جزئیات آگهی!" + response.statusCode());
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public String createAdvertisement(String token, String title, String description,
                                      Double price, Long categoryId, Long cityId,
                                      List<String> imagePaths) {
        try {
            JsonObject body = new JsonObject();
            body.addProperty("title", title);
            body.addProperty("description", description);
            body.addProperty("price", price);
            body.addProperty("categoryId", categoryId);
            body.addProperty("cityId", cityId);
            body.add("imagePaths", gson.toJsonTree(imagePaths));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(body)))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201 || response.statusCode() == 200) {
                return "SUCCESS";
            } else {
                try {
                    JsonObject err = gson.fromJson(response.body(), JsonObject.class);
                    if (err != null && err.has("message")) {
                        return err.get("message").getAsString();
                    }
                } catch (Exception ignored) { }
                return "خطا در ثبت آگهی!" + response.statusCode();
            }
        } catch (java.net.ConnectException e) {
            return "خطا: امکان اتصال به سرور بک‌اَند وجود ندارد.";
        } catch (Exception e) {
            e.printStackTrace();
            return "خطایی در سیستم رخ داده است: " + e.getMessage();
        }
    }
}