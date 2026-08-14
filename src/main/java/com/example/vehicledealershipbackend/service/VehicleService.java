package com.example.vehicledealershipbackend.service;

import com.example.vehicledealershipbackend.dto.vehicle.VehicleRequest;
import com.example.vehicledealershipbackend.dto.vehicle.VehicleResponse;
import com.example.vehicledealershipbackend.entity.Dealer;
import com.example.vehicledealershipbackend.entity.Vehicle;
import com.example.vehicledealershipbackend.exception.ResourceNotFoundException;
import com.example.vehicledealershipbackend.mapper.VehicleMapper;
import com.example.vehicledealershipbackend.repository.DealerRepository;
import com.example.vehicledealershipbackend.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final DealerRepository dealerRepository;
    private final VehicleMapper vehicleMapper;

    @Transactional
    public VehicleResponse create(VehicleRequest request) throws ResourceNotFoundException {
        Vehicle vehicle = vehicleMapper.toEntity(request);

        if (request.getDealerId() != null) {
            Dealer dealer = findDealerById(request.getDealerId());
            vehicle.setDealer(dealer);
        }

        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return vehicleMapper.toResponse(savedVehicle);
    }

    @Transactional(readOnly = true)
    public VehicleResponse findById(Long id) throws ResourceNotFoundException {
        Vehicle vehicle = findVehicleById(id);
        return vehicleMapper.toResponse(vehicle);
    }

    @Transactional(readOnly = true)
    public List<VehicleResponse> findAll() {

        return vehicleRepository.findAll()
                .stream()
                .map(vehicleMapper::toResponse)
                .toList();
    }

    @Transactional
    public VehicleResponse update(Long id, VehicleRequest request) throws ResourceNotFoundException {

        Vehicle vehicle = findVehicleById(id);

        vehicle.setBrand(request.getBrand());
        vehicle.setModel(request.getModel());
        vehicle.setFuelType(request.getFuelType());
        vehicle.setColor(request.getColor());
        vehicle.setYear(request.getYear());
        vehicle.setChassis(request.getChassis());
        vehicle.setPrice(request.getPrice());

        if (request.getDealerId() != null) {
            Dealer dealer = findDealerById(request.getDealerId());
            vehicle.setDealer(dealer);
        } else {

            vehicle.setDealer(null);
        }

        Vehicle updatedVehicle = vehicleRepository.save(vehicle);

        return vehicleMapper.toResponse(updatedVehicle);
    }

    @Transactional
    public void delete(Long id) throws ResourceNotFoundException {
        Vehicle vehicle = findVehicleById(id);
        vehicleRepository.delete(vehicle);
    }

    @Transactional(readOnly = true)
    public List<VehicleResponse> findByDealer(Long dealerId) throws ResourceNotFoundException {

        if (!dealerRepository.existsById(dealerId)) {
            throw new ResourceNotFoundException("Dealer not found with id: " + dealerId);
        }

        return vehicleRepository.findByDealerId(dealerId)
                .stream()
                .map(vehicleMapper::toResponse)
                .toList();
    }

    private Vehicle findVehicleById(Long id) throws ResourceNotFoundException {
        return vehicleRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Vehicle not found with id: " + id));
    }

    private Dealer findDealerById(Long id) throws ResourceNotFoundException {
        return dealerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Dealer not found with id: " + id
                        )
                );
    }
}
