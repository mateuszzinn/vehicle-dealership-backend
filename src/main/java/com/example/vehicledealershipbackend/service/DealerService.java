package com.example.vehicledealershipbackend.service;

import com.example.vehicledealershipbackend.dto.dealer.DealerRequest;
import com.example.vehicledealershipbackend.dto.dealer.DealerResponse;
import com.example.vehicledealershipbackend.dto.dealer.DealerUpdateRequest;
import com.example.vehicledealershipbackend.entity.Dealer;
import com.example.vehicledealershipbackend.exception.ExternalServiceException;
import com.example.vehicledealershipbackend.exception.ResourceNotFoundException;
import com.example.vehicledealershipbackend.exception.ResourceConflictException;
import com.example.vehicledealershipbackend.mapper.DealerMapper;
import com.example.vehicledealershipbackend.repository.DealerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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
    public DealerResponse create(DealerRequest request) throws ResourceConflictException, ExternalServiceException {
        validateUniqueCnpj(request.getCnpj());

        Dealer dealer = dealerMapper.toEntity(request);

        addressService.fillDealerAddress(dealer, request.getCep());
        Dealer savedDealer = saveDealerWithConflictHandling(dealer, request.getCnpj());
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
    public DealerResponse update(Long id, DealerUpdateRequest request) throws ResourceNotFoundException, ResourceConflictException {
        Dealer dealer = findDealerById(id);

        if(request.getCorporateName() != null) {
            dealer.setCorporateName(request.getCorporateName());
        } if(request.getCnpj() != null) {
            if (!request.getCnpj().equals(dealer.getCnpj())) {
                validateUniqueCnpj(request.getCnpj());
            }
            dealer.setCnpj(request.getCnpj());
        }

        Dealer updatedDealer = saveDealerWithConflictHandling(dealer, dealer.getCnpj());
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

    private void validateUniqueCnpj(String cnpj) throws ResourceConflictException {
        if (dealerRepository.existsByCnpj(cnpj)) {
            throw new ResourceConflictException("Dealer already exists with cnpj: " + cnpj);
        }
    }

    private Dealer saveDealerWithConflictHandling(Dealer dealer, String cnpj) throws ResourceConflictException {
        try {
            return dealerRepository.save(dealer);
        } catch (DataIntegrityViolationException ex) {
            throw new ResourceConflictException("Dealer already exists with cnpj: " + cnpj);
        }
    }
}