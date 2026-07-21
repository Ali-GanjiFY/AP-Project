package org.example.backend.repository;

import org.example.backend.entity.AdvertisementEntity;
import org.example.backend.entity.ConversationEntity;
import org.example.backend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Contract for conversation repository.
 */
@Repository
public interface ConversationRepository extends JpaRepository<ConversationEntity, Long> {
    /**
     * Finds by buyer and advertisement.
     * @param buyer the buyer
     * @param advertisement the advertisement
     * @return the result
     */
    Optional<ConversationEntity> findByBuyerAndAdvertisement(UserEntity buyer, AdvertisementEntity advertisement);
    /**
     * Finds by buyer or seller order by last message at desc.
     * @param buyer the buyer
     * @param seller the seller
     * @return the result
     */
    List<ConversationEntity> findByBuyerOrSellerOrderByLastMessageAtDesc(UserEntity buyer, UserEntity seller);
}
