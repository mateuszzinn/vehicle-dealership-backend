package com.example.vehicledealershipbackend.dto.dealer;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CNPJ;
import jakarta.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DealerRequest {
    @NotBlank(message = "Corporate name is required")
    private String corporateName;
    @NotBlank(message = "CNPJ is required")
    @CNPJ(message = "Invalid CNPJ format")
    private String cnpj;
    @NotBlank(message = "CEP is required")
    @Pattern(regexp = "^\\d{5}-?\\d{3}$", message = "Invalid CEP")
    private String cep;
}
