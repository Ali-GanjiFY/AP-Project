package org.example.backend.repository;

import org.example.backend.entity.Advertisement;
import org.example.backend.entity.AdvertisementImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdvertisementImageRepository extends JpaRepository<AdvertisementImage, Long> {
    List<AdvertisementImage> findByAdvertisement(Advertisement advertisement);
    void deleteByAdvertisement(Advertisement advertisement);
}

