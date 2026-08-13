package com.example.vehicledealershipbackend.controller;

import com.example.vehicledealershipbackend.dto.vehicle.VehicleRequest;
import com.example.vehicledealershipbackend.dto.vehicle.VehicleResponse;
import com.example.vehicledealershipbackend.exception.ResourceNotFoundException;
import com.example.vehicledealershipbackend.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @GetMapping
    public ResponseEntity<List<VehicleResponse>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(vehicleService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleResponse> findById(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(vehicleService.findById(id));
    }

    @GetMapping("/dealer/{dealerId}")
    public ResponseEntity<List<VehicleResponse>> findByDealer(@PathVariable Long dealerId) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(vehicleService.findByDealer(dealerId));
    }

    @PostMapping
    public ResponseEntity<VehicleResponse> create(@Valid @RequestBody VehicleRequest request) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED).body(vehicleService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehicleResponse> update(@PathVariable Long id, @Valid @RequestBody VehicleRequest request) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(vehicleService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws ResourceNotFoundException {
        vehicleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
