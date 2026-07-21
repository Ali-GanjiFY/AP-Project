package org.example.backend.service;

import org.example.backend.dto.request.CityRequest;
import org.example.backend.dto.response.CityResponse;
import org.example.backend.entity.CityEntity;

import java.util.List;

public interface CityService {

    // Create new city
    CityResponse createCity(CityRequest request);

    // Update city
    CityResponse updateCity(Long id, CityRequest request);

    // Delete city (checks for existing ads)
    void deleteCity(Long id);

    // Get city by ID
    CityResponse getCityById(Long id);

    // Get city by name (case-insensitive)
    CityResponse getCityByName(String name);

    // Internal use only
    CityEntity getCityEntityById(Long id);

    // List all cities
    List<CityResponse> getAllCities();
}