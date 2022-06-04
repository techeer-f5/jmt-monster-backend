package com.techeer.f5.jmtmonster.domain.region.repository;

import com.techeer.f5.jmtmonster.domain.region.entity.Region;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RegionalRepository extends MongoRepository<Region, String> {
}
