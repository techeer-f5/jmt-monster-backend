package com.techeer.f5.jmtmonster.domain.region.repository;

import com.techeer.f5.jmtmonster.domain.region.entity.District;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DistrictRepository extends MongoRepository<District, String> {
}
