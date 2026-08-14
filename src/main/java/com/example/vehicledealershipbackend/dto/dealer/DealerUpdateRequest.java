package com.example.vehicledealershipbackend.dto.dealer;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DealerUpdateRequest {
    @NotBlank(message = "Corporate name is required")
    private String corporateName;
    @NotBlank(message = "CNPJ is required")
    private String cnpj;
}
