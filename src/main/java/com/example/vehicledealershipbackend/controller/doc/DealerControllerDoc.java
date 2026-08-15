package com.example.vehicledealershipbackend.controller.doc;

import com.example.vehicledealershipbackend.dto.dealer.DealerRequest;
import com.example.vehicledealershipbackend.dto.dealer.DealerResponse;
import com.example.vehicledealershipbackend.dto.dealer.DealerUpdateRequest;
import com.example.vehicledealershipbackend.exception.ExternalServiceException;
import com.example.vehicledealershipbackend.exception.ResourceConflictException;
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

@Tag(name = "Dealers", description = "Endpoints for managing vehicle dealerships")
public interface DealerControllerDoc {

    @Operation(
            summary = "List all dealers",
            description = "Returns all registered vehicle dealerships"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Dealers successfully returned"
    )
    ResponseEntity<List<DealerResponse>> findAll();

    @Operation(
            summary = "Find dealer by ID",
            description = "Returns a dealer based on its unique identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Dealer successfully found"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Dealer not found"
            )
    })
    ResponseEntity<DealerResponse> findById(
            @PathVariable Long id
    ) throws ResourceNotFoundException;

    @Operation(
            summary = "Create a dealer",
            description = "Registers a new vehicle dealership"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Dealer successfully created"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid dealer data"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Dealer already exists for provided CNPJ"
            ),
            @ApiResponse(
                    responseCode = "502",
                    description = "Failed to fetch dealer address from external service"
            )
    })
    ResponseEntity<DealerResponse> create(
            @Valid @RequestBody DealerRequest request
    ) throws ResourceConflictException, ExternalServiceException;

    @Operation(
            summary = "Update a dealer",
            description = "Updates the information of an existing dealer"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Dealer successfully updated"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid dealer data"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Dealer not found"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Dealer already exists for provided CNPJ"
            )
    })
    ResponseEntity<DealerResponse> update(
            @PathVariable Long id, @Valid @RequestBody DealerUpdateRequest request)
            throws ResourceNotFoundException, ResourceConflictException;

    @Operation(
            summary = "Delete a dealer",
            description = "Deletes an existing dealer"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Dealer successfully deleted"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Dealer not found"
            )
    })
    ResponseEntity<Void> delete(
            @PathVariable Long id
    ) throws ResourceNotFoundException;
}