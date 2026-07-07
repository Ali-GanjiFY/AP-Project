package org.example.backend.repository;

import org.example.backend.entity.AdvertisementEntity;
import org.example.backend.entity.AdvertisementImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdvertisementImageRepository extends JpaRepository<AdvertisementImageEntity, Long> {
    List<AdvertisementImageEntity> findByAdvertisement(AdvertisementEntity advertisement);
    void deleteByAdvertisement(AdvertisementEntity advertisement);
}

