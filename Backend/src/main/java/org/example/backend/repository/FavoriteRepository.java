package org.example.backend.repository;

import org.example.backend.entity.AdvertisementEntity;
import org.example.backend.entity.FavoriteEntity;
import org.example.backend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Contract for favorite repository.
 */
@Repository
public interface FavoriteRepository extends JpaRepository<FavoriteEntity, Long> {
    /**
     * Finds by user order by saved at desc.
     * @param user the user
     * @return the result
     */
    List<FavoriteEntity> findByUserOrderBySavedAtDesc(UserEntity user);
    /**
     * Finds by user and advertisement.
     * @param user the user
     * @param advertisement the advertisement
     * @return the result
     */
    Optional<FavoriteEntity> findByUserAndAdvertisement(UserEntity user, AdvertisementEntity advertisement);
    /**
     * Deletes by user and advertisement.
     * @param user the user
     * @param advertisement the advertisement
     */
    void deleteByUserAndAdvertisement(UserEntity user, AdvertisementEntity advertisement);
    /**
     * Checks whether by user and advertisement.
     * @param user the user
     * @param advertisement the advertisement
     * @return the result
     */
    boolean existsByUserAndAdvertisement(UserEntity user, AdvertisementEntity advertisement);
}