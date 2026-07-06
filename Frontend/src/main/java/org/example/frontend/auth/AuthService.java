package org.example.frontend.auth;

import org.example.frontend.shared.UserSession;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class AuthService {

    private static final String BASE_URL = "http://localhost:8080/api/auth"; // آدرس پیش‌فرض بک‌اند
    private final HttpClient httpClient;

    public AuthService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    public String login(String username, String password) {
        try {
            // ساخت بدنه درخواست به صورت JSON
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
                // استخراج اطلاعات از AuthResponse JSON
                String token = extractJsonValue(responseBody, "token");
                String userIdStr = extractJsonValue(responseBody, "userId");
                String responseUsername = extractJsonValue(responseBody, "username");
                String role = extractJsonValue(responseBody, "role");

                // ذخیره اطلاعات در سشن برنامه
                UserSession session = UserSession.getInstance();
                session.setToken(token);
                session.setUserId(Long.parseLong(userIdStr));
                session.setUsername(responseUsername);
                session.setRole(role);

                return "SUCCESS";
            } else {
                // استخراج پیام خطا از ErrorResponse ارسالی از بک‌اند
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

    // یک متد کمکی ساده برای پیدا کردن مقادیر در رشته JSON بدون نیاز به کتابخانه‌های سنگین
    private String extractJsonValue(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int index = json.indexOf(searchKey);
        if (index == -1) return null;

        int startValue = index + searchKey.length();
        // حذف فاصله‌ها
        while (startValue < json.length() && (json.charAt(startValue) == ' ' || json.charAt(startValue) == ':')) {
            startValue++;
        }

        char firstChar = json.charAt(startValue);
        if (firstChar == '"') { // اگر رشته باشد
            int endValue = json.indexOf("\"", startValue + 1);
            return json.substring(startValue + 1, endValue);
        } else { // اگر عدد یا بولین باشد
            int endValue = startValue;
            while (endValue < json.length() && json.charAt(endValue) != ',' && json.charAt(endValue) != '}') {
                endValue++;
            }
            return json.substring(startValue, endValue).trim();
        }
    }
}

