package org.example.backend.service;

import org.example.backend.dto.request.SendMessageRequest;
import org.example.backend.dto.response.ChatMessageResponse;
import org.example.backend.entity.UserEntity;

import java.util.List;

public interface MessageService {

    // Send message in conversation (sender must be participant)
    ChatMessageResponse sendMessage(Long conversationId, UserEntity sender, SendMessageRequest request);

    // Get all messages (user must be participant)
    List<ChatMessageResponse> getConversationMessages(Long conversationId, UserEntity currentUser);

    // Mark messages from other user as seen
    void markMessagesAsSeen(Long conversationId, UserEntity currentUser);
}