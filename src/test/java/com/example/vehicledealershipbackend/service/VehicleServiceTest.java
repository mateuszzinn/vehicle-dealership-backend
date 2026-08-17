package com.example.vehicledealershipbackend.service;

import com.example.vehicledealershipbackend.dto.vehicle.VehicleRequest;
import com.example.vehicledealershipbackend.dto.vehicle.VehicleResponse;
import com.example.vehicledealershipbackend.entity.Dealer;
import com.example.vehicledealershipbackend.entity.Vehicle;
import com.example.vehicledealershipbackend.entity.enums.FuelType;
import com.example.vehicledealershipbackend.exception.ResourceNotFoundException;
import com.example.vehicledealershipbackend.mapper.VehicleMapper;
import com.example.vehicledealershipbackend.repository.DealerRepository;
import com.example.vehicledealershipbackend.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;
    @Mock
    private DealerRepository dealerRepository;
    @Mock
    private VehicleMapper vehicleMapper;

    @InjectMocks
    private VehicleService vehicleService;

    @Test
    void createShouldAssociateDealerWhenDealerIdIsProvided() throws ResourceNotFoundException {
        VehicleRequest request = buildRequest();
        request.setDealerId(5L);
        Vehicle vehicle = new Vehicle();
        Dealer dealer = new Dealer();
        VehicleResponse expectedResponse = new VehicleResponse();

        when(vehicleMapper.toEntity(request)).thenReturn(vehicle);
        when(dealerRepository.findById(5L)).thenReturn(Optional.of(dealer));
        when(vehicleRepository.save(vehicle)).thenReturn(vehicle);
        when(vehicleMapper.toResponse(vehicle)).thenReturn(expectedResponse);

        VehicleResponse response = vehicleService.create(request);

        assertSame(dealer, vehicle.getDealer());
        assertSame(expectedResponse, response);
    }

    @Test
    void createShouldThrowWhenDealerDoesNotExist() {
        VehicleRequest request = buildRequest();
        request.setDealerId(99L);

        when(vehicleMapper.toEntity(request)).thenReturn(new Vehicle());
        when(dealerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> vehicleService.create(request));
        verify(vehicleRepository, never()).save(org.mockito.ArgumentMatchers.any(Vehicle.class));
    }

    @Test
    void createShouldSaveWithoutDealerWhenDealerIdIsNull() throws ResourceNotFoundException {
        VehicleRequest request = buildRequest();
        request.setDealerId(null);
        Vehicle vehicle = new Vehicle();
        vehicle.setDealer(new Dealer());
        VehicleResponse expectedResponse = new VehicleResponse();

        when(vehicleMapper.toEntity(request)).thenReturn(vehicle);
        when(vehicleRepository.save(vehicle)).thenReturn(vehicle);
        when(vehicleMapper.toResponse(vehicle)).thenReturn(expectedResponse);

        VehicleResponse response = vehicleService.create(request);

        verify(dealerRepository, never()).findById(org.mockito.ArgumentMatchers.anyLong());
        assertSame(expectedResponse, response);
    }

    @Test
    void findByIdShouldReturnMappedVehicleWhenItExists() throws ResourceNotFoundException {
        Vehicle vehicle = new Vehicle();
        VehicleResponse expectedResponse = new VehicleResponse();

        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        when(vehicleMapper.toResponse(vehicle)).thenReturn(expectedResponse);

        VehicleResponse response = vehicleService.findById(1L);

        assertSame(expectedResponse, response);
    }

    @Test
    void findByIdShouldThrowWhenVehicleDoesNotExist() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> vehicleService.findById(1L));
    }

    @Test
    void findAllShouldReturnMappedVehicles() {
        Vehicle firstVehicle = new Vehicle();
        firstVehicle.setId(1L);
        Vehicle secondVehicle = new Vehicle();
        secondVehicle.setId(2L);
        VehicleResponse firstResponse = new VehicleResponse();
        VehicleResponse secondResponse = new VehicleResponse();

        when(vehicleRepository.findAll()).thenReturn(List.of(firstVehicle, secondVehicle));
        when(vehicleMapper.toResponse(firstVehicle)).thenReturn(firstResponse);
        when(vehicleMapper.toResponse(secondVehicle)).thenReturn(secondResponse);

        List<VehicleResponse> responses = vehicleService.findAll();

        assertEquals(2, responses.size());
        assertSame(firstResponse, responses.get(0));
        assertSame(secondResponse, responses.get(1));
    }

    @Test
    void updateShouldChangeFieldsAndRemoveDealerWhenDealerIdIsNull() throws ResourceNotFoundException {
        VehicleRequest request = buildRequest();
        request.setDealerId(null);
        request.setBrand("Chevrolet");
        request.setModel("Onix");
        request.setPrice(98000.00);

        Vehicle vehicle = new Vehicle();
        vehicle.setDealer(new Dealer());
        VehicleResponse expectedResponse = new VehicleResponse();

        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        when(vehicleRepository.save(vehicle)).thenReturn(vehicle);
        when(vehicleMapper.toResponse(vehicle)).thenReturn(expectedResponse);

        VehicleResponse response = vehicleService.update(1L, request);

        verify(vehicleMapper).updateEntityFromRequest(request, vehicle);
        assertNull(vehicle.getDealer());
        assertSame(expectedResponse, response);
    }

    @Test
    void updateShouldAssociateDealerWhenDealerIdIsProvided() throws ResourceNotFoundException {
        VehicleRequest request = buildRequest();
        request.setDealerId(8L);
        Dealer expectedDealer = new Dealer();
        Vehicle vehicle = new Vehicle();
        vehicle.setDealer(null);
        VehicleResponse expectedResponse = new VehicleResponse();

        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        when(dealerRepository.findById(8L)).thenReturn(Optional.of(expectedDealer));
        when(vehicleRepository.save(vehicle)).thenReturn(vehicle);
        when(vehicleMapper.toResponse(vehicle)).thenReturn(expectedResponse);

        VehicleResponse response = vehicleService.update(1L, request);

        assertSame(expectedDealer, vehicle.getDealer());
        assertSame(expectedResponse, response);
    }

    @Test
    void updateShouldThrowWhenDealerDoesNotExist() {
        VehicleRequest request = buildRequest();
        request.setDealerId(8L);
        Vehicle vehicle = new Vehicle();

        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        when(dealerRepository.findById(8L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> vehicleService.update(1L, request));
        verify(vehicleRepository, never()).save(org.mockito.ArgumentMatchers.any(Vehicle.class));
    }

    @Test
    void updateShouldThrowWhenVehicleDoesNotExist() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> vehicleService.update(1L, buildRequest()));
    }

    @Test
    void findByDealerShouldThrowWhenDealerDoesNotExist() {
        when(dealerRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> vehicleService.findByDealer(10L));
        verify(vehicleRepository, never()).findByDealerId(10L);
    }

    @Test
    void findByDealerShouldReturnMappedVehicles() throws ResourceNotFoundException {
        Vehicle vehicle = new Vehicle();
        VehicleResponse expectedResponse = new VehicleResponse();
        expectedResponse.setId(1L);

        when(dealerRepository.findById(10L)).thenReturn(Optional.of(new Dealer()));
        when(vehicleRepository.findByDealerId(10L)).thenReturn(List.of(vehicle));
        when(vehicleMapper.toResponse(vehicle)).thenReturn(expectedResponse);

        List<VehicleResponse> responses = vehicleService.findByDealer(10L);

        assertEquals(1, responses.size());
        assertSame(expectedResponse, responses.getFirst());
    }

    @Test
    void deleteShouldRemoveVehicleWhenItExists() throws ResourceNotFoundException {
        Vehicle vehicle = new Vehicle();
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));

        vehicleService.delete(1L);

        verify(vehicleRepository).delete(vehicle);
    }

    @Test
    void deleteShouldThrowWhenVehicleDoesNotExist() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> vehicleService.delete(1L));
        verify(vehicleRepository, never()).delete(org.mockito.ArgumentMatchers.any(Vehicle.class));
    }

    private VehicleRequest buildRequest() {
        VehicleRequest request = new VehicleRequest();
        request.setBrand("Fiat");
        request.setModel("Pulse");
        request.setFuelType(FuelType.FLEX);
        request.setColor("White");
        request.setPrice(120000.00);
        request.setYear(2024);
        request.setChassis("CHASSIS123");
        return request;
    }
}
