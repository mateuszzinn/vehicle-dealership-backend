package com.example.vehicledealershipbackend.dto.vehicle;

import com.example.vehicledealershipbackend.entity.enums.FuelType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleResponse {
    private Long id;
    private String brand;
    private String model;
    private FuelType fuelType;
    private String color;
    private Double price;
    private Integer year;
    private String chassis;
    private String dealerCorporateName;
}
