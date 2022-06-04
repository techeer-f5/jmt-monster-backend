package com.techeer.f5.jmtmonster.domain.region.repository;

import com.techeer.f5.jmtmonster.domain.region.entity.City;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CityRepository extends MongoRepository<City, String> {
}
