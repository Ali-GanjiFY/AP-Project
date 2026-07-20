package org.example.backend.service.impl;

import org.example.backend.dto.request.AdvertisementSearchRequest;
import org.example.backend.dto.request.CreateAdvertisementRequest;
import org.example.backend.dto.request.UpdateAdvertisementRequest;
import org.example.backend.dto.response.AdvertisementDetailResponse;
import org.example.backend.dto.response.AdvertisementImageResponse;
import org.example.backend.dto.response.AdvertisementSummaryResponse;
import org.example.backend.dto.response.CategoryResponse;
import org.example.backend.dto.response.CityResponse;
import org.example.backend.entity.AdvertisementEntity;
import org.example.backend.entity.AdvertisementImageEntity;
import org.example.backend.entity.CategoryEntity;
import org.example.backend.entity.CityEntity;
import org.example.backend.entity.SellerRatingEntity;
import org.example.backend.entity.UserEntity;
import org.example.backend.enums.AdvertisementStatusEnum;
import org.example.backend.enums.RoleEnum;
import org.example.backend.enums.UserStatusEnum;
import org.example.backend.exception.InvalidInputException;
import org.example.backend.exception.ResourceNotFoundException;
import org.example.backend.exception.UnauthorizedException;
import org.example.backend.repository.AdvertisementImageRepository;
import org.example.backend.repository.AdvertisementRepository;
import org.example.backend.repository.SellerRatingRepository;
import org.example.backend.service.AdvertisementService;
import org.example.backend.service.CategoryService;
import org.example.backend.service.CityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AdvertisementServiceImpl implements AdvertisementService {

    private final AdvertisementRepository advertisementRepository;
    private final AdvertisementImageRepository advertisementImageRepository;
    private final SellerRatingRepository sellerRatingRepository;
    private final CategoryService categoryService;
    private final CityService cityService;

    public AdvertisementServiceImpl(AdvertisementRepository advertisementRepository,
                                    AdvertisementImageRepository advertisementImageRepository,
                                    SellerRatingRepository sellerRatingRepository,
                                    CategoryService categoryService,
                                    CityService cityService) {
        this.advertisementRepository = advertisementRepository;
        this.advertisementImageRepository = advertisementImageRepository;
        this.sellerRatingRepository = sellerRatingRepository;
        this.categoryService = categoryService;
        this.cityService = cityService;
    }

    // Create a new advertisement with PENDING status, requires active user and at least one image
    @Override
    @Transactional
    public AdvertisementDetailResponse createAdvertisement(UserEntity owner, CreateAdvertisementRequest request) {
        // Only active users can post advertisements
        if (owner.getStatus() != UserStatusEnum.ACTIVE) {
            throw new UnauthorizedException("حساب کاربری شما مسدود است و امکان ثبت آگهی وجود ندارد");
        }

        // Every advertisement must have at least one image
        if (request.getImagePaths() == null || request.getImagePaths().isEmpty()) {
            throw new InvalidInputException("آگهی باید حداقل شامل یک تصویر باشد");
        }

        // Fetch category and city
        CategoryEntity category = categoryService.getCategoryEntityById(request.getCategoryId());
        CityEntity city = cityService.getCityEntityById(request.getCityId());

        // Build and save advertisement
        AdvertisementEntity ad = new AdvertisementEntity();
        ad.setTitle(request.getTitle());
        ad.setDescription(request.getDescription());
        ad.setPrice(request.getPrice());
        ad.setOwner(owner);
        ad.setCategory(category);
        ad.setCity(city);
        ad.setStatus(AdvertisementStatusEnum.PENDING); // New ads need admin approval
        ad.setCreatedAt(LocalDateTime.now());

        AdvertisementEntity saved = advertisementRepository.save(ad);

        // Save all images
        if (request.getImagePaths() != null) {
            for (String path : request.getImagePaths()) {
                advertisementImageRepository.save(new AdvertisementImageEntity(path, saved));
            }
        }

        return getAdvertisementDetail(saved.getId(), owner);
    }

    // Update advertisement: only owner can edit, returns to PENDING for re-review
    @Override
    @Transactional
    public AdvertisementDetailResponse updateAdvertisement(Long adId, UserEntity currentUser, UpdateAdvertisementRequest request) {
        AdvertisementEntity ad = getAdvertisementEntityById(adId);
        ensureOwner(ad, currentUser); // Only owner can edit

        // Update only provided fields
        if (request.getTitle() != null) ad.setTitle(request.getTitle());
        if (request.getDescription() != null) ad.setDescription(request.getDescription());
        if (request.getPrice() != null) ad.setPrice(request.getPrice());
        if (request.getCategoryId() != null) ad.setCategory(categoryService.getCategoryEntityById(request.getCategoryId()));
        if (request.getCityId() != null) ad.setCity(cityService.getCityEntityById(request.getCityId()));

        // If the client sent an image list, it must be the full final list (existing + new),
        // and it fully replaces the current set of images for this ad.
        if (request.getImagePaths() != null) {
            if (request.getImagePaths().isEmpty()) {
                throw new InvalidInputException("آگهی باید حداقل شامل یک تصویر باشد");
            }
            advertisementImageRepository.deleteByAdvertisement(ad);
            for (String path : request.getImagePaths()) {
                advertisementImageRepository.save(new AdvertisementImageEntity(path, ad));
            }
        }

        // After editing, needs admin approval again
        ad.setStatus(AdvertisementStatusEnum.PENDING);
        ad.setUpdatedAt(LocalDateTime.now());
        advertisementRepository.save(ad);

        return getAdvertisementDetail(adId, currentUser);
    }

    // Soft delete: owner or admin can delete
    @Override
    @Transactional
    public void deleteAdvertisement(Long adId, UserEntity currentUser) {
        AdvertisementEntity ad = getAdvertisementEntityById(adId);
        ensureOwnerOrAdmin(ad, currentUser); // Owner or admin can delete
        ad.setStatus(AdvertisementStatusEnum.DELETED);
        ad.setUpdatedAt(LocalDateTime.now());
        advertisementRepository.save(ad);
    }

    // Mark as SOLD: only owner, only if currently ACTIVE
    @Override
    @Transactional
    public AdvertisementDetailResponse markAsSold(Long adId, UserEntity currentUser) {
        AdvertisementEntity ad = getAdvertisementEntityById(adId);
        ensureOwner(ad, currentUser); // Only owner can mark as sold

        // Only active ads can be marked as sold
        if (ad.getStatus() != AdvertisementStatusEnum.ACTIVE) {
            throw new InvalidInputException("فقط آگهی فعال را می‌توان فروخته‌شده علامت زد");
        }

        ad.setStatus(AdvertisementStatusEnum.SOLD);
        ad.setUpdatedAt(LocalDateTime.now());
        advertisementRepository.save(ad);
        return getAdvertisementDetail(adId, currentUser);
    }

    // Internal method: change status (used only by AdminReviewService)
    @Override
    @Transactional
    public void changeStatus(AdvertisementEntity ad, AdvertisementStatusEnum newStatus) {
        ad.setStatus(newStatus);
        ad.setUpdatedAt(LocalDateTime.now());
        advertisementRepository.save(ad);
    }

    // Get full advertisement details with seller ratings
    @Override
    public AdvertisementDetailResponse getAdvertisementDetail(Long adId, UserEntity currentUser) {
        AdvertisementEntity ad = getAdvertisementEntityById(adId);

        // Non-active ads (PENDING/REJECTED/DELETED/SOLD... except SOLD which stays
        // visible) must only be visible to their owner or an admin. Otherwise any
        // guest or user could view someone else's unreviewed/rejected ad just by
        // guessing its numeric id via GET /api/advertisements/{id}.
        boolean isOwner = currentUser != null && ad.getOwner().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser != null && currentUser.getRole() == RoleEnum.ADMIN;
        boolean publiclyVisible = ad.getStatus() == AdvertisementStatusEnum.ACTIVE || ad.getStatus() == AdvertisementStatusEnum.SOLD;

        if (!publiclyVisible && !isOwner && !isAdmin) {
            throw new ResourceNotFoundException("آگهی یافت نشد");
        }

        return toDetailResponse(ad, currentUser);
    }

    // Get advertisement entity by ID (internal use by other services)
    @Override
    public AdvertisementEntity getAdvertisementEntityById(Long adId) {
        return advertisementRepository.findById(adId)
                .orElseThrow(() -> new ResourceNotFoundException("آگهی یافت نشد"));
    }

    // Get all active advertisements for public browsing
    @Override
    @Transactional(readOnly = true)
    public List<AdvertisementSummaryResponse> getAllActiveAds() {
        return advertisementRepository.findByStatus(AdvertisementStatusEnum.ACTIVE)
                .stream()
                .map(this::toSummaryResponse)
                .collect(Collectors.toList());
    }

    // Get all advertisements owned by a specific user
    @Override
    public List<AdvertisementSummaryResponse> getMyAdvertisements(UserEntity owner) {
        return advertisementRepository.findByOwner(owner).stream().map(this::toSummaryResponse).toList();
    }

    // Get pending advertisements for admin review (oldest first)
    @Override
    public List<AdvertisementSummaryResponse> getPendingAdvertisements() {
        return advertisementRepository.findByStatusOrderByCreatedAtAsc(AdvertisementStatusEnum.PENDING)
                .stream().map(this::toSummaryResponse).toList();
    }

    @Override
    public List<AdvertisementSummaryResponse> searchAdvertisements(AdvertisementSearchRequest request) {
        // Validate price range
        if (request.getMinPrice() != null && request.getMaxPrice() != null
                && request.getMinPrice() > request.getMaxPrice()) {
            throw new InvalidInputException("حداقل قیمت نمی‌تواند از حداکثر قیمت بیشتر باشد");
        }

        // Start with all active ads
        List<AdvertisementEntity> ads = advertisementRepository.findByStatus(AdvertisementStatusEnum.ACTIVE);

        // اگر یک کتگوری برای فیلتر انتخاب شده، باید هم خودش و هم زیرمجموعه‌هاش (اگر والد باشه) در نظر گرفته بشه
        Set<Long> allowedCategoryIds = null;
        if (request.getCategoryId() != null) {
            CategoryEntity selectedCategory = categoryService.getCategoryEntityById(request.getCategoryId());
            allowedCategoryIds = new HashSet<>();
            allowedCategoryIds.add(selectedCategory.getId());

            // اگر این کتگوری، خودش والد یک یا چند زیرمجموعه است، آی‌دی همه‌ی فرزندانش هم اضافه می‌شود
            if (selectedCategory.getSubCategories() != null) {
                for (CategoryEntity child : selectedCategory.getSubCategories()) {
                    allowedCategoryIds.add(child.getId());
                }
            }
        }
        final Set<Long> finalAllowedCategoryIds = allowedCategoryIds;

        // Apply filters using stream (in-memory filtering)
        return ads.stream()
                // Keyword search: title or description
                .filter(ad -> request.getKeyword() == null || request.getKeyword().isBlank()
                        || ad.getTitle().toLowerCase().contains(request.getKeyword().toLowerCase())
                        || ad.getDescription().toLowerCase().contains(request.getKeyword().toLowerCase()))
                // Category filter (شامل زیرمجموعه‌ها هم می‌شود)
                .filter(ad -> finalAllowedCategoryIds == null || finalAllowedCategoryIds.contains(ad.getCategory().getId()))
                // City filter
                .filter(ad -> request.getCityId() == null || ad.getCity().getId().equals(request.getCityId()))
                // Price range filter
                .filter(ad -> request.getMinPrice() == null || ad.getPrice() >= request.getMinPrice())
                .filter(ad -> request.getMaxPrice() == null || ad.getPrice() <= request.getMaxPrice())
                // Sorting
                .sorted(buildComparator(request))
                .map(this::toSummaryResponse)
                .toList();
    }

    // Build comparator for sorting based on request
    private Comparator<AdvertisementEntity> buildComparator(AdvertisementSearchRequest request) {
        Comparator<AdvertisementEntity> comparator = "price".equalsIgnoreCase(request.getSortBy())
                ? Comparator.comparing(AdvertisementEntity::getPrice)
                : Comparator.comparing(AdvertisementEntity::getCreatedAt);

        return "desc".equalsIgnoreCase(request.getSortDirection()) ? comparator.reversed() : comparator;
    }

    // Ensure current user is the owner
    private void ensureOwner(AdvertisementEntity ad, UserEntity currentUser) {
        if (!ad.getOwner().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("شما اجازه‌ی تغییر این آگهی را ندارید");
        }
    }

    // Ensure current user is either owner or admin
    private void ensureOwnerOrAdmin(AdvertisementEntity ad, UserEntity currentUser) {
        boolean isOwner = ad.getOwner().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole() == RoleEnum.ADMIN;
        if (!isOwner && !isAdmin) {
            throw new UnauthorizedException("شما اجازه‌ی حذف این آگهی را ندارید");
        }
    }

    // Convert Advertisement to Summary DTO (for list views)
    private AdvertisementSummaryResponse toSummaryResponse(AdvertisementEntity ad) {
        String mainImage = (ad.getImages() != null && !ad.getImages().isEmpty())
                ? ad.getImages().get(0).getImagePath() : null;

        return new AdvertisementSummaryResponse(
                ad.getId(), ad.getTitle(), ad.getPrice(), ad.getCity().getName(),
                ad.getCategory().getName(), ad.getStatus(), ad.getCreatedAt(), mainImage
        );
    }

    // Convert Advertisement to Detail DTO with full data and seller ratings
    private AdvertisementDetailResponse toDetailResponse(AdvertisementEntity ad, UserEntity currentUser) {
        // Fetch real images
        List<AdvertisementImageResponse> images = advertisementImageRepository.findByAdvertisement(ad).stream()
                .map(img -> new AdvertisementImageResponse(img.getId(), img.getImagePath()))
                .toList();

        // Build category response with parent info
        CategoryEntity category = ad.getCategory();
        CategoryResponse categoryResponse = new CategoryResponse(
                category.getId(), category.getName(), category.getDescription(),
                category.getParentCategory() != null ? category.getParentCategory().getId() : null,
                category.getParentCategory() != null ? category.getParentCategory().getName() : null,
                category.isActive()
        );

        // Build city response
        CityResponse cityResponse = new CityResponse(
                ad.getCity().getId(), ad.getCity().getName(), ad.getCity().getProvince()
        );

        // Calculate seller's average rating and count (real data)
        UserEntity owner = ad.getOwner();
        List<SellerRatingEntity> ratings = sellerRatingRepository.findBySeller(owner);
        double avgRating = ratings.stream().mapToInt(SellerRatingEntity::getScore).average().orElse(0.0);
        long ratingCount = sellerRatingRepository.countBySeller(owner);

        // Check if current user is the owner
        boolean ownedByCurrentUser = currentUser != null && currentUser.getId().equals(owner.getId());

        return new AdvertisementDetailResponse(
                ad.getId(), ad.getTitle(), ad.getDescription(), ad.getPrice(), ad.getStatus(),
                ad.getCreatedAt(), ad.getUpdatedAt(),
                categoryResponse, cityResponse, images,
                owner.getId(), owner.getFullName(), owner.getUsername(),
                avgRating, ratingCount,
                ownedByCurrentUser
        );
    }
}