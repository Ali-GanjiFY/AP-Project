package org.example.backend.service.impl;

import org.example.backend.dto.response.AdminDashboardStatsResponse;
import org.example.backend.entity.AdvertisementEntity;
import org.example.backend.enums.AdvertisementStatusEnum;
import org.example.backend.repository.AdminReviewRepository;
import org.example.backend.repository.AdvertisementRepository;
import org.example.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for AdminDashboardServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
class AdminDashboardServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AdvertisementRepository advertisementRepository;

    @Mock
    private AdminReviewRepository adminReviewRepository;

    @InjectMocks
    private AdminDashboardServiceImpl adminDashboardService;

    /**
     * Should return dashboard statistics correctly.
     */
    @Test
    void getStats_ShouldReturnCorrectDashboardStats() {
        AdvertisementEntity pendingAd1 = new AdvertisementEntity();
        pendingAd1.setId(1L);
        pendingAd1.setStatus(AdvertisementStatusEnum.PENDING);

        AdvertisementEntity pendingAd2 = new AdvertisementEntity();
        pendingAd2.setId(2L);
        pendingAd2.setStatus(AdvertisementStatusEnum.PENDING);

        when(userRepository.count()).thenReturn(10L);
        when(advertisementRepository.count()).thenReturn(25L);
        when(adminReviewRepository.count()).thenReturn(7L);
        when(advertisementRepository.findByStatus(AdvertisementStatusEnum.PENDING))
                .thenReturn(List.of(pendingAd1, pendingAd2));

        AdminDashboardStatsResponse response = adminDashboardService.getStats();

        assertEquals(10L, response.getUsersCount());
        assertEquals(25L, response.getAdvertisementsCount());
        assertEquals(7L, response.getAdminReviewsCount());
        assertEquals(2L, response.getPendingAdvertisementsCount());

        verify(userRepository).count();
        verify(advertisementRepository).count();
        verify(adminReviewRepository).count();
        verify(advertisementRepository).findByStatus(AdvertisementStatusEnum.PENDING);
    }

    /**
     * Should return zero pending advertisements when there is no pending ad.
     */
    @Test
    void getStats_WhenNoPendingAdvertisements_ShouldReturnZeroPendingCount() {
        when(userRepository.count()).thenReturn(3L);
        when(advertisementRepository.count()).thenReturn(8L);
        when(adminReviewRepository.count()).thenReturn(2L);
        when(advertisementRepository.findByStatus(AdvertisementStatusEnum.PENDING))
                .thenReturn(List.of());

        AdminDashboardStatsResponse response = adminDashboardService.getStats();

        assertEquals(3L, response.getUsersCount());
        assertEquals(8L, response.getAdvertisementsCount());
        assertEquals(2L, response.getAdminReviewsCount());
        assertEquals(0L, response.getPendingAdvertisementsCount());

        verify(userRepository).count();
        verify(advertisementRepository).count();
        verify(adminReviewRepository).count();
        verify(advertisementRepository).findByStatus(AdvertisementStatusEnum.PENDING);
    }
}
