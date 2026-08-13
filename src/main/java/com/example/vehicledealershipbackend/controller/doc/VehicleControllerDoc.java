package com.example.vehicledealershipbackend.controller.doc;

import com.example.vehicledealershipbackend.dto.vehicle.VehicleRequest;
import com.example.vehicledealershipbackend.dto.vehicle.VehicleResponse;
import com.example.vehicledealershipbackend.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Vehicles", description = "Endpoints for managing vehicles")
public interface VehicleControllerDoc {

    @Operation(
            summary = "List all vehicles",
            description = "Returns all registered vehicles"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Vehicles successfully returned"
    )
    ResponseEntity<List<VehicleResponse>> findAll();

    @Operation(
            summary = "Find vehicle by ID",
            description = "Returns a vehicle based on its unique identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Vehicle successfully found"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Vehicle not found"
            )
    })
    ResponseEntity<VehicleResponse> findById(
            @PathVariable Long id
    ) throws ResourceNotFoundException;

    @Operation(
            summary = "List vehicles by dealer",
            description = "Returns all vehicles associated with a specific dealer"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Vehicles successfully returned"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Dealer not found"
            )
    })
    ResponseEntity<List<VehicleResponse>> findByDealer(
            @PathVariable Long dealerId
    ) throws ResourceNotFoundException;

    @Operation(
            summary = "Create a vehicle",
            description = "Registers a new vehicle and optionally associates it with a dealer"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Vehicle successfully created"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid vehicle data"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Dealer not found"
            )
    })
    ResponseEntity<VehicleResponse> create(
            @Valid @RequestBody VehicleRequest request
    ) throws ResourceNotFoundException;

    @Operation(
            summary = "Update a vehicle",
            description = "Updates the information of an existing vehicle"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Vehicle successfully updated"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid vehicle data"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Vehicle or dealer not found"
            )
    })
    ResponseEntity<VehicleResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody VehicleRequest request
    ) throws ResourceNotFoundException;

    @Operation(
            summary = "Delete a vehicle",
            description = "Deletes an existing vehicle"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Vehicle successfully deleted"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Vehicle not found"
            )
    })
    ResponseEntity<Void> delete(
            @PathVariable Long id
    ) throws ResourceNotFoundException;
}