package com.example.vehicledealershipbackend.service;

import com.example.vehicledealershipbackend.dto.dealer.DealerRequest;
import com.example.vehicledealershipbackend.dto.dealer.DealerResponse;
import com.example.vehicledealershipbackend.entity.Dealer;
import com.example.vehicledealershipbackend.exception.ResourceNotFoundException;
import com.example.vehicledealershipbackend.mapper.DealerMapper;
import com.example.vehicledealershipbackend.repository.DealerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DealerService {

    private final DealerRepository dealerRepository;
    private final DealerMapper dealerMapper;
    private final AddressService addressService;

    @Transactional
    public DealerResponse create(DealerRequest request) {

        Dealer dealer = dealerMapper.toEntity(request);

        addressService.fillDealerAddress(dealer, request.getCep());
        Dealer savedDealer = dealerRepository.save(dealer);
        return dealerMapper.toResponse(savedDealer);
    }

    @Transactional(readOnly = true)
    public DealerResponse findById(Long id) throws ResourceNotFoundException {

        Dealer dealer = findDealerById(id);
        return dealerMapper.toResponse(dealer);
    }

    @Transactional(readOnly = true)
    public List<DealerResponse> findAll() {
        return dealerRepository.findAll()
                .stream()
                .map(dealerMapper::toResponse)
                .toList();
    }

    @Transactional
    public DealerResponse update(Long id, DealerRequest request) throws ResourceNotFoundException {

        Dealer dealer = findDealerById(id);

        dealer.setCorporateName(request.getCorporateName());
        dealer.setCnpj(request.getCnpj());

        addressService.fillDealerAddress(dealer, request.getCep());

        Dealer updatedDealer = dealerRepository.save(dealer);

        return dealerMapper.toResponse(updatedDealer);
    }

    @Transactional
    public void delete(Long id) throws ResourceNotFoundException {
        Dealer dealer = findDealerById(id);
        dealerRepository.delete(dealer);
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