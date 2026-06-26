package org.example.backend.repository;

import org.example.backend.entity.Advertisement;
import org.example.backend.entity.User;
import org.example.backend.enums.AdvertisementStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
    List<Advertisement> findByStatus(AdvertisementStatus status);
    List<Advertisement> findByOwner(User owner);
    List<Advertisement> findByStatusOrderByCreatedAtAsc(AdvertisementStatus status);
}
