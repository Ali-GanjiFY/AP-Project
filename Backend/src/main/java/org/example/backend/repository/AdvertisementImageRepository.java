package org.example.backend.repository;

import org.example.backend.entity.AdvertisementEntity;
import org.example.backend.entity.AdvertisementImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Contract for advertisement image repository.
 */
@Repository
public interface AdvertisementImageRepository extends JpaRepository<AdvertisementImageEntity, Long> {
    /**
     * Finds by advertisement.
     * @param advertisement the advertisement
     * @return the result
     */
    List<AdvertisementImageEntity> findByAdvertisement(AdvertisementEntity advertisement);
    /**
     * Deletes by advertisement.
     * @param advertisement the advertisement
     */
    void deleteByAdvertisement(AdvertisementEntity advertisement);
}

