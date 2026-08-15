package com.example.vehicledealershipbackend.service;

import com.example.vehicledealershipbackend.client.ViaCepClient;
import com.example.vehicledealershipbackend.dto.viacep.ViaCepResponse;
import com.example.vehicledealershipbackend.entity.Dealer;
import com.example.vehicledealershipbackend.exception.ExternalServiceException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final ViaCepClient viaCepClient;

    public void fillDealerAddress(Dealer dealer, String cep) throws ExternalServiceException {
        String cleanCep = cep.replaceAll("\\D", "");

        ViaCepResponse response;
        try {
            response = viaCepClient.getAddressByZipCode(cleanCep);
        } catch (FeignException ex) {
            throw new ExternalServiceException("Failed to fetch address from ViaCEP for CEP: " + cep);
        }

        if (response == null || response.getCep() == null) {
            throw new ExternalServiceException("Invalid address response from ViaCEP for CEP: " + cep);
        }

        dealer.setStreet(response.getLogradouro());
        dealer.setNeighborhood(response.getBairro());
        dealer.setCity(response.getLocalidade());
        dealer.setState(response.getUf());
    }
}
