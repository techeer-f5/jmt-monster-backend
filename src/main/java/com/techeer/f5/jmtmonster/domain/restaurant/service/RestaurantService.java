package com.techeer.f5.jmtmonster.domain.restaurant.service;

import com.techeer.f5.jmtmonster.domain.restaurant.entity.Restaurant;
import com.techeer.f5.jmtmonster.domain.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final WebClient webClient;
    private final RestaurantRepository restaurantRepository;

    @Transactional
    public Optional<Restaurant> findOneByCid(Long cid) {
        return restaurantRepository.findByCid(cid);
    }

    public RestaurantService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://jsonplaceholder.typicode.com").build();
    }
    public String getFirstTodosTest() {
        String response =
                this.webClient.get().uri("/todos/1")
                        .retrieve().bodyToMono(String.class)
                        .block();
        return response;
    }

//    Todo save data from cid
//    @Transactional
//    public Restaurant create(RestaurantSaveDto RestSaveDto) {
//        return restaurantRepository.save(RestSaveDto);
//    }
}
