package com.example.vehicledealershipbackend.service;

import com.example.vehicledealershipbackend.dto.dealer.DealerRequest;
import com.example.vehicledealershipbackend.dto.dealer.DealerResponse;
import com.example.vehicledealershipbackend.dto.dealer.DealerUpdateRequest;
import com.example.vehicledealershipbackend.entity.Dealer;
import com.example.vehicledealershipbackend.exception.ResourceNotFoundException;
import com.example.vehicledealershipbackend.mapper.DealerMapper;
import com.example.vehicledealershipbackend.repository.DealerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DealerServiceTest {

    @Mock
    private DealerRepository dealerRepository;
    @Mock
    private DealerMapper dealerMapper;
    @Mock
    private AddressService addressService;

    @InjectMocks
    private DealerService dealerService;

    @Test
    void createShouldFillAddressSaveAndReturnResponse() {
        DealerRequest request = new DealerRequest("Dealer Test", "123", "58400-000");
        Dealer dealer = new Dealer();
        Dealer savedDealer = new Dealer();
        DealerResponse expectedResponse = new DealerResponse();

        when(dealerMapper.toEntity(request)).thenReturn(dealer);
        when(dealerRepository.save(dealer)).thenReturn(savedDealer);
        when(dealerMapper.toResponse(savedDealer)).thenReturn(expectedResponse);

        DealerResponse response = dealerService.create(request);

        verify(addressService).fillDealerAddress(dealer, "58400-000");
        assertSame(expectedResponse, response);
    }

    @Test
    void findByIdShouldThrowWhenDealerDoesNotExist() {
        when(dealerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> dealerService.findById(1L));
    }

    @Test
    void findByIdShouldReturnMappedDealerWhenItExists() throws ResourceNotFoundException {
        Dealer dealer = new Dealer();
        DealerResponse expectedResponse = new DealerResponse();

        when(dealerRepository.findById(1L)).thenReturn(Optional.of(dealer));
        when(dealerMapper.toResponse(dealer)).thenReturn(expectedResponse);

        DealerResponse response = dealerService.findById(1L);

        assertSame(expectedResponse, response);
    }

    @Test
    void findAllShouldMapAllDealers() {
        Dealer firstDealer = new Dealer();
        firstDealer.setId(1L);
        Dealer secondDealer = new Dealer();
        secondDealer.setId(2L);

        DealerResponse firstResponse = new DealerResponse();
        firstResponse.setId(1L);
        DealerResponse secondResponse = new DealerResponse();
        secondResponse.setId(2L);

        when(dealerRepository.findAll()).thenReturn(List.of(firstDealer, secondDealer));
        when(dealerMapper.toResponse(firstDealer)).thenReturn(firstResponse);
        when(dealerMapper.toResponse(secondDealer)).thenReturn(secondResponse);

        List<DealerResponse> responses = dealerService.findAll();

        assertEquals(2, responses.size());
        assertSame(firstResponse, responses.get(0));
        assertSame(secondResponse, responses.get(1));
    }

    @Test
    void updateShouldChangeCorporateNameAndCnpj() throws ResourceNotFoundException {
        DealerUpdateRequest request = new DealerUpdateRequest("New Dealer", "999");
        Dealer dealer = new Dealer();
        dealer.setCorporateName("Old Dealer");
        dealer.setCnpj("111");
        DealerResponse expectedResponse = new DealerResponse();
        expectedResponse.setCorporateName("New Dealer");
        expectedResponse.setCnpj("999");

        when(dealerRepository.findById(1L)).thenReturn(Optional.of(dealer));
        when(dealerRepository.save(dealer)).thenReturn(dealer);
        when(dealerMapper.toResponse(dealer)).thenReturn(expectedResponse);

        DealerResponse response = dealerService.update(1L, request);

        assertEquals("New Dealer", dealer.getCorporateName());
        assertEquals("999", dealer.getCnpj());
        assertSame(expectedResponse, response);
    }

    @Test
    void updateShouldKeepValuesWhenRequestFieldsAreNull() throws ResourceNotFoundException {
        DealerUpdateRequest request = new DealerUpdateRequest(null, null);
        Dealer dealer = new Dealer();
        dealer.setCorporateName("Current Dealer");
        dealer.setCnpj("123");
        DealerResponse expectedResponse = new DealerResponse();

        when(dealerRepository.findById(1L)).thenReturn(Optional.of(dealer));
        when(dealerRepository.save(dealer)).thenReturn(dealer);
        when(dealerMapper.toResponse(dealer)).thenReturn(expectedResponse);

        DealerResponse response = dealerService.update(1L, request);

        assertEquals("Current Dealer", dealer.getCorporateName());
        assertEquals("123", dealer.getCnpj());
        assertSame(expectedResponse, response);
    }

    @Test
    void updateShouldThrowWhenDealerDoesNotExist() {
        when(dealerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> dealerService.update(1L, new DealerUpdateRequest("name", "cnpj")));
        verify(dealerRepository, never()).save(org.mockito.ArgumentMatchers.any(Dealer.class));
    }

    @Test
    void deleteShouldRemoveDealerWhenItExists() throws ResourceNotFoundException {
        Dealer dealer = new Dealer();
        when(dealerRepository.findById(1L)).thenReturn(Optional.of(dealer));

        dealerService.delete(1L);

        verify(dealerRepository).delete(dealer);
    }

    @Test
    void deleteShouldThrowWhenDealerDoesNotExist() {
        when(dealerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> dealerService.delete(1L));
        verify(dealerRepository, never()).delete(org.mockito.ArgumentMatchers.any(Dealer.class));
    }
}
