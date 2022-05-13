package com.techeer.f5.jmtmonster.domain.restaurant.repository;

import com.techeer.f5.jmtmonster.domain.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, UUID> {
    Optional<Restaurant> findByCid(Long cid);
    boolean existsByCid(Long cid);

}
