package org.example.backend.service.impl;

import org.example.backend.dto.request.AdminDecisionRequest;
import org.example.backend.dto.response.AdminReviewResponse;
import org.example.backend.dto.response.AdvertisementSummaryResponse;
import org.example.backend.entity.AdminReviewEntity;
import org.example.backend.entity.AdvertisementEntity;
import org.example.backend.entity.UserEntity;
import org.example.backend.enums.AdvertisementStatusEnum;
import org.example.backend.enums.ReviewDecisionEnum;
import org.example.backend.exception.InvalidInputException;
import org.example.backend.exception.ResourceNotFoundException;
import org.example.backend.repository.AdminReviewRepository;
import org.example.backend.service.AdvertisementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for AdminReviewServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
class AdminReviewServiceImplTest {

    @Mock
    private AdminReviewRepository adminReviewRepository;

    @Mock
    private AdvertisementService advertisementService;

    @InjectMocks
    private AdminReviewServiceImpl adminReviewService;

    /**
     * Should approve a pending advertisement and change its status to ACTIVE.
     */
    @Test
    void reviewAdvertisement_WhenDecisionIsApproved_ShouldCreateReviewAndActivateAdvertisement() {
        UserEntity admin = createAdmin();
        AdvertisementEntity advertisement = createAdvertisement(100L, AdvertisementStatusEnum.PENDING);
        AdminDecisionRequest request = new AdminDecisionRequest(
                ReviewDecisionEnum.APPROVED,
                "آگهی تایید شد"
        );

        when(advertisementService.getAdvertisementEntityById(100L)).thenReturn(advertisement);
        when(adminReviewRepository.findByAdvertisement(advertisement)).thenReturn(Optional.empty());
        when(adminReviewRepository.save(any(AdminReviewEntity.class))).thenAnswer(invocation -> {
            AdminReviewEntity review = invocation.getArgument(0);
            review.setId(1L);
            return review;
        });

        AdminReviewResponse response = adminReviewService.reviewAdvertisement(admin, 100L, request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(ReviewDecisionEnum.APPROVED, response.getDecision());
        assertEquals("آگهی تایید شد", response.getNote());
        assertEquals("admin_user", response.getAdminUsername());
        assertEquals(100L, response.getAdvertisementId());
        assertNotNull(response.getReviewedAt());

        verify(advertisementService).getAdvertisementEntityById(100L);
        verify(adminReviewRepository).findByAdvertisement(advertisement);
        verify(adminReviewRepository).save(any(AdminReviewEntity.class));
        verify(advertisementService).changeStatus(advertisement, AdvertisementStatusEnum.ACTIVE);
    }

    /**
     * Should reject a pending advertisement and change its status to REJECTED.
     */
    @Test
    void reviewAdvertisement_WhenDecisionIsRejected_ShouldUpdateReviewAndRejectAdvertisement() {
        UserEntity admin = createAdmin();
        AdvertisementEntity advertisement = createAdvertisement(200L, AdvertisementStatusEnum.PENDING);
        AdminReviewEntity existingReview = new AdminReviewEntity("old note", admin, advertisement);
        existingReview.setId(5L);
        existingReview.setDecision(ReviewDecisionEnum.APPROVED);

        AdminDecisionRequest request = new AdminDecisionRequest(
                ReviewDecisionEnum.REJECTED,
                "اطلاعات آگهی ناقص است"
        );

        when(advertisementService.getAdvertisementEntityById(200L)).thenReturn(advertisement);
        when(adminReviewRepository.findByAdvertisement(advertisement)).thenReturn(Optional.of(existingReview));
        when(adminReviewRepository.save(any(AdminReviewEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AdminReviewResponse response = adminReviewService.reviewAdvertisement(admin, 200L, request);

        assertNotNull(response);
        assertEquals(5L, response.getId());
        assertEquals(ReviewDecisionEnum.REJECTED, response.getDecision());
        assertEquals("اطلاعات آگهی ناقص است", response.getNote());
        assertEquals("admin_user", response.getAdminUsername());
        assertEquals(200L, response.getAdvertisementId());
        assertNotNull(response.getReviewedAt());

        verify(advertisementService).getAdvertisementEntityById(200L);
        verify(adminReviewRepository).findByAdvertisement(advertisement);
        verify(adminReviewRepository).save(existingReview);
        verify(advertisementService).changeStatus(advertisement, AdvertisementStatusEnum.REJECTED);
    }

    /**
     * Should remove a pending advertisement and change its status to DELETED.
     */
    @Test
    void reviewAdvertisement_WhenDecisionIsRemoved_ShouldChangeAdvertisementStatusToDeleted() {
        UserEntity admin = createAdmin();
        AdvertisementEntity advertisement = createAdvertisement(300L, AdvertisementStatusEnum.PENDING);
        AdminDecisionRequest request = new AdminDecisionRequest(
                ReviewDecisionEnum.REMOVED,
                "آگهی حذف شود"
        );

        when(advertisementService.getAdvertisementEntityById(300L)).thenReturn(advertisement);
        when(adminReviewRepository.findByAdvertisement(advertisement)).thenReturn(Optional.empty());
        when(adminReviewRepository.save(any(AdminReviewEntity.class))).thenAnswer(invocation -> {
            AdminReviewEntity review = invocation.getArgument(0);
            review.setId(10L);
            return review;
        });

        AdminReviewResponse response = adminReviewService.reviewAdvertisement(admin, 300L, request);

        assertNotNull(response);
        assertEquals(10L, response.getId());
        assertEquals(ReviewDecisionEnum.REMOVED, response.getDecision());
        assertEquals("آگهی حذف شود", response.getNote());
        assertEquals(300L, response.getAdvertisementId());

        verify(advertisementService).getAdvertisementEntityById(300L);
        verify(adminReviewRepository).save(any(AdminReviewEntity.class));
        verify(advertisementService).changeStatus(advertisement, AdvertisementStatusEnum.DELETED);
    }

    /**
     * Should throw InvalidInputException when advertisement is not PENDING.
     */
    @Test
    void reviewAdvertisement_WhenAdvertisementIsNotPending_ShouldThrowInvalidInputException() {
        UserEntity admin = createAdmin();
        AdvertisementEntity advertisement = createAdvertisement(400L, AdvertisementStatusEnum.ACTIVE);
        AdminDecisionRequest request = new AdminDecisionRequest(
                ReviewDecisionEnum.REJECTED,
                "رد شود"
        );

        when(advertisementService.getAdvertisementEntityById(400L)).thenReturn(advertisement);

        InvalidInputException exception = assertThrows(
                InvalidInputException.class,
                () -> adminReviewService.reviewAdvertisement(admin, 400L, request)
        );

        assertEquals(
                "فقط آگهی‌های در انتظار بررسی (PENDING) قابل تایید یا رد هستند",
                exception.getMessage()
        );

        verify(advertisementService).getAdvertisementEntityById(400L);
        verify(adminReviewRepository, never()).findByAdvertisement(any(AdvertisementEntity.class));
        verify(adminReviewRepository, never()).save(any(AdminReviewEntity.class));
        verify(advertisementService, never()).changeStatus(any(AdvertisementEntity.class), any(AdvertisementStatusEnum.class));
    }

    /**
     * Should return review by advertisement id when review exists.
     */
    @Test
    void getReviewByAdvertisementId_WhenReviewExists_ShouldReturnReviewResponse() {
        UserEntity admin = createAdmin();
        AdvertisementEntity advertisement = createAdvertisement(500L, AdvertisementStatusEnum.REJECTED);

        AdminReviewEntity review = new AdminReviewEntity("رد به دلیل نقص اطلاعات", admin, advertisement);
        review.setId(20L);
        review.setDecision(ReviewDecisionEnum.REJECTED);

        when(advertisementService.getAdvertisementEntityById(500L)).thenReturn(advertisement);
        when(adminReviewRepository.findByAdvertisement(advertisement)).thenReturn(Optional.of(review));

        AdminReviewResponse response = adminReviewService.getReviewByAdvertisementId(500L);

        assertNotNull(response);
        assertEquals(20L, response.getId());
        assertEquals(ReviewDecisionEnum.REJECTED, response.getDecision());
        assertEquals("رد به دلیل نقص اطلاعات", response.getNote());
        assertEquals("admin_user", response.getAdminUsername());
        assertEquals(500L, response.getAdvertisementId());

        verify(advertisementService).getAdvertisementEntityById(500L);
        verify(adminReviewRepository).findByAdvertisement(advertisement);
    }

    /**
     * Should throw ResourceNotFoundException when review does not exist.
     */
    @Test
    void getReviewByAdvertisementId_WhenReviewDoesNotExist_ShouldThrowResourceNotFoundException() {
        AdvertisementEntity advertisement = createAdvertisement(600L, AdvertisementStatusEnum.PENDING);

        when(advertisementService.getAdvertisementEntityById(600L)).thenReturn(advertisement);
        when(adminReviewRepository.findByAdvertisement(advertisement)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> adminReviewService.getReviewByAdvertisementId(600L)
        );

        assertEquals("هنوز بازبینی‌ای برای این آگهی ثبت نشده است", exception.getMessage());

        verify(advertisementService).getAdvertisementEntityById(600L);
        verify(adminReviewRepository).findByAdvertisement(advertisement);
    }

    /**
     * Should delegate getting pending advertisements to AdvertisementService.
     */
    @Test
    void getPendingAdvertisements_ShouldDelegateToAdvertisementService() {
        List<AdvertisementSummaryResponse> pendingAdvertisements = List.of();

        when(advertisementService.getPendingAdvertisements()).thenReturn(pendingAdvertisements);

        List<AdvertisementSummaryResponse> result = adminReviewService.getPendingAdvertisements();

        assertNotNull(result);
        assertEquals(0, result.size());

        verify(advertisementService).getPendingAdvertisements();
    }

    /**
     * Creates admin user for tests.
     * @return the admin user
     */
    private UserEntity createAdmin() {
        UserEntity admin = new UserEntity();
        admin.setId(1L);
        admin.setUsername("admin_user");
        return admin;
    }

    /**
     * Creates advertisement for tests.
     * @param id the advertisement id
     * @param status the advertisement status
     * @return the advertisement
     */
    private AdvertisementEntity createAdvertisement(Long id, AdvertisementStatusEnum status) {
        AdvertisementEntity advertisement = new AdvertisementEntity();
        advertisement.setId(id);
        advertisement.setTitle("Test Advertisement");
        advertisement.setDescription("Test Description");
        advertisement.setPrice(1000.0);
        advertisement.setStatus(status);
        return advertisement;
    }
}
