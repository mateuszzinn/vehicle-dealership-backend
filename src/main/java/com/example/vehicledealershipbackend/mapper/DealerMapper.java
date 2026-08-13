package com.example.vehicledealershipbackend.mapper;

import com.example.vehicledealershipbackend.dto.dealer.DealerRequest;
import com.example.vehicledealershipbackend.dto.dealer.DealerResponse;
import com.example.vehicledealershipbackend.entity.Dealer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DealerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "vehicles", ignore = true)
    Dealer toEntity(DealerRequest request);

    @Mapping(
            target = "vehicleIds",
            expression = "java(dealer.getVehicles() == null ? java.util.List.of() : dealer.getVehicles().stream().map(vehicle -> vehicle.getId()).toList())"
    )
    DealerResponse toResponse(Dealer dealer);
}
