package com.example.vehicledealershipbackend.dto.dealer;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DealerRequest {
    @NotBlank(message = "Corporate name is required")
    private String corporateName;
    @NotBlank(message = "CNPJ is required")
    private String cnpj;
    @NotBlank(message = "CEP is required")
    private String cep;
}
