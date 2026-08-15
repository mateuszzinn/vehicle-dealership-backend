package com.example.vehicledealershipbackend.service;

import com.example.vehicledealershipbackend.client.ViaCepClient;
import com.example.vehicledealershipbackend.dto.viacep.ViaCepResponse;
import com.example.vehicledealershipbackend.entity.Dealer;
import com.example.vehicledealershipbackend.exception.ExternalServiceException;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @Mock
    private ViaCepClient viaCepClient;

    @InjectMocks
    private AddressService addressService;

    @Test
    void fillDealerAddressShouldCleanCepAndFillAddressWhenViaCepReturnsValidData() throws ExternalServiceException {
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
    void fillDealerAddressShouldThrowWhenViaCepReturnsNull() {
        Dealer dealer = new Dealer();

        when(viaCepClient.getAddressByZipCode("58400000")).thenReturn(null);

        assertThrows(ExternalServiceException.class, () -> addressService.fillDealerAddress(dealer, "58400-000"));
    }

    @Test
    void fillDealerAddressShouldThrowWhenResponseCepIsNull() {
        Dealer dealer = new Dealer();
        ViaCepResponse response = new ViaCepResponse();
        response.setCep(null);

        when(viaCepClient.getAddressByZipCode("58400000")).thenReturn(response);

        assertThrows(ExternalServiceException.class, () -> addressService.fillDealerAddress(dealer, "58400-000"));
    }

    @Test
    void fillDealerAddressShouldThrowWhenViaCepClientFails() {
        Dealer dealer = new Dealer();

        when(viaCepClient.getAddressByZipCode("58400000"))
                .thenThrow(mock(FeignException.class));

        assertThrows(ExternalServiceException.class, () -> addressService.fillDealerAddress(dealer, "58400-000"));
    }
}
