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

/**
 * Represents rating service.
 */
public class RatingService {

    private static final String BASE_URL = "http://localhost:8080/api/ratings";

    private final HttpClient httpClient;
    private final Gson gson;

    /**
     * Constructs a new RatingService.
     */
    public RatingService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        this.gson = new Gson();
    }

    /**
     * GET /api/ratings/advertisements/{id} -> public.
     * @param advertisementId the advertisement id
     * @return the result
     */
    public List<RatingDto> getRatingsByAdvertisement(Long advertisementId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/advertisements/" + advertisementId))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Type listType = new TypeToken<List<RatingDto>>() {}.getType();
                List<RatingDto> ratings = gson.fromJson(response.body(), listType);
                return ratings != null ? ratings : Collections.emptyList();
            }
            System.err.println("خطا در دریافت نظرات آگهی! " + response.statusCode());
            return Collections.emptyList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * GET /api/ratings/me -> ratings received by the logged-in user (as seller).
     * @param token the token
     * @return the result
     */
    public List<RatingDto> getMyReceivedRatings(String token) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/me"))
                    .header("Accept", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Type listType = new TypeToken<List<RatingDto>>() {}.getType();
                List<RatingDto> ratings = gson.fromJson(response.body(), listType);
                return ratings != null ? ratings : Collections.emptyList();
            }
            System.err.println("خطا در دریافت امتیازهای دریافتی! " + response.statusCode());
            return Collections.emptyList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * GET /api/ratings/me/summary -> average + count for the logged-in user (as seller).
     * @param token the token
     * @return the result
     */
    public RatingSummaryDto getMyRatingSummary(String token) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/me/summary"))
                    .header("Accept", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return gson.fromJson(response.body(), RatingSummaryDto.class);
            }
            System.err.println("خطا در دریافت میانگین امتیاز! " + response.statusCode());
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * POST /api/ratings -> create a rating for the seller of the given advertisement.
     * @param token the token
     * @param advertisementId the advertisement id
     * @param score the score
     * @param comment the comment
     * @return the result
     */
    public String createRating(String token, Long advertisementId, int score, String comment) {
        try {
            JsonObject body = new JsonObject();
            body.addProperty("advertisementId", advertisementId);
            body.addProperty("score", score);
            body.addProperty("comment", comment);

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
                return "خطا در ثبت امتیاز! " + response.statusCode();
            }
        } catch (java.net.ConnectException e) {
            return "خطا: امکان اتصال به سرور بک‌اند وجود ندارد.";
        } catch (Exception e) {
            e.printStackTrace();
            return "خطایی در سیستم رخ داده است: " + e.getMessage();
        }
    }

    /**
     * Represents rating dto.
     */
    public static class RatingDto {
        private Long id;
        private Integer score;
        private String comment;
        private String ratedAt;
        private String buyerUsername;

        /**
         * Gets id.
         * @return the result
         */
        public Long getId() { return id; }
        /**
         * Gets score.
         * @return the result
         */
        public Integer getScore() { return score; }
        /**
         * Gets comment.
         * @return the result
         */
        public String getComment() { return comment; }
        /**
         * Gets rated at.
         * @return the result
         */
        public String getRatedAt() { return ratedAt; }
        /**
         * Gets buyer username.
         * @return the result
         */
        public String getBuyerUsername() { return buyerUsername; }
    }

    /**
     * Represents rating summary dto.
     */
    public static class RatingSummaryDto {
        private double averageScore;
        private long totalCount;

        /**
         * Gets average score.
         * @return the result
         */
        public double getAverageScore() { return averageScore; }
        /**
         * Gets total count.
         * @return the result
         */
        public long getTotalCount() { return totalCount; }
    }
}