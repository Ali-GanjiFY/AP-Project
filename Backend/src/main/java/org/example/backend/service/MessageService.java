package org.example.backend.service;

import org.example.backend.dto.request.SendMessageRequest;
import org.example.backend.dto.response.ChatMessageResponse;
import org.example.backend.entity.UserEntity;

import java.util.List;

public interface MessageService {

    // Send a message in a conversation (sender must be a participant)
    ChatMessageResponse sendMessage(Long conversationId, UserEntity sender, SendMessageRequest request);

    // Get all messages in a conversation (currentUser must be a participant)
    List<ChatMessageResponse> getConversationMessages(Long conversationId, UserEntity currentUser);

    // Mark all unseen messages as seen (only marks messages from others)
    void markMessagesAsSeen(Long conversationId, UserEntity currentUser);
}