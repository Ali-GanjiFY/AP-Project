package org.example.backend.repository;

import org.example.backend.entity.AdvertisementEntity;
import org.example.backend.entity.ConversationEntity;
import org.example.backend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<ConversationEntity, Long> {
    Optional<ConversationEntity> findByBuyerAndAdvertisement(UserEntity buyer, AdvertisementEntity advertisement);
    List<ConversationEntity> findByBuyerOrSellerOrderByLastMessageAtDesc(UserEntity buyer, UserEntity seller);
}
