package org.example.backend.service.impl;

import org.example.backend.dto.response.AdminDashboardStatsResponse;
import org.example.backend.enums.AdvertisementStatusEnum;
import org.example.backend.repository.AdminReviewRepository;
import org.example.backend.repository.AdvertisementRepository;
import org.example.backend.repository.UserRepository;
import org.example.backend.service.AdminDashboardService;
import org.springframework.stereotype.Service;

/**
 * Represents admin dashboard service impl.
 */
@Service
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private final UserRepository userRepository;
    private final AdvertisementRepository advertisementRepository;
    private final AdminReviewRepository adminReviewRepository;

    /**
     * Constructs a new AdminDashboardServiceImpl.
     * @param userRepository the user repository
     * @param advertisementRepository the advertisement repository
     * @param adminReviewRepository the admin review repository
     */
    public AdminDashboardServiceImpl(UserRepository userRepository,
                                     AdvertisementRepository advertisementRepository,
                                     AdminReviewRepository adminReviewRepository) {
        this.userRepository = userRepository;
        this.advertisementRepository = advertisementRepository;
        this.adminReviewRepository = adminReviewRepository;
    }

    /**
     * Gets admin dashboard statistics.
     * @return the result
     */
    @Override
    public AdminDashboardStatsResponse getStats() {
        long usersCount = userRepository.count();
        long advertisementsCount = advertisementRepository.count();
        long adminReviewsCount = adminReviewRepository.count();
        long pendingAdvertisementsCount = advertisementRepository
                .findByStatus(AdvertisementStatusEnum.PENDING)
                .size();

        return new AdminDashboardStatsResponse(
                usersCount,
                advertisementsCount,
                adminReviewsCount,
                pendingAdvertisementsCount
        );
    }
}

