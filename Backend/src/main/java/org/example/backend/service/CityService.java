package org.example.backend.service;

import org.example.backend.dto.request.CityRequest;
import org.example.backend.dto.response.CityResponse;
import org.example.backend.entity.City;

import java.util.List;

public interface CityService {

    // Create a new city with unique name validation
    CityResponse createCity(CityRequest request);

    // Update city details (name, province)
    CityResponse updateCity(Long id, CityRequest request);

    // Delete city by ID (with advertisement dependency check)
    void deleteCity(Long id);

    // Get city by ID as DTO
    CityResponse getCityById(Long id);

    // Get city by name (case-insensitive)
    CityResponse getCityByName(String name);

    // Get city entity by ID (internal use by other services)
    City getCityEntityById(Long id);

    // Get list of all cities
    List<CityResponse> getAllCities();
}