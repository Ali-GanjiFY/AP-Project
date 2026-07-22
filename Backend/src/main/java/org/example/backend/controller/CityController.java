package org.example.backend.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.example.backend.dto.request.CityRequest;
import org.example.backend.dto.response.CityResponse;
import org.example.backend.service.CityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Represents city controller.
 */
@RestController
@RequestMapping("/api/cities")
@Validated
public class CityController {

    private final CityService cityService;

    /**
     * Constructs a new CityController.
     * @param cityService the city service
     */
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    /**
     * GET /api/cities -> public, list all cities.
     * @return the result
     */
    @GetMapping
    public ResponseEntity<List<CityResponse>> getAllCities() {
        return ResponseEntity.ok(cityService.getAllCities());
    }

    /**
     * GET /api/cities/{id} -> public, city details.
     * @param id the id
     * @return the result
     */
    @GetMapping("/{id}")
    public ResponseEntity<CityResponse> getCityById(@PathVariable Long id) {
        return ResponseEntity.ok(cityService.getCityById(id));
    }

    /**
     * GET /api/cities/by-name?name=... -> public, exact (case-insensitive) match. returns a single exact match.
     * @param name the name
     * @return the result
     */
    @GetMapping("/by-name")
    public ResponseEntity<CityResponse> getCityByName(
            @RequestParam @NotBlank(message = "Name must not be blank") String name) {
        return ResponseEntity.ok(cityService.getCityByName(name));
    }

    /**
     * POST /api/cities -> admin only.
     * @param request the request
     * @return the result
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CityResponse> createCity(@Valid @RequestBody CityRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cityService.createCity(request));
    }

    /**
     * PUT /api/cities/{id} -> admin only.
     * @param id the id
     * @param request the request
     * @return the result
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CityResponse> updateCity(@PathVariable Long id,
                                                   @Valid @RequestBody CityRequest request) {
        return ResponseEntity.ok(cityService.updateCity(id, request));
    }

    /**
     * DELETE /api/cities/{id} -> admin only.
     * @param id the id
     * @return the result
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable Long id) {
        cityService.deleteCity(id);
        return ResponseEntity.noContent().build();
    }
}