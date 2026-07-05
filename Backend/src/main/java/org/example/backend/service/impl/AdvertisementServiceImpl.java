package org.example.backend.service.impl;

import org.example.backend.dto.request.AdvertisementSearchRequest;
import org.example.backend.dto.request.CreateAdvertisementRequest;
import org.example.backend.dto.request.UpdateAdvertisementRequest;
import org.example.backend.dto.response.AdvertisementDetailResponse;
import org.example.backend.dto.response.AdvertisementImageResponse;
import org.example.backend.dto.response.AdvertisementSummaryResponse;
import org.example.backend.dto.response.CategoryResponse;
import org.example.backend.dto.response.CityResponse;
import org.example.backend.entity.Advertisement;
import org.example.backend.entity.AdvertisementImage;
import org.example.backend.entity.Category;
import org.example.backend.entity.City;
import org.example.backend.entity.SellerRating;
import org.example.backend.entity.User;
import org.example.backend.enums.AdvertisementStatus;
import org.example.backend.enums.Role;
import org.example.backend.enums.UserStatus;
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
import java.util.List;
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
    public AdvertisementDetailResponse createAdvertisement(User owner, CreateAdvertisementRequest request) {
        // Only active users can post advertisements
        if (owner.getStatus() != UserStatus.ACTIVE) {
            throw new UnauthorizedException("حساب کاربری شما مسدود است و امکان ثبت آگهی وجود ندارد");
        }

        // Every advertisement must have at least one image
        if (request.getImagePaths() == null || request.getImagePaths().isEmpty()) {
            throw new InvalidInputException("آگهی باید حداقل شامل یک تصویر باشد");
        }

        // Fetch category and city
        Category category = categoryService.getCategoryEntityById(request.getCategoryId());
        City city = cityService.getCityEntityById(request.getCityId());

        // Build and save advertisement
        Advertisement ad = new Advertisement();
        ad.setTitle(request.getTitle());
        ad.setDescription(request.getDescription());
        ad.setPrice(request.getPrice());
        ad.setOwner(owner);
        ad.setCategory(category);
        ad.setCity(city);
        ad.setStatus(AdvertisementStatus.PENDING); // New ads need admin approval
        ad.setCreatedAt(LocalDateTime.now());

        Advertisement saved = advertisementRepository.save(ad);

        // Save all images
        if (request.getImagePaths() != null) {
            for (String path : request.getImagePaths()) {
                advertisementImageRepository.save(new AdvertisementImage(path, saved));
            }
        }

        return getAdvertisementDetail(saved.getId(), owner);
    }

    // Update advertisement: only owner can edit, returns to PENDING for re-review
    @Override
    @Transactional
    public AdvertisementDetailResponse updateAdvertisement(Long adId, User currentUser, UpdateAdvertisementRequest request) {
        Advertisement ad = getAdvertisementEntityById(adId);
        ensureOwner(ad, currentUser); // Only owner can edit

        // Update only provided fields
        if (request.getTitle() != null) ad.setTitle(request.getTitle());
        if (request.getDescription() != null) ad.setDescription(request.getDescription());
        if (request.getPrice() != null) ad.setPrice(request.getPrice());
        if (request.getCategoryId() != null) ad.setCategory(categoryService.getCategoryEntityById(request.getCategoryId()));
        if (request.getCityId() != null) ad.setCity(cityService.getCityEntityById(request.getCityId()));

        // After editing, needs admin approval again
        ad.setStatus(AdvertisementStatus.PENDING);
        ad.setUpdatedAt(LocalDateTime.now());
        advertisementRepository.save(ad);

        return getAdvertisementDetail(adId, currentUser);
    }

    // Soft delete: owner or admin can delete
    @Override
    @Transactional
    public void deleteAdvertisement(Long adId, User currentUser) {
        Advertisement ad = getAdvertisementEntityById(adId);
        ensureOwnerOrAdmin(ad, currentUser); // Owner or admin can delete
        ad.setStatus(AdvertisementStatus.DELETED);
        ad.setUpdatedAt(LocalDateTime.now());
        advertisementRepository.save(ad);
    }

    // Mark as SOLD: only owner, only if currently ACTIVE
    @Override
    @Transactional
    public AdvertisementDetailResponse markAsSold(Long adId, User currentUser) {
        Advertisement ad = getAdvertisementEntityById(adId);
        ensureOwner(ad, currentUser); // Only owner can mark as sold

        // Only active ads can be marked as sold
        if (ad.getStatus() != AdvertisementStatus.ACTIVE) {
            throw new InvalidInputException("فقط آگهی فعال را می‌توان فروخته‌شده علامت زد");
        }

        ad.setStatus(AdvertisementStatus.SOLD);
        ad.setUpdatedAt(LocalDateTime.now());
        advertisementRepository.save(ad);
        return getAdvertisementDetail(adId, currentUser);
    }

    // Internal method: change status (used only by AdminReviewService)
    @Override
    @Transactional
    public void changeStatus(Advertisement ad, AdvertisementStatus newStatus) {
        ad.setStatus(newStatus);
        ad.setUpdatedAt(LocalDateTime.now());
        advertisementRepository.save(ad);
    }

    // Get full advertisement details with seller ratings
    @Override
    public AdvertisementDetailResponse getAdvertisementDetail(Long adId, User currentUser) {
        return toDetailResponse(getAdvertisementEntityById(adId), currentUser);
    }

    // Get advertisement entity by ID (internal use by other services)
    @Override
    public Advertisement getAdvertisementEntityById(Long adId) {
        return advertisementRepository.findById(adId)
                .orElseThrow(() -> new ResourceNotFoundException("آگهی یافت نشد"));
    }

    // Get all active advertisements for public browsing
    @Override
    @Transactional(readOnly = true)
    public List<AdvertisementSummaryResponse> getAllActiveAds() {
        return advertisementRepository.findByStatus(AdvertisementStatus.ACTIVE)
                .stream()
                .map(this::toSummaryResponse)
                .collect(Collectors.toList());
    }

    // Get all advertisements owned by a specific user
    @Override
    public List<AdvertisementSummaryResponse> getMyAdvertisements(User owner) {
        return advertisementRepository.findByOwner(owner).stream().map(this::toSummaryResponse).toList();
    }

    // Get pending advertisements for admin review (oldest first)
    @Override
    public List<AdvertisementSummaryResponse> getPendingAdvertisements() {
        return advertisementRepository.findByStatusOrderByCreatedAtAsc(AdvertisementStatus.PENDING)
                .stream().map(this::toSummaryResponse).toList();
    }

    // Search and filter active advertisements
    @Override
    public List<AdvertisementSummaryResponse> searchAdvertisements(AdvertisementSearchRequest request) {
        // Validate price range
        if (request.getMinPrice() != null && request.getMaxPrice() != null
                && request.getMinPrice() > request.getMaxPrice()) {
            throw new InvalidInputException("حداقل قیمت نمی‌تواند از حداکثر قیمت بیشتر باشد");
        }

        // Start with all active ads
        List<Advertisement> ads = advertisementRepository.findByStatus(AdvertisementStatus.ACTIVE);

        // Apply filters using stream (in-memory filtering)
        return ads.stream()
                // Keyword search: title or description
                .filter(ad -> request.getKeyword() == null || request.getKeyword().isBlank()
                        || ad.getTitle().toLowerCase().contains(request.getKeyword().toLowerCase())
                        || ad.getDescription().toLowerCase().contains(request.getKeyword().toLowerCase()))
                // Category filter
                .filter(ad -> request.getCategoryId() == null || ad.getCategory().getId().equals(request.getCategoryId()))
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
    private Comparator<Advertisement> buildComparator(AdvertisementSearchRequest request) {
        Comparator<Advertisement> comparator = "price".equalsIgnoreCase(request.getSortBy())
                ? Comparator.comparing(Advertisement::getPrice)
                : Comparator.comparing(Advertisement::getCreatedAt);

        return "desc".equalsIgnoreCase(request.getSortDirection()) ? comparator.reversed() : comparator;
    }

    // Ensure current user is the owner
    private void ensureOwner(Advertisement ad, User currentUser) {
        if (!ad.getOwner().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("شما اجازه‌ی تغییر این آگهی را ندارید");
        }
    }

    // Ensure current user is either owner or admin
    private void ensureOwnerOrAdmin(Advertisement ad, User currentUser) {
        boolean isOwner = ad.getOwner().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;
        if (!isOwner && !isAdmin) {
            throw new UnauthorizedException("شما اجازه‌ی حذف این آگهی را ندارید");
        }
    }

    // Convert Advertisement to Summary DTO (for list views)
    private AdvertisementSummaryResponse toSummaryResponse(Advertisement ad) {
        String mainImage = (ad.getImages() != null && !ad.getImages().isEmpty())
                ? ad.getImages().get(0).getImagePath() : null;

        return new AdvertisementSummaryResponse(
                ad.getId(), ad.getTitle(), ad.getPrice(), ad.getCity().getName(),
                ad.getCategory().getName(), ad.getStatus(), ad.getCreatedAt(), mainImage
        );
    }

    // Convert Advertisement to Detail DTO with full data and seller ratings
    private AdvertisementDetailResponse toDetailResponse(Advertisement ad, User currentUser) {
        // Fetch real images
        List<AdvertisementImageResponse> images = advertisementImageRepository.findByAdvertisement(ad).stream()
                .map(img -> new AdvertisementImageResponse(img.getId(), img.getImagePath()))
                .toList();

        // Build category response with parent info
        Category category = ad.getCategory();
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
        User owner = ad.getOwner();
        List<SellerRating> ratings = sellerRatingRepository.findBySeller(owner);
        double avgRating = ratings.stream().mapToInt(SellerRating::getScore).average().orElse(0.0);
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