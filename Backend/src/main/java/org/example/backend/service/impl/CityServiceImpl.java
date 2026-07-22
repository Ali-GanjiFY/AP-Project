package org.example.backend.service.impl;

import org.example.backend.dto.request.CityRequest;
import org.example.backend.dto.response.CityResponse;
import org.example.backend.entity.CityEntity;
import org.example.backend.exception.DuplicateResourceException;
import org.example.backend.exception.InvalidInputException;
import org.example.backend.exception.ResourceNotFoundException;
import org.example.backend.repository.AdvertisementRepository;
import org.example.backend.repository.CityRepository;
import org.example.backend.service.CityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Represents city service impl.
 */
@Service
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    private final AdvertisementRepository advertisementRepository; // For checking ad dependencies before deletion

    /**
     * Constructs a new CityServiceImpl.
     * @param cityRepository the city repository
     * @param advertisementRepository the advertisement repository
     */
    public CityServiceImpl(CityRepository cityRepository, AdvertisementRepository advertisementRepository) {
        this.cityRepository = cityRepository;
        this.advertisementRepository = advertisementRepository;
    }

    /**
     * Create a new city.
     * @param request the request
     * @return the result
     */
    @Override
    @Transactional
    public CityResponse createCity(CityRequest request) {
        // Check if city already exists
        if (cityRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("این شهر قبلاً ثبت شده است");
        }
        CityEntity city = new CityEntity(request.getName());
        city.setProvince(request.getProvince());
        return toResponse(cityRepository.save(city));
    }

    /**
     * Update city details.
     * @param id the id
     * @param request the request
     * @return the result
     */
    @Override
    @Transactional
    public CityResponse updateCity(Long id, CityRequest request) {
        CityEntity city = getCityEntityById(id);
        // Check name uniqueness
        if (!city.getName().equals(request.getName()) && cityRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("این شهر قبلاً ثبت شده است");
        }
        city.setName(request.getName());
        city.setProvince(request.getProvince());
        return toResponse(cityRepository.save(city));
    }

    /**
     * Delete city with advertisement dependency.
     * @param id the id
     */
    @Override
    @Transactional
    public void deleteCity(Long id) {
        CityEntity city = getCityEntityById(id);

        // Check if any advertisements use this city
        long advertisementsCount = advertisementRepository.countByCity(city);
        if (advertisementsCount > 0) {
            throw new InvalidInputException(
                    "این شهر دارای " + advertisementsCount + " آگهی است. " +
                            "ابتدا آگهی‌ها را حذف یا به شهر دیگری منتقل کنید."
            );
        }

        cityRepository.delete(city);
    }

    /**
     * Get city by ID.
     * @param id the id
     * @return the result
     */
    @Override
    public CityResponse getCityById(Long id) {
        return toResponse(getCityEntityById(id));
    }

    /**
     * Get city by name (case-insensitive).
     * @param name the name
     * @return the result
     */
    @Override
    @Transactional(readOnly = true)
    public CityResponse getCityByName(String name) {
        CityEntity city = cityRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new ResourceNotFoundException("شهر با نام '" + name + "' یافت نشد"));
        return toResponse(city);
    }

    /**
     * Get city entity by ID.
     * @param id the id
     * @return the result
     */
    @Override
    public CityEntity getCityEntityById(Long id) {
        return cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("شهر یافت نشد"));
    }

    /**
     * Get all cities.
     * @return the result
     */
    @Override
    public List<CityResponse> getAllCities() {
        return cityRepository.findAll().stream().map(this::toResponse).toList();
    }

    /**
     * Convert City entity to CityResponse.
     * @param city the city
     * @return the result
     */
    private CityResponse toResponse(CityEntity city) {
        return new CityResponse(city.getId(), city.getName(), city.getProvince());
    }
}