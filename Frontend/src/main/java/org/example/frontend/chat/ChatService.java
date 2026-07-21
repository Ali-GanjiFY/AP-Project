package org.example.frontend.chat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.frontend.shared.UserSession;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ChatService {

    private static final String BASE_URL =
            "http://localhost:8080/api/conversations";

    private final HttpClient httpClient;
    private final Gson gson;

    public ChatService() {
        this.httpClient = HttpClient.newHttpClient();
        this.gson = new Gson();
    }


    public List<ConversationResponse> getMyConversations()
            throws IOException, InterruptedException {

        HttpRequest request = requestBuilder(BASE_URL)
                .GET()
                .build();

        HttpResponse<String> response = send(request);
        ensureSuccessful(response, "دریافت لیست گفتگوها");

        Type listType =
                new TypeToken<List<ConversationResponse>>() {}.getType();

        List<ConversationResponse> conversations =
                gson.fromJson(response.body(), listType);

        return conversations == null ? List.of() : conversations;
    }

    /**
     * شروع گفتگو یا دریافت گفتگوی موجود برای یک آگهی.
     */
    public ConversationResponse startOrGetConversation(Long advertisementId)
            throws IOException, InterruptedException {

        validateId(advertisementId, "advertisementId");

        HttpRequest request = requestBuilder(
                BASE_URL + "/advertisements/" + advertisementId
        )
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = send(request);
        ensureSuccessful(response, "شروع یا دریافت گفتگو");

        return gson.fromJson(
                response.body(),
                ConversationResponse.class
        );
    }

    /**
     * دریافت اطلاعات یک گفتگو.
     */
    public ConversationResponse getConversation(Long conversationId)
            throws IOException, InterruptedException {

        validateId(conversationId, "conversationId");

        HttpRequest request = requestBuilder(
                BASE_URL + "/" + conversationId
        )
                .GET()
                .build();

        HttpResponse<String> response = send(request);
        ensureSuccessful(response, "دریافت اطلاعات گفتگو");

        return gson.fromJson(
                response.body(),
                ConversationResponse.class
        );
    }

    //دریافت تاریخچه پیام‌های گفتگو

    public List<ChatMessageResponse> getMessages(Long conversationId)
            throws IOException, InterruptedException {

        validateId(conversationId, "conversationId");

        HttpRequest request = requestBuilder(
                BASE_URL + "/" + conversationId + "/messages"
        )
                .GET()
                .build();

        HttpResponse<String> response = send(request);
        ensureSuccessful(response, "دریافت پیام‌ها");

        Type listType =
                new TypeToken<List<ChatMessageResponse>>() {}.getType();

        List<ChatMessageResponse> messages =
                gson.fromJson(response.body(), listType);

        return messages == null ? List.of() : messages;
    }


     // ارسال و ذخیره پیام جدید.

    public ChatMessageResponse sendMessage(
            Long conversationId,
            String content
    ) throws IOException, InterruptedException {

        validateId(conversationId, "conversationId");

        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException(
                    "متن پیام نمی‌تواند خالی باشد."
            );
        }

        SendMessageRequest body =
                new SendMessageRequest(content.trim());

        String json = gson.toJson(body);

        HttpRequest request = requestBuilder(
                BASE_URL + "/" + conversationId + "/messages"
        )
                .POST(HttpRequest.BodyPublishers.ofString(
                        json,
                        StandardCharsets.UTF_8
                ))
                .build();

        HttpResponse<String> response = send(request);
        ensureSuccessful(response, "ارسال پیام");

        return gson.fromJson(
                response.body(),
                ChatMessageResponse.class
        );
    }

    /**
     * علامت‌زدن پیام‌های دریافتی به‌عنوان دیده‌شده.
     */
    public void markMessagesAsSeen(Long conversationId)
            throws IOException, InterruptedException {

        validateId(conversationId, "conversationId");

        HttpRequest request = requestBuilder(
                BASE_URL + "/" + conversationId + "/messages/seen"
        )
                .PUT(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = send(request);
        ensureSuccessful(response, "ثبت مشاهده پیام‌ها");
    }

    private HttpRequest.Builder requestBuilder(String url) {
        String token = UserSession.getInstance().getToken();

        if (token == null || token.isBlank()) {
            throw new IllegalStateException(
                    "توکن ورود موجود نیست. دوباره وارد حساب شوید."
            );
        }

        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json; charset=UTF-8")
                .header("Accept", "application/json");
    }

    private HttpResponse<String> send(HttpRequest request)
            throws IOException, InterruptedException {

        return httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)
        );
    }

    private void ensureSuccessful(
            HttpResponse<String> response,
            String operation
    ) {
        int status = response.statusCode();

        if (status < 200 || status >= 300) {
            throw new RuntimeException(
                    operation + " ناموفق بود." +
                            "\nHTTP Status: " + status +
                            "\nResponse: " + response.body()
            );
        }
    }

    private void validateId(Long id, String name) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    name + " نامعتبر است."
            );
        }
    }
}
