package org.example.backend.service.impl;

import org.example.backend.dto.request.SendMessageRequest;
import org.example.backend.dto.response.ChatMessageResponse;
import org.example.backend.entity.ChatMessageEntity;
import org.example.backend.entity.ConversationEntity;
import org.example.backend.entity.UserEntity;
import org.example.backend.enums.UserStatusEnum;
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
    public ChatMessageResponse sendMessage(Long conversationId, UserEntity sender, SendMessageRequest request) {
        // Verify sender is a participant in this conversation
        ConversationEntity conversation = conversationService.getConversationEntityById(conversationId, sender);

        // Both buyer and seller must be active (not blocked or deleted)
        if (conversation.getBuyer().getStatus() != UserStatusEnum.ACTIVE
                || conversation.getSeller().getStatus() != UserStatusEnum.ACTIVE) {
            throw new UnauthorizedException("امکان ارسال پیام برای کاربر مسدودشده وجود ندارد");
        }

        // Create and save the message
        ChatMessageEntity message = new ChatMessageEntity(request.getContent(), sender, conversation);
        ChatMessageEntity saved = chatMessageRepository.save(message);

        // Update the conversation's last message timestamp
        conversationService.touchLastMessageAt(conversation, saved.getSentAt());

        return toResponse(saved);
    }

    // Get all messages in a conversation with access control
    @Override
    public List<ChatMessageResponse> getConversationMessages(Long conversationId, UserEntity currentUser) {
        // Verify current user is a participant
        ConversationEntity conversation = conversationService.getConversationEntityById(conversationId, currentUser);
        // Return messages in chronological order
        return chatMessageRepository.findByConversationOrderBySentAtAsc(conversation).stream()
                .map(this::toResponse)
                .toList();
    }

    // Mark all unseen messages as seen (only messages from other users)
    @Override
    @Transactional
    public void markMessagesAsSeen(Long conversationId, UserEntity currentUser) {
        // Verify current user is a participant
        ConversationEntity conversation = conversationService.getConversationEntityById(conversationId, currentUser);
        List<ChatMessageEntity> unseen = chatMessageRepository.findByConversationAndSeenFalse(conversation);

        // Mark messages from others as seen, keep own messages unseen
        for (ChatMessageEntity message : unseen) {
            if (!message.getSender().getId().equals(currentUser.getId())) {
                message.setSeen(true);
            }
        }
        chatMessageRepository.saveAll(unseen);
    }

    // Convert ChatMessage entity to ChatMessageResponse DTO
    private ChatMessageResponse toResponse(ChatMessageEntity message) {
        return new ChatMessageResponse(
                message.getId(), message.getContent(), message.getSentAt(), message.isSeen(),
                message.getSender().getId(), message.getSender().getUsername()
        );
    }
}