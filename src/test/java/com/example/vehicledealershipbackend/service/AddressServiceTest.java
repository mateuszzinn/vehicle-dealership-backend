package com.example.vehicledealershipbackend.service;

import com.example.vehicledealershipbackend.client.ViaCepClient;
import com.example.vehicledealershipbackend.dto.viacep.ViaCepResponse;
import com.example.vehicledealershipbackend.entity.Dealer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @Mock
    private ViaCepClient viaCepClient;

    @InjectMocks
    private AddressService addressService;

    @Test
    void fillDealerAddressShouldCleanCepAndFillAddressWhenViaCepReturnsValidData() {
        Dealer dealer = new Dealer();
        ViaCepResponse response = new ViaCepResponse();
        response.setCep("58400-000");
        response.setLogradouro("Rua A");
        response.setBairro("Centro");
        response.setLocalidade("Campina Grande");
        response.setUf("PB");

        when(viaCepClient.getAddressByZipCode("58400000")).thenReturn(response);

        addressService.fillDealerAddress(dealer, "58400-000");

        verify(viaCepClient).getAddressByZipCode("58400000");
        assertEquals("Rua A", dealer.getStreet());
        assertEquals("Centro", dealer.getNeighborhood());
        assertEquals("Campina Grande", dealer.getCity());
        assertEquals("PB", dealer.getState());
    }

    @Test
    void fillDealerAddressShouldNotChangeDealerWhenViaCepReturnsNull() {
        Dealer dealer = new Dealer();
        dealer.setStreet("Old Street");
        dealer.setNeighborhood("Old Neighborhood");
        dealer.setCity("Old City");
        dealer.setState("OS");

        when(viaCepClient.getAddressByZipCode("58400000")).thenReturn(null);

        addressService.fillDealerAddress(dealer, "58400-000");

        assertEquals("Old Street", dealer.getStreet());
        assertEquals("Old Neighborhood", dealer.getNeighborhood());
        assertEquals("Old City", dealer.getCity());
        assertEquals("OS", dealer.getState());
    }

    @Test
    void fillDealerAddressShouldNotChangeDealerWhenResponseCepIsNull() {
        Dealer dealer = new Dealer();
        dealer.setStreet("Old Street");
        dealer.setNeighborhood("Old Neighborhood");
        dealer.setCity("Old City");
        dealer.setState("OS");
        ViaCepResponse response = new ViaCepResponse();
        response.setCep(null);

        when(viaCepClient.getAddressByZipCode("58400000")).thenReturn(response);

        addressService.fillDealerAddress(dealer, "58400-000");

        assertEquals("Old Street", dealer.getStreet());
        assertEquals("Old Neighborhood", dealer.getNeighborhood());
        assertEquals("Old City", dealer.getCity());
        assertEquals("OS", dealer.getState());
    }
}
