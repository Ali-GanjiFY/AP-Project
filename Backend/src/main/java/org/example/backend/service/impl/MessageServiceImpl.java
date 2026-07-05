package org.example.backend.service.impl;

import org.example.backend.dto.request.SendMessageRequest;
import org.example.backend.dto.response.ChatMessageResponse;
import org.example.backend.entity.ChatMessage;
import org.example.backend.entity.Conversation;
import org.example.backend.entity.User;
import org.example.backend.enums.UserStatus;
import org.example.backend.exception.UnauthorizedException;
import org.example.backend.repository.ChatMessageRepository;
import org.example.backend.service.ConversationService;
import org.example.backend.service.MessageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ConversationService conversationService;

    public MessageServiceImpl(ChatMessageRepository chatMessageRepository,
                              ConversationService conversationService) {
        this.chatMessageRepository = chatMessageRepository;
        this.conversationService = conversationService;
    }

    // Send a message: validate participant and user status, update conversation timestamp
    @Override
    @Transactional
    public ChatMessageResponse sendMessage(Long conversationId, User sender, SendMessageRequest request) {
        // Verify sender is a participant in this conversation
        Conversation conversation = conversationService.getConversationEntityById(conversationId, sender);

        // Both buyer and seller must be active (not blocked or deleted)
        if (conversation.getBuyer().getStatus() != UserStatus.ACTIVE
                || conversation.getSeller().getStatus() != UserStatus.ACTIVE) {
            throw new UnauthorizedException("امکان ارسال پیام برای کاربر مسدودشده وجود ندارد");
        }

        // Create and save the message
        ChatMessage message = new ChatMessage(request.getContent(), sender, conversation);
        ChatMessage saved = chatMessageRepository.save(message);

        // Update the conversation's last message timestamp
        conversationService.touchLastMessageAt(conversation, saved.getSentAt());

        return toResponse(saved);
    }

    // Get all messages in a conversation with access control
    @Override
    public List<ChatMessageResponse> getConversationMessages(Long conversationId, User currentUser) {
        // Verify current user is a participant
        Conversation conversation = conversationService.getConversationEntityById(conversationId, currentUser);
        // Return messages in chronological order
        return chatMessageRepository.findByConversationOrderBySentAtAsc(conversation).stream()
                .map(this::toResponse)
                .toList();
    }

    // Mark all unseen messages as seen (only messages from other users)
    @Override
    @Transactional
    public void markMessagesAsSeen(Long conversationId, User currentUser) {
        // Verify current user is a participant
        Conversation conversation = conversationService.getConversationEntityById(conversationId, currentUser);
        List<ChatMessage> unseen = chatMessageRepository.findByConversationAndSeenFalse(conversation);

        // Mark messages from others as seen, keep own messages unseen
        for (ChatMessage message : unseen) {
            if (!message.getSender().getId().equals(currentUser.getId())) {
                message.setSeen(true);
            }
        }
        chatMessageRepository.saveAll(unseen);
    }

    // Convert ChatMessage entity to ChatMessageResponse DTO
    private ChatMessageResponse toResponse(ChatMessage message) {
        return new ChatMessageResponse(
                message.getId(), message.getContent(), message.getSentAt(), message.isSeen(),
                message.getSender().getId(), message.getSender().getUsername()
        );
    }
}