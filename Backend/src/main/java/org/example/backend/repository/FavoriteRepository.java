package org.example.backend.repository;

import org.example.backend.entity.AdvertisementEntity;
import org.example.backend.entity.FavoriteEntity;
import org.example.backend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<FavoriteEntity, Long> {
    List<FavoriteEntity> findByUserOrderBySavedAtDesc(UserEntity user);
    Optional<FavoriteEntity> findByUserAndAdvertisement(UserEntity user, AdvertisementEntity advertisement);
    void deleteByUserAndAdvertisement(UserEntity user, AdvertisementEntity advertisement);
    boolean existsByUserAndAdvertisement(UserEntity user, AdvertisementEntity advertisement);
}