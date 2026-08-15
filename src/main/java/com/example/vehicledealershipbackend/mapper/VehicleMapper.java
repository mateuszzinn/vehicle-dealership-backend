package com.example.vehicledealershipbackend.mapper;

import com.example.vehicledealershipbackend.dto.vehicle.VehicleRequest;
import com.example.vehicledealershipbackend.dto.vehicle.VehicleResponse;
import com.example.vehicledealershipbackend.entity.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dealer", ignore = true)
    Vehicle toEntity(VehicleRequest request);

    @Mapping(source = "dealer.corporateName", target = "dealerCorporateName")
    VehicleResponse toResponse(Vehicle vehicle);
}