package org.example.frontend.auth;

import org.example.frontend.shared.UserSession;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class AuthService {

    private static final String BASE_URL = "http://localhost:8080/api/auth";
    private final HttpClient httpClient;

    public AuthService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    public String login(String username, String password) {
        try {
            String jsonRequestBody = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonRequestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            String responseBody = response.body();

            if (statusCode == 200) {
                String token = extractJsonValue(responseBody, "token");
                String userIdStr = extractJsonValue(responseBody, "userId");
                String responseUsername = extractJsonValue(responseBody, "username");
                String role = extractJsonValue(responseBody, "role");

                UserSession session = UserSession.getInstance();
                session.setToken(token);
                session.setUserId(Long.parseLong(userIdStr));
                session.setUsername(responseUsername);
                session.setRole(role);

                return "SUCCESS";
            } else {
                String errorMessage = extractJsonValue(responseBody, "message");
                return errorMessage != null ? errorMessage : "خطای ناشناخته در ورود به سیستم";
            }
        } catch (java.net.ConnectException e) {
            return "خطا: امکان اتصال به سرور بک‌اَند وجود ندارد. مطمئن شوید سرور روشن است.";
        } catch (Exception e) {
            e.printStackTrace();
            return "خطایی در سیستم رخ داده است: " + e.getMessage();
        }
    }

    // متد جدید ثبت‌نام بر اساس ساختار RegisterRequest بک‌اَند
    public String register(String fullName, String username, String password, String phone, String email) {
        try {
            // ساخت بدنه جیسون به صورت دستی و تمیز
            String jsonRequestBody = String.format(
                    "{\"fullName\":\"%s\",\"username\":\"%s\",\"password\":\"%s\",\"phone\":\"%s\",\"email\":\"%s\"}",
                    fullName, username, password, phone, email
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/register"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonRequestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            String responseBody = response.body();

            // طبق کنترلر بک‌اَند، ثبت‌نام موفق با کد 201 (CREATED) برمی‌گردد
            if (statusCode == 201 || statusCode == 200) {
                // ثبت‌نام موفقیت‌آمیز بود؛ اطلاعات توکن سشن را هم ذخیره می‌کنیم
                String token = extractJsonValue(responseBody, "token");
                String userIdStr = extractJsonValue(responseBody, "userId");
                String responseUsername = extractJsonValue(responseBody, "username");
                String role = extractJsonValue(responseBody, "role");

                UserSession session = UserSession.getInstance();
                session.setToken(token);
                session.setUserId(Long.parseLong(userIdStr));
                session.setUsername(responseUsername);
                session.setRole(role);

                return "SUCCESS";
            } else {
                String errorMessage = extractJsonValue(responseBody, "message");
                return errorMessage != null ? errorMessage : "خطای ناشناخته در ثبت‌نام";
            }
        } catch (java.net.ConnectException e) {
            return "خطا: امکان اتصال به سرور بک‌اَند وجود ندارد. مطمئن شوید سرور روشن است.";
        } catch (Exception e) {
            e.printStackTrace();
            return "خطایی در ثبت‌نام رخ داده است: " + e.getMessage();
        }
    }

    private String extractJsonValue(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int index = json.indexOf(searchKey);
        if (index == -1) return null;

        int startValue = index + searchKey.length();
        while (startValue < json.length() && (json.charAt(startValue) == ' ' || json.charAt(startValue) == ':')) {
            startValue++;
        }

        char firstChar = json.charAt(startValue);
        if (firstChar == '"') {
            int endValue = json.indexOf("\"", startValue + 1);
            return json.substring(startValue + 1, endValue);
        } else {
            int endValue = startValue;
            while (endValue < json.length() && json.charAt(endValue) != ',' && json.charAt(endValue) != '}') {
                endValue++;
            }
            return json.substring(startValue, endValue).trim();
        }
    }
}
