package com.example.vehicledealershipbackend.repository;

import com.example.vehicledealershipbackend.entity.Dealer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DealerRepository extends JpaRepository<Dealer, Long> {
}
