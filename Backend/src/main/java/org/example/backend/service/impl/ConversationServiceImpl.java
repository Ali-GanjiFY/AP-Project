package org.example.backend.service.impl;

import org.example.backend.dto.response.ConversationResponse;
import org.example.backend.entity.AdvertisementEntity;
import org.example.backend.entity.ChatMessageEntity;
import org.example.backend.entity.ConversationEntity;
import org.example.backend.entity.UserEntity;
import org.example.backend.enums.UserStatusEnum;
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

/**
 * Represents conversation service impl.
 */
@Service
public class ConversationServiceImpl implements ConversationService {

    private final ConversationRepository conversationRepository;
    private final ChatMessageRepository chatMessageRepository;

    /**
     * Constructs a new ConversationServiceImpl.
     * @param conversationRepository the conversation repository
     * @param chatMessageRepository the chat message repository
     */
    public ConversationServiceImpl(ConversationRepository conversationRepository,
                                   ChatMessageRepository chatMessageRepository) {
        this.conversationRepository = conversationRepository;
        this.chatMessageRepository = chatMessageRepository;
    }

    /**
     * Start a new conversation or get existing one between buyer and seller.
     * @param buyer the buyer
     * @param advertisement the advertisement
     * @return the result
     */
    @Override
    @Transactional
    public ConversationResponse startOrGetConversation(UserEntity buyer, AdvertisementEntity advertisement) {
        // Prevent users from chatting with themselves
        if (advertisement.getOwner().getId().equals(buyer.getId())) {
            throw new InvalidInputException("شما نمی‌توانید با آگهی خودتان گفتگو کنید");
        }

        // Both buyer and seller must be active
        if (buyer.getStatus() != UserStatusEnum.ACTIVE || advertisement.getOwner().getStatus() != UserStatusEnum.ACTIVE) {
            throw new UnauthorizedException("امکان شروع گفتگو برای کاربر مسدودشده وجود ندارد");
        }

        // Find existing conversation or create new one
        ConversationEntity conversation = conversationRepository.findByBuyerAndAdvertisement(buyer, advertisement)
                .orElseGet(() -> conversationRepository.save(
                        new ConversationEntity(buyer, advertisement.getOwner(), advertisement)));

        return toResponse(conversation, buyer);
    }

    /**
     * Get all conversations for a user (both as buyer and seller).
     * @param user the user
     * @return the result
     */
    @Override
    public List<ConversationResponse> getUserConversations(UserEntity user) {
        // Query conversations where user is either buyer or seller
        return conversationRepository.findByBuyerOrSellerOrderByLastMessageAtDesc(user, user).stream()
                .map(c -> toResponse(c, user))
                .toList();
    }

    /**
     * Get conversation by ID with access control.
     * @param conversationId the conversation id
     * @param currentUser the current user
     * @return the result
     */
    @Override
    public ConversationResponse getConversationById(Long conversationId, UserEntity currentUser) {
        return toResponse(getConversationEntityById(conversationId, currentUser), currentUser);
    }

    /**
     * Get conversation entity with access control (internal use).
     * @param conversationId the conversation id
     * @param currentUser the current user
     * @return the result
     */
    @Override
    public ConversationEntity getConversationEntityById(Long conversationId, UserEntity currentUser) {
        ConversationEntity conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("گفتگو یافت نشد"));

        // Verify current user is a participant
        boolean isParticipant = conversation.getBuyer().getId().equals(currentUser.getId())
                || conversation.getSeller().getId().equals(currentUser.getId());

        if (!isParticipant) {
            throw new UnauthorizedException("شما به این گفتگو دسترسی ندارید");
        }
        return conversation;
    }

    /**
     * Update last message timestamp.
     * @param conversation the conversation
     * @param time the time
     */
    @Override
    @Transactional
    public void touchLastMessageAt(ConversationEntity conversation, LocalDateTime time) {
        conversation.setLastMessageAt(time);
        conversationRepository.save(conversation);
    }

    /**
     * Convert Conversation entity to ConversationResponse.
     * @param conversation the conversation
     * @param currentUser the current user
     * @return the result
     */
    private ConversationResponse toResponse(ConversationEntity conversation, UserEntity currentUser) {

        UserEntity other = conversation.getBuyer().getId().equals(currentUser.getId())
                ? conversation.getSeller() : conversation.getBuyer();

        // Get last message for preview
        ChatMessageEntity lastMessage = chatMessageRepository.findTopByConversationOrderBySentAtDesc(conversation);

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