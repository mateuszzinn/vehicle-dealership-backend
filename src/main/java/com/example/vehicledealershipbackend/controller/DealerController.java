package com.example.vehicledealershipbackend.controller;

import com.example.vehicledealershipbackend.controller.doc.DealerControllerDoc;
import com.example.vehicledealershipbackend.dto.dealer.DealerRequest;
import com.example.vehicledealershipbackend.dto.dealer.DealerResponse;
import com.example.vehicledealershipbackend.exception.ResourceNotFoundException;
import com.example.vehicledealershipbackend.service.DealerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dealer")
@RequiredArgsConstructor
public class DealerController implements DealerControllerDoc {

    private final DealerService dealerService;

    @GetMapping
    public ResponseEntity<List<DealerResponse>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(dealerService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DealerResponse> findById(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(dealerService.findById(id));
    }

    @PostMapping
    public ResponseEntity<DealerResponse> create(@Valid @RequestBody DealerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dealerService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DealerResponse> update(@PathVariable Long id, @Valid @RequestBody DealerRequest request) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(dealerService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws ResourceNotFoundException {
        dealerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
