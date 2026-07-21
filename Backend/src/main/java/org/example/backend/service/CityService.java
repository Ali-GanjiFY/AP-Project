package org.example.backend.service;

import org.example.backend.dto.request.CityRequest;
import org.example.backend.dto.response.CityResponse;
import org.example.backend.entity.CityEntity;

import java.util.List;

/**
 * Contract for city service.
 */
public interface CityService {

    /**
     * Create new city.
     * @param request the request
     * @return the result
     */
    CityResponse createCity(CityRequest request);

    /**
     * Update city.
     * @param id the id
     * @param request the request
     * @return the result
     */
    CityResponse updateCity(Long id, CityRequest request);

    /**
     * Delete city (checks for existing ads).
     * @param id the id
     */
    void deleteCity(Long id);

    /**
     * Get city by ID.
     * @param id the id
     * @return the result
     */
    CityResponse getCityById(Long id);

    /**
     * Get city by name (case-insensitive).
     * @param name the name
     * @return the result
     */
    CityResponse getCityByName(String name);

    /**
     * Internal use only.
     * @param id the id
     * @return the result
     */
    CityEntity getCityEntityById(Long id);

    /**
     * List all cities.
     * @return the result
     */
    List<CityResponse> getAllCities();
}