package com.techeer.f5.jmtmonster.domain.region.parser;

import com.techeer.f5.jmtmonster.domain.region.dto.CityDto;
import com.techeer.f5.jmtmonster.domain.region.dto.DistrictDto;
import com.techeer.f5.jmtmonster.domain.region.dto.ParsedRegionalDataDto;
import com.techeer.f5.jmtmonster.domain.region.dto.RegionDto;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class RegionalDataParserImpl implements RegionalDataParser {
    private final static String basePath = "src/main/resources/regional";
    private final static String cityFilePath = basePath + "/city.json";
    private final static String districtFilePath = basePath + "/district.json";
    private final static String regionFilePath = basePath + "/region.json";

    private CityDto parseCity() {
        String json = getStringFromPath(cityFilePath);
    }

    private DistrictDto parseDistrict() {
        String json = getStringFromPath(districtFilePath);

    }

    private RegionDto parseRegion() {
        String json = getStringFromPath(regionFilePath);

    }

    private String getStringFromPath(String path) {
        try {
            return Files.readString(Paths.get(path));
        } catch (IOException exception) {
            return null;
        }
    }

    @Override
    public ParsedRegionalDataDto getData() {


        return null;
    }
}
