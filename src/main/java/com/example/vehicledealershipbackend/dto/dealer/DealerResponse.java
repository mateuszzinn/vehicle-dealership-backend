package com.example.vehicledealershipbackend.dto.dealer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DealerResponse {
    private Long id;
    private String corporateName;
    private String cnpj;
    private String cep;
    private String street;
    private String neighborhood;
    private String city;
    private String state;
    private List<Long> vehicleIds;
}
