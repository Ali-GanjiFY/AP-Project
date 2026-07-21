package org.example.backend.service;

import org.example.backend.dto.request.SendMessageRequest;
import org.example.backend.dto.response.ChatMessageResponse;
import org.example.backend.entity.UserEntity;

import java.util.List;

/**
 * Contract for message service.
 */
public interface MessageService {

    /**
     * Send message in conversation (sender must be participant).
     * @param conversationId the conversation id
     * @param sender the sender
     * @param request the request
     * @return the result
     */
    ChatMessageResponse sendMessage(Long conversationId, UserEntity sender, SendMessageRequest request);

    /**
     * Get all messages (user must be participant).
     * @param conversationId the conversation id
     * @param currentUser the current user
     * @return the result
     */
    List<ChatMessageResponse> getConversationMessages(Long conversationId, UserEntity currentUser);

    /**
     * Mark messages from other user as seen.
     * @param conversationId the conversation id
     * @param currentUser the current user
     */
    void markMessagesAsSeen(Long conversationId, UserEntity currentUser);
}