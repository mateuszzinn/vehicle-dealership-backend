package com.example.vehicledealershipbackend.service;

import com.example.vehicledealershipbackend.client.ViaCepClient;
import com.example.vehicledealershipbackend.dto.viacep.ViaCepResponse;
import com.example.vehicledealershipbackend.entity.Dealer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final ViaCepClient viaCepClient;

    public void fillDealerAddress(Dealer dealer, String cep) {
        String cleanCep = cep.replaceAll("\\D", "");

        ViaCepResponse response = viaCepClient.getAddressByZipCode(cleanCep);

        if (response != null && response.getCep() != null) {
            dealer.setStreet(response.getLogradouro());
            dealer.setNeighborhood(response.getBairro());
            dealer.setCity(response.getLocalidade());
            dealer.setState(response.getUf());
        }
    }
}
