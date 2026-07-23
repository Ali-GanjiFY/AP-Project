package org.example.frontend.advertisement;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.List;

/**
 * Represents advertisement service.
 */
public class AdvertisementService {

    private static final String BASE_URL = "http://localhost:8080/api/advertisements";
    private static final String CITIES_URL = "http://localhost:8080/api/cities";
    private static final String CATEGORIES_URL = "http://localhost:8080/api/categories";

    private final HttpClient httpClient;
    private final Gson gson;

    /**
     * Constructs a new AdvertisementService.
     */
    public AdvertisementService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        this.gson = new Gson();
    }

    /**
     * Gets all active ads.
     * @return the result
     */
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
                System.err.println("خطا در دریافت آگهی‌ها! " + response.statusCode());
                return Collections.emptyList();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * Searches for advertisements.
     * @param keyword the keyword
     * @param categoryId the category id
     * @param cityId the city id
     * @param minPrice the min price
     * @param maxPrice the max price
     * @param sortBy the sort by
     * @param sortDirection the sort direction
     * @return the result
     */
    public List<Advertisement> searchAdvertisements(String keyword, Long categoryId, Long cityId,
                                                    Double minPrice, Double maxPrice,
                                                    String sortBy, String sortDirection) {
        try {
            StringBuilder urlBuilder = new StringBuilder(BASE_URL + "/search?");

            if (keyword != null && !keyword.isBlank()) {
                urlBuilder.append("keyword=")
                        .append(URLEncoder.encode(keyword, StandardCharsets.UTF_8))
                        .append("&");
            }
            if (categoryId != null) {
                urlBuilder.append("categoryId=").append(categoryId).append("&");
            }
            if (cityId != null) {
                urlBuilder.append("cityId=").append(cityId).append("&");
            }
            if (minPrice != null) {
                urlBuilder.append("minPrice=").append(minPrice).append("&");
            }
            if (maxPrice != null) {
                urlBuilder.append("maxPrice=").append(maxPrice).append("&");
            }
            if (sortBy != null && !sortBy.isBlank()) {
                urlBuilder.append("sortBy=").append(sortBy).append("&");
            }
            if (sortDirection != null && !sortDirection.isBlank()) {
                urlBuilder.append("sortDirection=").append(sortDirection).append("&");
            }

            String finalUrl = urlBuilder.toString();
            if (finalUrl.endsWith("&") || finalUrl.endsWith("?")) {
                finalUrl = finalUrl.substring(0, finalUrl.length() - 1);
            }

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(finalUrl))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Type listType = new TypeToken<List<Advertisement>>() {}.getType();
                List<Advertisement> ads = gson.fromJson(response.body(), listType);
                return ads != null ? ads : Collections.emptyList();
            } else {
                System.err.println("خطا در جست‌وجوی آگهی‌ها! کد وضعیت: " + response.statusCode());
                return Collections.emptyList();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * Gets my advertisements.
     * @param token the token
     * @return the result
     */
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
                System.err.println("خطا در دریافت آگهی‌های من! " + response.statusCode());
                return Collections.emptyList();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * Gets all advertisements regardless of status (admin only).
     * @param token the token
     * @return the result
     */
    public List<Advertisement> getAllAdvertisementsForAdmin(String token) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/all"))
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
                System.err.println("خطا در دریافت کل آگهی‌ها! " + response.statusCode());
                return Collections.emptyList();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * Deletes advertisement.
     * @param token the token
     * @param id the id
     * @return the result
     */
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
            return "خطا در حذف آگهی! " + response.statusCode();
        } catch (java.net.ConnectException e) {
            return "خطا: امکان اتصال به سرور بک‌اند وجود ندارد.";
        } catch (Exception e) {
            e.printStackTrace();
            return "خطایی در سیستم رخ داده است: " + e.getMessage();
        }
    }

    /**
     * Marks as sold.
     * @param token the token
     * @param id the id
     * @return the result
     */
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
            } catch (Exception ignored) {
            }

            return "خطا در تغییر وضعیت آگهی! " + response.statusCode();
        } catch (java.net.ConnectException e) {
            return "خطا: امکان اتصال به سرور بک‌اند وجود ندارد.";
        } catch (Exception e) {
            e.printStackTrace();
            return "خطایی در سیستم رخ داده است: " + e.getMessage();
        }
    }

    /**
     * Gets advertisement detail.
     * @param id the id
     * @return the result
     */
    public AdvertisementDetail getAdvertisementDetail(Long id) {
        return getAdvertisementDetail(id, null);
    }

    /**
     * Gets advertisement detail.
     * @param id the id
     * @param token the token
     * @return the result
     */
    public AdvertisementDetail getAdvertisementDetail(Long id, String token) {
        try {
            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/" + id))
                    .header("Accept", "application/json")
                    .GET();

            if (token != null && !token.isBlank()) {
                builder.header("Authorization", "Bearer " + token);
            }

            HttpRequest request = builder.build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return gson.fromJson(response.body(), AdvertisementDetail.class);
            } else {
                System.err.println("خطا در دریافت جزئیات آگهی! " + response.statusCode());
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Creates advertisement.
     * @param token the token
     * @param title the title
     * @param description the description
     * @param price the price
     * @param categoryId the category id
     * @param cityId the city id
     * @param imagePaths the image paths
     * @return the result
     */
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
                } catch (Exception ignored) {
                }
                return "خطا در ثبت آگهی! " + response.statusCode();
            }
        } catch (java.net.ConnectException e) {
            return "خطا: امکان اتصال به سرور بک‌اند وجود ندارد.";
        } catch (Exception e) {
            e.printStackTrace();
            return "خطایی در سیستم رخ داده است: " + e.getMessage();
        }
    }

    /**
     * PUT /api/advertisements/{id} -> owner only; backend moves the ad back to PENDING for re-review imagePaths: pass null to leave images untouched, or the FULL final list (remaining existing images + newly uploaded ones) to replace the ad's images entirely.
     * @param token the token
     * @param id the id
     * @param title the title
     * @param description the description
     * @param price the price
     * @param categoryId the category id
     * @param cityId the city id
     * @param imagePaths the image paths
     * @return the result
     */
    public String updateAdvertisement(String token, Long id, String title, String description,
                                      Double price, Long categoryId, Long cityId, List<String> imagePaths) {
        try {
            JsonObject body = new JsonObject();
            body.addProperty("title", title);
            body.addProperty("description", description);
            body.addProperty("price", price);
            body.addProperty("categoryId", categoryId);
            body.addProperty("cityId", cityId);
            if (imagePaths != null) {
                body.add("imagePaths", gson.toJsonTree(imagePaths));
            }

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/" + id))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .PUT(HttpRequest.BodyPublishers.ofString(gson.toJson(body)))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return "SUCCESS";
            } else {
                try {
                    JsonObject err = gson.fromJson(response.body(), JsonObject.class);
                    if (err != null && err.has("message")) {
                        return err.get("message").getAsString();
                    }
                } catch (Exception ignored) {
                }
                return "خطا در ویرایش آگهی! " + response.statusCode();
            }
        } catch (java.net.ConnectException e) {
            return "خطا: امکان اتصال به سرور بک‌اند وجود ندارد.";
        } catch (Exception e) {
            e.printStackTrace();
            return "خطایی در سیستم رخ داده است: " + e.getMessage();
        }
    }

    /**
     * Gets all cities.
     * @return the result
     */
    public List<CityDto> getAllCities() {

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(CITIES_URL))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Type listType = new TypeToken<List<CityDto>>() {}.getType();
                List<CityDto> cities = gson.fromJson(response.body(), listType);
                return cities != null ? cities : Collections.emptyList();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * Gets all categories.
     * @return the result
     */
    public List<CategoryDto> getAllCategories() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(CATEGORIES_URL))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Type listType = new TypeToken<List<CategoryDto>>() {}.getType();
                List<CategoryDto> categories = gson.fromJson(response.body(), listType);
                return categories != null ? categories : Collections.emptyList();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * Represents city dto.
     */
    public static class CityDto {
        private Long id;
        private String name;

        /**
         * Gets id.
         * @return the result
         */
        public Long getId() {
            return id;
        }

        /**
         * Gets name.
         * @return the result
         */
        public String getName() {
            return name;
        }

        /**
         * Converts to string.
         * @return the result
         */
        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * Represents category dto.
     */
    public static class CategoryDto {
        private Long id;
        private String name;

        /**
         * Gets id.
         * @return the result
         */
        public Long getId() {
            return id;
        }

        /**
         * Gets name.
         * @return the result
         */
        public String getName() {
            return name;
        }

        /**
         * Converts to string.
         * @return the result
         */
        @Override
        public String toString() {
            return name;
        }
    }
}