package org.example.backend.repository;

import org.example.backend.entity.Advertisement;
import org.example.backend.entity.Conversation;
import org.example.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    Optional<Conversation> findByBuyerAndAdvertisement(User buyer, Advertisement advertisement);
    List<Conversation> findByBuyerOrSellerOrderByLastMessageAtDesc(User buyer, User seller);
}
