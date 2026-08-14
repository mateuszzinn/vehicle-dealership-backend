package com.example.vehicledealershipbackend.dto.vehicle;

import com.example.vehicledealershipbackend.entity.enums.FuelType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleRequest {
    @NotBlank(message = "Brand is required")
    private String brand;
    @NotBlank(message = "Model is required")
    private String model;
    @NotNull(message = "Fuel type is required")
    private FuelType fuelType;
    @NotBlank(message = "Color is required")
    private String color;
    private Double price;
    private Integer year;
    private String chassis;
    private Long dealerId;
}
