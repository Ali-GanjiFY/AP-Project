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

/**
 * Represents advertisement service impl.
 */
@Service
public class AdvertisementServiceImpl implements AdvertisementService {

    private final AdvertisementRepository advertisementRepository;
    private final AdvertisementImageRepository advertisementImageRepository;
    private final SellerRatingRepository sellerRatingRepository;
    private final CategoryService categoryService;
    private final CityService cityService;

    /**
     * Constructs a new AdvertisementServiceImpl.
     * @param advertisementRepository the advertisement repository
     * @param advertisementImageRepository the advertisement image repository
     * @param sellerRatingRepository the seller rating repository
     * @param categoryService the category service
     * @param cityService the city service
     */
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

    /**
     * Create new ad.
     * @param owner the owner
     * @param request the request
     * @return the result
     */
    @Override
    @Transactional
    public AdvertisementDetailResponse createAdvertisement(UserEntity owner, CreateAdvertisementRequest request) {
        if (owner.getStatus() != UserStatusEnum.ACTIVE) {
            throw new UnauthorizedException("حساب کاربری شما مسدود است و امکان ثبت آگهی وجود ندارد");
        }

        // Must have at least one image
        if (request.getImagePaths() == null || request.getImagePaths().isEmpty()) {
            throw new InvalidInputException("آگهی باید حداقل شامل یک تصویر باشد");
        }

        // Fetch category and city
        CategoryEntity category = categoryService.getCategoryEntityById(request.getCategoryId());
        CityEntity city = cityService.getCityEntityById(request.getCityId());

        AdvertisementEntity ad = new AdvertisementEntity();
        ad.setTitle(request.getTitle());
        ad.setDescription(request.getDescription());
        ad.setPrice(request.getPrice());
        ad.setOwner(owner);
        ad.setCategory(category);
        ad.setCity(city);
        ad.setStatus(AdvertisementStatusEnum.PENDING);
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

    /**
     * Update ad: owner only, returns to PENDING for re-review.
     * @param adId the ad id
     * @param currentUser the current user
     * @param request the request
     * @return the result
     */
    @Override
    @Transactional
    public AdvertisementDetailResponse updateAdvertisement(Long adId, UserEntity currentUser, UpdateAdvertisementRequest request) {
        AdvertisementEntity ad = getAdvertisementEntityById(adId);
        ensureOwner(ad, currentUser);

        // Update only provided fields
        if (request.getTitle() != null) ad.setTitle(request.getTitle());
        if (request.getDescription() != null) ad.setDescription(request.getDescription());
        if (request.getPrice() != null) ad.setPrice(request.getPrice());
        if (request.getCategoryId() != null) ad.setCategory(categoryService.getCategoryEntityById(request.getCategoryId()));
        if (request.getCityId() != null) ad.setCity(cityService.getCityEntityById(request.getCityId()));

        // Replace images if provided
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

    /**
     * Soft delete.
     * @param adId the ad id
     * @param currentUser the current user
     */
    @Override
    @Transactional
    public void deleteAdvertisement(Long adId, UserEntity currentUser) {
        AdvertisementEntity ad = getAdvertisementEntityById(adId);
        ensureOwnerOrAdmin(ad, currentUser);
        ad.setStatus(AdvertisementStatusEnum.DELETED);
        ad.setUpdatedAt(LocalDateTime.now());
        advertisementRepository.save(ad);
    }

    /**
     * Mark as SOLD.
     * @param adId the ad id
     * @param currentUser the current user
     * @return the result
     */
    @Override
    @Transactional
    public AdvertisementDetailResponse markAsSold(Long adId, UserEntity currentUser) {
        AdvertisementEntity ad = getAdvertisementEntityById(adId);
        ensureOwner(ad, currentUser);

        // Only active ads can be marked as sold
        if (ad.getStatus() != AdvertisementStatusEnum.ACTIVE) {
            throw new InvalidInputException("فقط آگهی فعال را می‌توان فروخته‌شده علامت زد");
        }

        ad.setStatus(AdvertisementStatusEnum.SOLD);
        ad.setUpdatedAt(LocalDateTime.now());
        advertisementRepository.save(ad);
        return getAdvertisementDetail(adId, currentUser);
    }

    /**
     * Change status.
     * @param ad the ad
     * @param newStatus the new status
     */
    @Override
    @Transactional
    public void changeStatus(AdvertisementEntity ad, AdvertisementStatusEnum newStatus) {
        ad.setStatus(newStatus);
        ad.setUpdatedAt(LocalDateTime.now());
        advertisementRepository.save(ad);
    }

    /**
     * Full details with seller ratings.
     * @param adId the ad id
     * @param currentUser the current user
     * @return the result
     */
    @Override
    public AdvertisementDetailResponse getAdvertisementDetail(Long adId, UserEntity currentUser) {
        AdvertisementEntity ad = getAdvertisementEntityById(adId);

        boolean isOwner = currentUser != null && ad.getOwner().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser != null && currentUser.getRole() == RoleEnum.ADMIN;
        boolean publiclyVisible = ad.getStatus() == AdvertisementStatusEnum.ACTIVE || ad.getStatus() == AdvertisementStatusEnum.SOLD;

        if (!publiclyVisible && !isOwner && !isAdmin) {
            throw new ResourceNotFoundException("آگهی یافت نشد");
        }

        return toDetailResponse(ad, currentUser);
    }

    /**
     * Gets advertisement entity by id.
     * @param adId the ad id
     * @return the result
     */
    @Override
    public AdvertisementEntity getAdvertisementEntityById(Long adId) {
        return advertisementRepository.findById(adId)
                .orElseThrow(() -> new ResourceNotFoundException("آگهی یافت نشد"));
    }

    /**
     * All active ads.
     * @return the result
     */
    @Override
    @Transactional(readOnly = true)
    public List<AdvertisementSummaryResponse> getAllActiveAds() {
        return advertisementRepository.findByStatus(AdvertisementStatusEnum.ACTIVE)
                .stream()
                .map(this::toSummaryResponse)
                .collect(Collectors.toList());
    }

    /**
     * Ads owned by specific user.
     * @param owner the owner
     * @return the result
     */
    @Override
    public List<AdvertisementSummaryResponse> getMyAdvertisements(UserEntity owner) {
        return advertisementRepository.findByOwner(owner).stream().map(this::toSummaryResponse).toList();
    }

    /**
     * Pending ads for admin (oldest first).
     * @return the result
     */
    @Override
    public List<AdvertisementSummaryResponse> getPendingAdvertisements() {
        return advertisementRepository.findByStatusOrderByCreatedAtAsc(AdvertisementStatusEnum.PENDING)
                .stream().map(this::toSummaryResponse).toList();
    }

    /**
     * Search: keyword, category, city, price, sorting.
     * @param request the request
     * @return the result
     */
    @Override
    public List<AdvertisementSummaryResponse> searchAdvertisements(AdvertisementSearchRequest request) {
        if (request.getMinPrice() != null && request.getMaxPrice() != null
                && request.getMinPrice() > request.getMaxPrice()) {
            throw new InvalidInputException("حداقل قیمت نمی‌تواند از حداکثر قیمت بیشتر باشد");
        }

        List<AdvertisementEntity> ads = advertisementRepository.findByStatus(AdvertisementStatusEnum.ACTIVE);

        // Build category filter including subcategories
        Set<Long> allowedCategoryIds = null;
        if (request.getCategoryId() != null) {
            CategoryEntity selectedCategory = categoryService.getCategoryEntityById(request.getCategoryId());
            allowedCategoryIds = new HashSet<>();
            allowedCategoryIds.add(selectedCategory.getId());

            if (selectedCategory.getSubCategories() != null) {
                for (CategoryEntity child : selectedCategory.getSubCategories()) {
                    allowedCategoryIds.add(child.getId());
                }
            }
        }
        final Set<Long> finalAllowedCategoryIds = allowedCategoryIds;

        // Apply filters via stream
        return ads.stream()
                // Keyword
                .filter(ad -> request.getKeyword() == null || request.getKeyword().isBlank()
                        || ad.getTitle().toLowerCase().contains(request.getKeyword().toLowerCase())
                        || ad.getDescription().toLowerCase().contains(request.getKeyword().toLowerCase()))
                // Category (includes subcategories)
                .filter(ad -> finalAllowedCategoryIds == null || finalAllowedCategoryIds.contains(ad.getCategory().getId()))
                // City
                .filter(ad -> request.getCityId() == null || ad.getCity().getId().equals(request.getCityId()))
                // Price range
                .filter(ad -> request.getMinPrice() == null || ad.getPrice() >= request.getMinPrice())
                .filter(ad -> request.getMaxPrice() == null || ad.getPrice() <= request.getMaxPrice())
                // Sorting
                .sorted(buildComparator(request))
                .map(this::toSummaryResponse)
                .toList();
    }

    /**
     * Build comparator for sorting.
     * @param request the request
     * @return the result
     */
    private Comparator<AdvertisementEntity> buildComparator(AdvertisementSearchRequest request) {
        Comparator<AdvertisementEntity> comparator = "price".equalsIgnoreCase(request.getSortBy())
                ? Comparator.comparing(AdvertisementEntity::getPrice)
                : Comparator.comparing(AdvertisementEntity::getCreatedAt);

        return "desc".equalsIgnoreCase(request.getSortDirection()) ? comparator.reversed() : comparator;
    }

    /**
     * Ensure current user is owner.
     * @param ad the ad
     * @param currentUser the current user
     */
    private void ensureOwner(AdvertisementEntity ad, UserEntity currentUser) {
        if (!ad.getOwner().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("شما اجازه‌ی تغییر این آگهی را ندارید");
        }
    }

    /**
     * Ensures owner or admin.
     * @param ad the ad
     * @param currentUser the current user
     */
    private void ensureOwnerOrAdmin(AdvertisementEntity ad, UserEntity currentUser) {
        boolean isOwner = ad.getOwner().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole() == RoleEnum.ADMIN;
        if (!isOwner && !isAdmin) {
            throw new UnauthorizedException("شما اجازه‌ی حذف این آگهی را ندارید");
        }
    }

    /**
     * Convert to Summary DTO.
     * @param ad the ad
     * @return the result
     */
    private AdvertisementSummaryResponse toSummaryResponse(AdvertisementEntity ad) {
        String mainImage = (ad.getImages() != null && !ad.getImages().isEmpty())
                ? ad.getImages().get(0).getImagePath() : null;

        return new AdvertisementSummaryResponse(
                ad.getId(), ad.getTitle(), ad.getPrice(), ad.getCity().getName(),
                ad.getCategory().getName(), ad.getStatus(), ad.getCreatedAt(), mainImage
        );
    }

    /**
     * Converts to detail response.
     * @param ad the ad
     * @param currentUser the current user
     * @return the result
     */
    private AdvertisementDetailResponse toDetailResponse(AdvertisementEntity ad, UserEntity currentUser) {
        // Fetch images
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

        // Calculate seller's average rating and count
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