package org.example.backend.repository;

import org.example.backend.entity.Advertisement;
import org.example.backend.entity.Favorite;
import org.example.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUserOrderBySavedAtDesc(User user);
    Optional<Favorite> findByUserAndAdvertisement(User user, Advertisement advertisement);
    void deleteByUserAndAdvertisement(User user, Advertisement advertisement);
    boolean existsByUserAndAdvertisement(User user, Advertisement advertisement);
}