package com.techeer.f5.jmtmonster.domain.restaurant.repository;

import com.techeer.f5.jmtmonster.domain.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RestaurantRepository extends JpaRepository<Restaurant, UUID> {
    Optional<Restaurant> findByCid(Long cid);
}
