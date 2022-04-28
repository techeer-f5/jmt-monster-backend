package com.techeer.f5.jmtmonster.domain.restaurant.service;

import com.techeer.f5.jmtmonster.domain.restaurant.entity.Restaurant;
import com.techeer.f5.jmtmonster.domain.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    @Transactional
    public Optional<Restaurant> findOneByCid(Long cid) {
        return restaurantRepository.findByCid(cid);
    }

//    Todo save data from cid
//    @Transactional
//    public Restaurant create(RestaurantSaveDto RestSaveDto) {
//        return restaurantRepository.save(RestSaveDto);
//    }
}
