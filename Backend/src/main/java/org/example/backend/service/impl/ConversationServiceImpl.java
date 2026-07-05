package org.example.backend.service.impl;

import org.example.backend.dto.response.ConversationResponse;
import org.example.backend.entity.Advertisement;
import org.example.backend.entity.ChatMessage;
import org.example.backend.entity.Conversation;
import org.example.backend.entity.User;
import org.example.backend.enums.UserStatus;
import org.example.backend.exception.InvalidInputException;
import org.example.backend.exception.ResourceNotFoundException;
import org.example.backend.exception.UnauthorizedException;
import org.example.backend.repository.ChatMessageRepository;
import org.example.backend.repository.ConversationRepository;
import org.example.backend.service.ConversationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConversationServiceImpl implements ConversationService {

    private final ConversationRepository conversationRepository;
    private final ChatMessageRepository chatMessageRepository;

    public ConversationServiceImpl(ConversationRepository conversationRepository,
                                   ChatMessageRepository chatMessageRepository) {
        this.conversationRepository = conversationRepository;
        this.chatMessageRepository = chatMessageRepository;
    }

    // Start a new conversation or get existing one between buyer and seller
    @Override
    @Transactional
    public ConversationResponse startOrGetConversation(User buyer, Advertisement advertisement) {
        // Prevent users from chatting with themselves
        if (advertisement.getOwner().getId().equals(buyer.getId())) {
            throw new InvalidInputException("شما نمی‌توانید با آگهی خودتان گفتگو کنید");
        }

        // Both buyer and seller must be active
        if (buyer.getStatus() != UserStatus.ACTIVE || advertisement.getOwner().getStatus() != UserStatus.ACTIVE) {
            throw new UnauthorizedException("امکان شروع گفتگو برای کاربر مسدودشده وجود ندارد");
        }

        // Find existing conversation or create new one
        Conversation conversation = conversationRepository.findByBuyerAndAdvertisement(buyer, advertisement)
                .orElseGet(() -> conversationRepository.save(
                        new Conversation(buyer, advertisement.getOwner(), advertisement)));

        return toResponse(conversation, buyer);
    }

    // Get all conversations for a user (both as buyer and seller)
    @Override
    public List<ConversationResponse> getUserConversations(User user) {
        // Query conversations where user is either buyer or seller
        return conversationRepository.findByBuyerOrSellerOrderByLastMessageAtDesc(user, user).stream()
                .map(c -> toResponse(c, user))
                .toList();
    }

    // Get conversation by ID with access control
    @Override
    public ConversationResponse getConversationById(Long conversationId, User currentUser) {
        return toResponse(getConversationEntityById(conversationId, currentUser), currentUser);
    }

    // Get conversation entity with access control (internal use)
    @Override
    public Conversation getConversationEntityById(Long conversationId, User currentUser) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("گفتگو یافت نشد"));

        // Verify current user is a participant
        boolean isParticipant = conversation.getBuyer().getId().equals(currentUser.getId())
                || conversation.getSeller().getId().equals(currentUser.getId());

        if (!isParticipant) {
            throw new UnauthorizedException("شما به این گفتگو دسترسی ندارید");
        }
        return conversation;
    }

    // Update last message timestamp
    @Override
    @Transactional
    public void touchLastMessageAt(Conversation conversation, LocalDateTime time) {
        conversation.setLastMessageAt(time);
        conversationRepository.save(conversation);
    }

    // Convert Conversation entity to ConversationResponse DTO
    private ConversationResponse toResponse(Conversation conversation, User currentUser) {
        // Determine the other participant
        User other = conversation.getBuyer().getId().equals(currentUser.getId())
                ? conversation.getSeller() : conversation.getBuyer();

        // Get last message for preview
        ChatMessage lastMessage = chatMessageRepository.findTopByConversationOrderBySentAtDesc(conversation);

        return new ConversationResponse(
                conversation.getId(),
                conversation.getAdvertisement().getId(),
                conversation.getAdvertisement().getTitle(),
                other.getId(),
                other.getUsername(),
                conversation.getLastMessageAt(),
                lastMessage != null ? lastMessage.getContent() : null
        );
    }
}