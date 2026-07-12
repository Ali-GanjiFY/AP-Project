package org.example.frontend.chat;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.Gson; // Import Gson
import org.example.frontend.shared.UserSession;

public class ChatService {

    private static final String BASE_URL = "http://localhost:8080/api/conversations";
    private final HttpClient httpClient;
    private final Gson gson = new Gson(); // Instantiate Gson

    public ChatService() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public ConversationResponse startOrGetConversation(Long advertisementId) throws IOException, InterruptedException {
        String token = UserSession.getInstance().getToken();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/advertisements/" + advertisementId))
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new RuntimeException("Failed to start conversation: " + response.statusCode() + " - " + response.body());
        }

        // Parse the JSON response into ConversationResponse object
        return gson.fromJson(response.body(), ConversationResponse.class);
    }
}
